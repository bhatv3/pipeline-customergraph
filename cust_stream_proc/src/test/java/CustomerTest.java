import com.intuit.sbg.domain.Address;
import com.intuit.sbg.domain.Customer;
import com.intuit.sbg.domain.Email;
import com.intuit.sbg.domain.Phone;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

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
    public void testadd() throws Exception {

        Session session = sessionFactory.openSession();

        Address a = new Address("14A St", "12145");
        Phone p = new Phone("(111) 222-3333");
        Email email = new Email("a4@a.com");
        Customer c = new Customer("Jack2", a, p, email);
        try {

            session.save(c);
        } catch (Exception e) {

            session.delete(c);
        }
    }

}
