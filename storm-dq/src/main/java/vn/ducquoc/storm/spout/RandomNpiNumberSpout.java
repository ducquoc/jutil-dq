package vn.ducquoc.storm.spout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * @author ducquoc
 * @see backtype.storm.spout.ISpout
 */
public class RandomNpiNumberSpout implements IRichSpout {

    private static final long serialVersionUID = 1L;

    boolean isDistributed;
    SpoutOutputCollector spoutOutputCollector;

    public RandomNpiNumberSpout() {
        this(true);
    }

    public RandomNpiNumberSpout(boolean isDistributed) {
        this.isDistributed = isDistributed;
    }

    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.spoutOutputCollector = collector;
    }

    public void nextTuple() {
        Utils.sleep(1000);
        String[] npis = new String[] { "1234567893", "1901050181", "2203205598", "2901419657", "1808871341",
                "220374080", "1234567897" };
        Random rand = new Random();
        String word = npis[rand.nextInt(npis.length)];
        spoutOutputCollector.emit(new Values(word));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("npi"));
    }

    // @Override
    public Map<String, Object> getComponentConfiguration() {
        if (!isDistributed) {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
            return ret;
        }
        return null;
    }

    public boolean isDistributed() {
        return true;
    }

    public void close() {
    }

    public void ack(Object msgId) {
    }

    public void fail(Object msgId) {
    }

    public void activate() {
    }

    public void deactivate() {
    }

}
