package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */


import com.intuit.sbg.Topology;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;


public class SinkTypeBolt extends BaseRichBolt {


    private static final long serialVersionUID = 1L;
    private OutputCollector collector;


    public void execute(Tuple tuple) {
        String value = tuple.getString(0);
        System.out.println("Received in SinkType bolt : " + value);
        String[] split = value.split(",");

        if (split.length == 5) {
            collector.emit(Topology.CUST_GRAPH_STREAM, new Values("cust-data", value));
            System.out.println("Emitted : " + value);
        } else {
            collector.emit(Topology.OTHER_DATA_STREAM, new Values("other-data", value));
            System.out.println("Emitted : " + value);
        }
        collector.ack(tuple);
    }


    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Topology.CUST_GRAPH_STREAM, new Fields("sinkType", "content"));
        declarer.declareStream(Topology.OTHER_DATA_STREAM, new Fields("sinkType", "content"));
    }

}