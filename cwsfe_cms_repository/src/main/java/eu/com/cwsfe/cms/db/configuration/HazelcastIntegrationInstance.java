package eu.com.cwsfe.cms.db.configuration;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
public class HazelcastIntegrationInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastIntegrationInstance.class);

    /**
     * Gets new hazelcast instance.
     *
     * @return the new hazelcast instance
     */
    @Bean(name = "hazelcastInstance")
    public HazelcastInstance getHazelcastInstance() {
        LOGGER.info("Registering hazelcast instance in spring context as bean.");
        return Hazelcast.newHazelcastInstance();
    }
}
