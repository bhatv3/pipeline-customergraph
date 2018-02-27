package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;


public class OtherDataBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    private OutputCollector collector;

    protected OtherDataBolt() {

    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    public void execute(Tuple input) {

        try {
            String content = (String) input.getValueByField("content");
            System.out.printf("OtherDataBolt -> Non-Customer Data Received: " + content);
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            collector.fail(input);
        }
    }


    @Override
    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
    }


}
