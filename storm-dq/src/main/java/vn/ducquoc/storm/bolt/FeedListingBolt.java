package vn.ducquoc.storm.bolt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * @see backtype.storm.task.IBolt
 * @see org.rometools.fetcher.impl.HttpURLFeedFetcher
 */
@SuppressWarnings("rawtypes")
public class FeedListingBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;

    private static final int LISTING_SIZE = 10;

    OutputCollector collector;

    List<Tuple> listings = new ArrayList<Tuple>();
    Set<String> listingIds = new HashSet<String>();

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Thread reporter = new Thread() {
            public void run() {
                while (true) {
                    System.out.println("Feed Listing start.");
                    synchronized (listings) {
                        for (Tuple tuple : listings) {
                            FeedListingBolt.this.collector.emit(new Values(tuple.getStringByField("link"), tuple
                                    .getLongByField("date"), tuple.getStringByField("description")));
                            System.out.println("Listing URL: " + tuple.getStringByField("link") + " listing date: ["
                                    + new Date(tuple.getLongByField("date")) + "]");
                        }
                    }
                    System.out.println("Feed Listing finished.");
                    Utils.sleep(1000);
                }
            };
        };
        reporter.start();

        // stop for local mode test
        Utils.sleep(1000);
        reporter.stop();
    }

    public void execute(Tuple input) {
        String listingId = input.getStringByField("link");
        synchronized (listings) {
            if (!listingIds.contains(listingId)) {
                listings.add(input);
                listingIds.add(listingId);
                if (listings.size() > LISTING_SIZE) {
                    Collections.sort(listings, new Comparator<Tuple>() {
                        public int compare(Tuple t1, Tuple t2) {
                            return t1.getLongByField("date").compareTo(t2.getLongByField("date"));
                        }
                    });
                    // Remove oldest one
                    listings.remove(0);
                }
            }
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

}
