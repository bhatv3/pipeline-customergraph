package com.intuit.sbg;

/**
 * Created by vikasbhat on 2/25/18.
 */

import com.intuit.sbg.bolts.*;
import com.intuit.sbg.spouts.SpoutBuilder;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;

/**
 * @author Vikas Bhat
 *         This is the main topology class.
 *         All the spouts and bolts are linked together and is submitted on to the cluster
 */
public class Topology {

    public static final String ERROR_STREAM = "error-stream";
    public static final String CUST_GRAPH_STREAM = "custgraph-stream";
    public static final String DATANORMALIZE_STREAM = "datanormalize-stream";

    public Properties configs;
    public BoltBuilder boltBuilder;
    public SpoutBuilder spoutBuilder;


    public Topology(String configFile) throws Exception {
        configs = new Properties();
        try {
            configs.load(Topology.class.getResourceAsStream("/com/intuit/sbg/default_config.properties"));
            boltBuilder = new BoltBuilder(configs);
            spoutBuilder = new SpoutBuilder(configs);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        String configFile;
        if (args.length == 0) {
            System.out.println("Missing input : config file location, using default");
            configFile = "com/intuit/sbg/default_config.properties";

        } else {
            configFile = args[0];
        }

        Topology ingestionTopology = new Topology(configFile);
        ingestionTopology.submitTopology();
    }

    private void submitTopology() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        KafkaSpout kafkaSpout = spoutBuilder.buildKafkaSpout();
        SinkTypeBolt sinkTypeBolt = boltBuilder.buildSinkTypeBolt();

        CustGraphNeo4JBolt custGraphNeo4JBolt = boltBuilder.buildCustDataBolt();
        ErrorBolt errorBolt = boltBuilder.buildErrorDataBolt();
        DataNormalizeBolt dataNormalizeBolt = boltBuilder.buildDataNormalizeBolt();

        //set the kafkaSpout to topology
        //parallelism-hint for kafkaSpout - defines number of executors/threads to be spawn per container
        int kafkaSpoutCount = Integer.parseInt(configs.getProperty(Keys.KAFKA_SPOUT_COUNT));
        builder.setSpout(configs.getProperty(Keys.KAFKA_SPOUT_ID), kafkaSpout, kafkaSpoutCount);


        //set the sinktype bolt
        int sinkBoltCount = Integer.parseInt(configs.getProperty(Keys.SINK_BOLT_COUNT));
        builder.setBolt(configs.getProperty(Keys.SINK_TYPE_BOLT_ID), sinkTypeBolt, sinkBoltCount).
                shuffleGrouping(configs.getProperty(Keys.KAFKA_SPOUT_ID));

        //set the custdata bolt
        int dataNormalizeBoltCount = Integer.parseInt(configs.getProperty(Keys.DATANORMALIZE_BOLT_COUNT));
        builder.setBolt(configs.getProperty(Keys.DATANORMALIZE_BOLT_ID), dataNormalizeBolt, dataNormalizeBoltCount).
                shuffleGrouping(configs.getProperty(Keys.SINK_TYPE_BOLT_ID), DATANORMALIZE_STREAM);

        //set the custdata bolt
        int custdataBoltCount = Integer.parseInt(configs.getProperty(Keys.CUSTGRAPHNEO4J_BOLT_COUNT));
        builder.setBolt(configs.getProperty(Keys.CUSTGRAPHNEO4J_BOLT_ID), custGraphNeo4JBolt, custdataBoltCount).
                shuffleGrouping(configs.getProperty(Keys.DATANORMALIZE_BOLT_ID), CUST_GRAPH_STREAM);

        //set the error bolt
        int errorBoltCount = Integer.parseInt(configs.getProperty(Keys.ERROR_BOLT_COUNT));
        builder.setBolt(configs.getProperty(Keys.ERROR_BOLT_ID), errorBolt, errorBoltCount).
                shuffleGrouping(configs.getProperty(Keys.SINK_TYPE_BOLT_ID), ERROR_STREAM).
                shuffleGrouping(configs.getProperty(Keys.CUSTGRAPHNEO4J_BOLT_ID), ERROR_STREAM);


        Config conf = new Config();
        String topologyName = configs.getProperty(Keys.TOPOLOGY_NAME);
        //Defines how many worker processes have to be created for the topology in the cluster.
        conf.setNumWorkers(1);
        StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
    }
}