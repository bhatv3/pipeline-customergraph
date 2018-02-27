package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;


public class CustDataBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    public String apiUrl;
    private OutputCollector collector;

    protected CustDataBolt(String apiUrl) {
        this.apiUrl = apiUrl;

    }


    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }


    public void execute(Tuple input) {

        try {
            String content = (String) input.getValueByField("content");
            System.out.printf("CustDataBolt -> Customer Data Received: " + content);
            Files.write(Paths.get("/tmp/custdatabolt"), Arrays.asList(content), UTF_8, APPEND, CREATE);
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
