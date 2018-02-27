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
    public static final String CUSTDATA_BOLT_ID = "custdata.bolt.id";
    public static final String CUSTDATA_BOLT_COUNT = "custdata.bolt.count";
    public static final String CUST_GRAPH_API_URL = "some url";

    //Other Data bolt
    public static final String OTHER_DATA_BOLT_ID = "otherdata.bolt.id";
    public static final String OTHERDATA_BOLT_COUNT = "otherdata.bolt.count";


}