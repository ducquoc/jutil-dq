package vn.ducquoc.storm.topology;

import vn.ducquoc.storm.bolt.NpiValidationBolt;
import vn.ducquoc.storm.spout.RandomNpiNumberSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class NpiValidationTopology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("first-spout", new RandomNpiNumberSpout(), 7);
        builder.setBolt("first-bolt", new NpiValidationBolt(), 5).shuffleGrouping("first-spout");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topology-dq", conf, builder.createTopology());

            Utils.sleep(10000);
            // cluster.killTopology("topology-dq");
            cluster.shutdown();
        }
    }

}
