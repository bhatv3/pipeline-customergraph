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

import java.util.Iterator;
import java.util.Map;

import static org.neo4j.ogm.session.Utils.map;


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
            createSameAsRelationship(session, customer);
            Utils.writeToLocalLog("CustGraphNeo4JBolt", "Added to graph " + customer.getFullName());
            collector.ack(input);
        } catch (Exception e) {
            e.printStackTrace();
            if (customer != null) {
                collector.emit(Topology.ERROR_STREAM, new Values("error-data", "SYSTEM: Error while adding customer to graph: " + customer.getFullName()));
            }
            collector.fail(input);
        }
    }

    private void createSameAsRelationship(Session session, Customer customer) {
        try {
            Customer incoming = session.queryForObject(Customer.class,
                    "match(c:Customer{fullName:{fullName}})return c limit 1",
                    map("fullName", customer.getFullName()));

            if(null != customer.getPhone()){
                Iterable<Customer> customersSharingPhone = session.query(Customer.class,
                        "match(p{number:{phonenum}})<-[:HAS_PHONE]-(c) return c",
                        map("phonenum", customer.getPhone().getNumber()));
                Iterator<Customer> pIterator = customersSharingPhone.iterator();
                addToSame(pIterator, incoming, incoming.getSame().size()+1);
            }

            if(null != customer.getEmail()){
                Iterable<Customer> customersSharingEmail = session.query(Customer.class,
                        "match(e{id:{emailid}})<-[:HAS_EMAIL]-(c) return c",
                        map("emailid", customer.getEmail().getId()));
                Iterator<Customer> eIterator = customersSharingEmail.iterator();
                addToSame(eIterator, incoming, incoming.getSame().size()+1);
            }
            

            session.save(incoming);
            Utils.writeToLocalLog("CustGraphNeo4JBolt", "Completed SAME_AS relationship creation  " + customer.getFullName());
        } catch (Exception e) {
            e.printStackTrace();
            collector.emit(Topology.ERROR_STREAM, new Values("error-data", "SYSTEM: Error while creating SAME_AS relationships: " + customer.getFullName()));

        }
    }

    private void addToSame(Iterator<Customer> i, Customer incoming, int n) {
        while (i.hasNext()) {
            Customer next = i.next();
            if(next.getId()!=incoming.getId()){
                incoming.addToSame(next);
                Utils.writeToLocalLog("CustGraphNeo4JBolt", "Created " + incoming.getFullName() + "--SAME_AS--" + next.getFullName());
                if (incoming.getSame().size() == n) break;
            }
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
