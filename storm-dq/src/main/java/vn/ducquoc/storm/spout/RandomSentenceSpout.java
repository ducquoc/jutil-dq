package vn.ducquoc.storm.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class RandomSentenceSpout extends BaseRichSpout {

    private static final long serialVersionUID = 1L;

    SpoutOutputCollector spoutOutputCollector;
    Random randomNumberGenerator;

    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        spoutOutputCollector = collector;
        randomNumberGenerator = new Random();
    }

    public void nextTuple() {
        Utils.sleep(100);
        String[] sentences = new String[] { "The quick brown fox jumped over the lazy dog", "An apple a day keeps the doctor away",
                "Beauty and the beast", "Snow white and the seven dwarfs", "There is no gain without pain" };
        String sentence = sentences[randomNumberGenerator.nextInt(sentences.length)];
        spoutOutputCollector.emit(new Values(sentence));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

}
