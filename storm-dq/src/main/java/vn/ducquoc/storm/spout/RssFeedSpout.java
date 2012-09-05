package vn.ducquoc.storm.spout;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * @see backtype.storm.spout.ISpout
 */
public class RssFeedSpout implements IRichSpout {

    private static final long serialVersionUID = 1L;

    SpoutOutputCollector spoutOutputCollector;
    String[] feedUrls;
    Queue<String> feedQueue = new LinkedList<String>();

    public RssFeedSpout(String[] feeds) {
        this.feedUrls = feeds;
    }

    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.spoutOutputCollector = collector;
        for (String feed : feedUrls) {
            feedQueue.add(feed);
        }
    }

    public void nextTuple() {
        // backtype.storm.utils.Utils.sleep(500);
        String nextFeed = feedQueue.poll();
        if (nextFeed != null) {
            this.spoutOutputCollector.emit(new Values(nextFeed), nextFeed);
        }
    }

    public void close() {
    }

    public void ack(Object feedId) {
        feedQueue.add((String) feedId);
    }

    public void fail(Object feedId) {
        feedQueue.add((String) feedId);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("feed"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    public void activate() {
    }

    public void deactivate() {
    }

}
