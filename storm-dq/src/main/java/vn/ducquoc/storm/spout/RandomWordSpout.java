package vn.ducquoc.storm.spout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class RandomWordSpout extends BaseRichSpout {

    private static final long serialVersionUID = 1L;

    boolean isDistributed;
    SpoutOutputCollector spoutOutputCollector;

    public RandomWordSpout() {
        this(true);
    }

    public RandomWordSpout(boolean isDistributed) {
        this.isDistributed = isDistributed;
    }

    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.spoutOutputCollector = collector;
    }

    public void nextTuple() {
        Utils.sleep(100);
        String[] words = new String[] { "ANT", "BIRD", "CAT", "DOG", "ELEPHANT", "FOX", "GIRAFFE" };
        Random rand = new Random();
        String word = words[rand.nextInt(words.length)];
        spoutOutputCollector.emit(new Values(word));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
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

}
