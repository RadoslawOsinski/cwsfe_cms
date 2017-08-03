package eu.com.cwsfe.cms.app.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Radosław Osiński
 */
@Configuration
@ComponentScan(value = {"eu.com.cwsfe.cms.db", "eu.com.cwsfe.cms.services"})
public class BeforeSecurityConfiguration {
}
