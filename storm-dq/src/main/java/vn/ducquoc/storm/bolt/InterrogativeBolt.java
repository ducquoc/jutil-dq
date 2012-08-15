package vn.ducquoc.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class InterrogativeBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;

    private OutputCollector outputCollector;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    public void execute(Tuple inputTuple) {
        // System.out.println(inputTuple.getStringByField("word") + "?");
        outputCollector.emit(inputTuple, new Values(inputTuple.getString(0) + "?")); // anchoring
        outputCollector.ack(inputTuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
