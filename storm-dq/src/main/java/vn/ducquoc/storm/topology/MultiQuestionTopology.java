package vn.ducquoc.storm.topology;

import vn.ducquoc.storm.bolt.InterrogativeBolt;
import vn.ducquoc.storm.spout.RandomWordSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class MultiQuestionTopology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word", new RandomWordSpout(), 10);
        builder.setBolt("first-question-bolt", new InterrogativeBolt(), 3).shuffleGrouping("word");
        builder.setBolt("second-question-bolt", new InterrogativeBolt(), 2).shuffleGrouping("first-question-bolt");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            // conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test-topology", conf, builder.createTopology());
            Utils.sleep(5000);
            // cluster.killTopology("test-topology");

            cluster.shutdown();
        }
    }

}
