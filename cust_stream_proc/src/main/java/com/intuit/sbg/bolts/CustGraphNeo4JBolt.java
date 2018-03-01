package com.intuit.sbg.bolts;

/**
 * Created by vikasbhat on 2/25/18.
 */

import com.intuit.sbg.Topology;
import com.intuit.sbg.Utils;
import com.intuit.sbg.domain.Address;
import com.intuit.sbg.domain.Customer;
import com.intuit.sbg.domain.Email;
import com.intuit.sbg.domain.Phone;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Map;


public class CustGraphNeo4JBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    public String boltUrl;
    SessionFactory sessionFactory;
    private OutputCollector collector;

    protected CustGraphNeo4JBolt(String boltUrl) {
        this.boltUrl = boltUrl;

    }


    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Configuration configuration = new Configuration.Builder()
                .uri(boltUrl)
                .connectionPoolSize(150)
                .build();
        sessionFactory = new SessionFactory(configuration, "com.intuit.sbg.domain");

    }


    public void execute(Tuple input) {

        Customer customer = null;
        try {
            Session session = sessionFactory.openSession();

            customer = buildCustomer(input);
            session.save(customer);

            Utils.writeToLocalLog("CustGraphNeo4JBolt", "Added to graph -> " + customer.getFullName());
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            if (customer != null) {
                collector.emit(Topology.ERROR_STREAM, new Values("error-data", "SYSTEM: Error while adding customer to graph: " + customer.getFullName()));
            }
            collector.fail(input);
        }
    }

    private Customer buildCustomer(Tuple custData) {


        String nameStr = (String) custData.getValueByField("name");
        String addressStr = (String) custData.getValueByField("address");
        String zipCodeStr = (String) custData.getValueByField("zipcode");
        String phoneStr = (String) custData.getValueByField("phone");
        String emailStr = (String) custData.getValueByField("email");

        Address address = new Address();
        Phone phone = null;
        Email email = null;

        if (!"".equals(emailStr)) {
            email = new Email(emailStr);
        }

        if (!"".equals(phoneStr)) {
            phone = new Phone(phoneStr);
        }

        if (!"".equals(zipCodeStr) || !"".equals(addressStr)) {
            address = new Address(addressStr, zipCodeStr);
        }

        return new Customer(nameStr, address, phone, email);

    }


    @Override
    public void cleanup() {
        sessionFactory.close();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(Topology.ERROR_STREAM, new Fields("sinkType", "content"));
    }

}
