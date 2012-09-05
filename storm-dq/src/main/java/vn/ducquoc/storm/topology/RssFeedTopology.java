package vn.ducquoc.storm.topology;

import vn.ducquoc.storm.bolt.FeedFetcherBolt;
import vn.ducquoc.storm.bolt.FeedListingBolt;
import vn.ducquoc.storm.spout.RssFeedSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * @see backtype.storm.topology.TopologyBuilder
 */
public class RssFeedTopology {

    static String[] FEEDS = { "http://www.dzone.com/links/feed/frontpage/rss.xml",
            "http://rss.slashdot.org/Slashdot/slashdotLinux", "http://www.quantrimang.com.vn/hedieuhanh/linux/rss.aspx" };

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("feedSpout", new RssFeedSpout(FEEDS), 1);

        builder.setBolt("fetcherBolt", new FeedFetcherBolt(), 2).shuffleGrouping("feedSpout");
        builder.setBolt("listingBolt", new FeedListingBolt(), 1).globalGrouping("fetcherBolt");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("testFeedTopology", conf, builder.createTopology());

            Utils.sleep(10000);
            cluster.killTopology("testFeedTopology");
            cluster.shutdown();
        }
    }

}
