package vn.ducquoc.storm.bolt;

import java.util.Map;

import vn.ducquoc.jutil.HealthcareUtil;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @author ducquoc
 * @see backtype.storm.task.IBolt
 */
public class NpiValidationBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;

    private OutputCollector outputCollector;

    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    public void execute(Tuple inputTuple) {
        String npiNumberText = inputTuple.getString(0);
        String result = HealthcareUtil.isValidNpi(npiNumberText) ? "VALID" : "INVALID";
        outputCollector.emit(inputTuple, new Values(npiNumberText, result)); // anchoring
        System.out.println(npiNumberText + ":" + result);
        outputCollector.ack(inputTuple);
    }

    public void cleanup() {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("originalNpi", "validationResult"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
