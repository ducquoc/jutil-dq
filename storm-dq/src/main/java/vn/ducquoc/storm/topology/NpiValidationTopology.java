package vn.ducquoc.storm.topology;

import vn.ducquoc.storm.bolt.NpiValidationBolt;
import vn.ducquoc.storm.spout.RandomNpiNumberSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * @author ducquoc
 * @see backtype.storm.topology.TopologyBuilder
 */
public class NpiValidationTopology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("random-npi-spout", new RandomNpiNumberSpout(true), 2);
        builder.setBolt("npi-validation-bolt", new NpiValidationBolt(), 3).shuffleGrouping("random-npi-spout");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topologyTest", conf, builder.createTopology());

            Utils.sleep(10000);
            // cluster.killTopology("topologyTest");
            cluster.shutdown();
        }
    }

}
