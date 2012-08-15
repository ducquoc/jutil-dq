package vn.ducquoc.storm.topology;

import vn.ducquoc.storm.bolt.PrinterBolt;
import vn.ducquoc.storm.spout.RandomSentenceSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class PrinterTopology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("random-sentence-spout", new RandomSentenceSpout(), 7);
        builder.setBolt("print-bolt", new PrinterBolt(), 5).shuffleGrouping("random-sentence-spout");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test-print-bolt", conf, builder.createTopology());
            Utils.sleep(10000);

            cluster.shutdown();
        }
    }

}
