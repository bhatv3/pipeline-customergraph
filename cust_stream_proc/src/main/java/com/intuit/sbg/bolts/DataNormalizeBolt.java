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


public class DataNormalizeBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    private OutputCollector collector;

    protected DataNormalizeBolt() {

    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

    }

    private String normalize(String content) {
        return content;
    }

    public void execute(Tuple input) {

        String content = (String) input.getValueByField("content");
        String[] cust = content.split(",");

        try {
            Values values = new Values("cust-data", cust[0].trim(), normalizeAddress(cust[1].trim()),
                    cust[2].trim(), normalizePhoneNumber(cust[3].trim()), normalizeEmail(cust[4]));
            collector.emit(Topology.CUST_GRAPH_STREAM,
                    values);
            collector.ack(input);
            Utils.writeToLocalLog("DataNormalizeBolt", "Emitted -> " + values.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.writeToLocalLog("DataNormalizeBolt", "Exception -> " + e.getMessage() + ", " + content);
            collector.fail(input);
        }

    }

    private String normalizeAddress(String address) {
        return address;
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.equals("")) return "";

        String digits = phoneNumber.replaceAll("[^0-9]", "");
        if (digits.length() == 10) {
            return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
        }
        return "";
    }

    private String normalizeEmail(String email) {
        if (email == null || email.equals("")) return "";

        if (Utils.validate(email)) {
            return email;
        } else {
            return "";
        }

    }

    @Override
    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Topology.CUST_GRAPH_STREAM, new Fields("sinkType", "name", "address", "zipcode", "phone", "email"));
    }


}
