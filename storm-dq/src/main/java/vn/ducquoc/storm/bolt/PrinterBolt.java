package vn.ducquoc.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    }

    public void execute(Tuple inputTuple) {
        System.out.println(inputTuple);
        // System.out.println(inputTuple.getValue(0));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
