import com.intuit.sbg.domain.Address;
import com.intuit.sbg.domain.Customer;
import com.intuit.sbg.domain.Email;
import com.intuit.sbg.domain.Phone;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Iterator;

import static org.neo4j.ogm.session.Utils.map;

/**
 * Created by vikasbhat on 2/28/18.
 */
public class CustomerTest {


    SessionFactory sessionFactory;


    @Before
    public void setUp() throws Exception {
        Configuration configuration = new Configuration.Builder()
                .uri("bolt://admin:admin@localhost")
                .connectionPoolSize(150)
                .build();
        sessionFactory = new SessionFactory(configuration, "com.intuit.sbg.domain");

    }

    @Test
    public void testaddCustomer() throws Exception {

        Customer c = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();

            Address a = new Address("14A St", "13145");
            Phone p = new Phone("303 111 2121");
            Email email = new Email("J@galt.ca");
            c = new Customer("Jack3", a, p, email);
            session.save(c);
            // createSameAsRelationship(session,c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.delete(c);

        }
    }

    private void createSameAsRelationship(Session session, Customer customer) {
        try {
            Customer incoming = session.queryForObject(Customer.class,
                    "match(c:Customer{fullName:{fullName}})return c limit 1",
                    map("fullName", customer.getFullName()));

            Iterable<Customer> customersSharingPhone = session.query(Customer.class,
                    "match(p{number:{phonenum}})<-[:HAS_PHONE]-(c) return c",
                    map("phonenum", customer.getPhone().getNumber()));

            Iterable<Customer> customersSharingEmail = session.query(Customer.class,
                    "match(e{id:{emailid}})<-[:HAS_EMAIL]-(c) return c",
                    map("emailid", customer.getEmail().getId()));

            Iterator<Customer> pIterator = customersSharingPhone.iterator();
            Iterator<Customer> eIterator = customersSharingEmail.iterator();
            addToSame(pIterator, incoming, 1);
            addToSame(eIterator, incoming, 2);
            session.save(incoming);

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    private void addToSame(Iterator<Customer> i, Customer incoming, int n) {
        while (i.hasNext()) {
            Customer next = i.next();
            incoming.addToSame(next);

            if (incoming.getSame().size() == n) break;
        }
    }

}
