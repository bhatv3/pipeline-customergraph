package com.intuit.sbg;

/**
 * Created by vikasbhat on 2/25/18.
 */
public class Keys {


    public static final String TOPOLOGY_NAME = "topology";

    //kafka spout
    public static final String KAFKA_SPOUT_ID = "kafka-spout";
    public static final String KAFKA_ZOOKEEPER = "kafka.zookeeper";
    public static final String KAFKA_TOPIC = "kafka.topic";
    public static final String KAFKA_ZKROOT = "kafka.zkRoot";
    public static final String KAFKA_CONSUMERGROUP = "kafka.consumer.group";
    public static final String KAFKA_SPOUT_COUNT = "kafkaspout.count";

    //sink bolt
    public static final String SINK_TYPE_BOLT_ID = "sink-type-bolt";
    public static final String SINK_BOLT_COUNT = "sinkbolt.count";


    //Customer Graph bolt
    public static final String CUSTGRAPHNEO4J_BOLT_ID = "custgraphneo4j.bolt.id";
    public static final String CUSTGRAPHNEO4J_BOLT_COUNT = "custgraphneo4j.bolt.count";
    public static final String CUSTGRAPHNEO4J_BOLT_URL = "custgraphneo4j.url";

    //Error bolt
    public static final String ERROR_BOLT_ID = "error.bolt.id";
    public static final String ERROR_BOLT_COUNT = "error.bolt.count";

    //Data Normalization bolt
    public static final String DATANORMALIZE_BOLT_ID = "datanormalize.bolt.id";
    public static final String DATANORMALIZE_BOLT_COUNT = "datanormalize.bolt.count";



}