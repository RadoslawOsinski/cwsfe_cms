package eu.com.cwsfe.cms;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
public class HazelcastIntegrationTestInstance {

    @Bean("hazelcastInstance")
    public HazelcastInstance getHazelcastInstance() {
        Config config = new Config();
        config.setInstanceName("cwsfeCmsHazelcastInstance1");
        return Hazelcast.newHazelcastInstance(config);
    }
}
