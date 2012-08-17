package vn.ducquoc.storm.bolt;

import java.util.Map;

import vn.ducquoc.jutil.HealthcareUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class NpiValidationBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;

    private OutputCollector outputCollector;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    public void execute(Tuple inputTuple) {
        String npiNumberText = inputTuple.getString(0);
        String result = HealthcareUtil.isValidNpi(npiNumberText) ? "VALID" : "INVALID";
        outputCollector.emit(inputTuple, new Values(npiNumberText + ":" + result)); // anchoring
        outputCollector.ack(inputTuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("npiValidationResult"));
    }

}
