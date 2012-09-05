package vn.ducquoc.storm.bolt;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.rometools.fetcher.FeedFetcher;
import org.rometools.fetcher.impl.HttpURLFeedFetcher;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * @see backtype.storm.task.IBolt
 * @see org.rometools.fetcher.impl.HttpURLFeedFetcher
 */
@SuppressWarnings("rawtypes")
public class FeedFetcherBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;

    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        FeedFetcher feedFetcher = new HttpURLFeedFetcher();
        String feedUrl = input.getStringByField("feed");
        try {
            SyndFeed feed = feedFetcher.retrieveFeed(new URL(feedUrl));
            for (Object obj : feed.getEntries()) {
                SyndEntry syndEntry = (SyndEntry) obj;
                Date entryDate = getSyndDate(syndEntry, feed);
                collector.emit(new Values(syndEntry.getLink(), entryDate.getTime(), removeHtmlBoilerplate(syndEntry
                        .getDescription().getValue())));
            }
            collector.ack(input);
        } catch (Throwable t) {
            t.printStackTrace();
            collector.fail(input);
        }
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("link", "date", "description"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    //
    // private
    //
    private Date getSyndDate(SyndEntry syndEntry, SyndFeed feed) {
        return syndEntry.getUpdatedDate() == null ? (syndEntry.getPublishedDate() == null ? feed.getPublishedDate()
                : syndEntry.getPublishedDate()) : syndEntry.getUpdatedDate();
    }

    private String removeHtmlBoilerplate(String htmlStr) {
        Pattern scripts = Pattern.compile(Pattern.quote("<script") + "[ ]*[^>]*>" + ".*?" + Pattern.quote("</script>"),
                Pattern.DOTALL);
        Pattern noScripts = Pattern.compile(
                Pattern.quote("<noscript") + "[ ]*[^>]*>" + ".*?" + Pattern.quote("</noscript>"), Pattern.DOTALL);
        Pattern styles = Pattern.compile(Pattern.quote("<style") + "[ ]*[^>]*>" + ".*?" + Pattern.quote("</style>"),
                Pattern.DOTALL);
        Pattern myRegex = Pattern.compile("\\<.*?>", Pattern.DOTALL);
        Pattern manySpaces = Pattern.compile("\\p{Space}+");

        if (htmlStr == null) {
            return null;
        }
        return manySpaces
                .matcher(
                        myRegex.matcher(
                                styles.matcher(
                                        noScripts.matcher(scripts.matcher(htmlStr).replaceAll(" ")).replaceAll(" "))
                                        .replaceAll(" ")).replaceAll(" ")).replaceAll(" ").trim();
    }

}
