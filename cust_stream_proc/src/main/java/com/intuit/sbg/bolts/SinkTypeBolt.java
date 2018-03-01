package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */


import com.intuit.sbg.Topology;
import com.intuit.sbg.Utils;
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

        String[] split = value.split(",");
        try {
            if (split.length == 5) {
                collector.emit(Topology.DATANORMALIZE_STREAM, new Values("cust-data", value));
            } else {
                collector.emit(Topology.ERROR_STREAM, new Values("error-data", "DATA: " + value));
            }
            collector.ack(tuple);
            Utils.writeToLocalLog("SinkTypeBolt", "Emitted -> " + value);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.writeToLocalLog("SinkTypeBolt", "Exception -> " + e.getMessage() + ", " + value);
            collector.fail(tuple);
        }


    }


    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Topology.DATANORMALIZE_STREAM, new Fields("sinkType", "content"));
        declarer.declareStream(Topology.ERROR_STREAM, new Fields("sinkType", "content"));

    }

}