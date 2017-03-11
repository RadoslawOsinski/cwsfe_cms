package eu.com.cwsfe.cms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        LOGGER.info("CWSFE CMS spring active profiles: {}", Arrays.toString(environment.getActiveProfiles()));
    }

    /**
     * JNDI name in wildfly is different than tomcat. Java standards sucks.
     *
     * @return data source
     */
    @Profile("wildfly")
    @Bean(name = "dataSource", value = "dataSource")
    public DataSource getWildflyDataSource() {
        LOGGER.info("Attaching to wildfly data source");
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        String jndiName = "jdbc/cwsfe_cms";
        jndiObjectFactoryBean.setJndiName(jndiName);
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) jndiObjectFactoryBean.getJndiTemplate().getContext().lookup(jndiName);
        } catch (NamingException e) {
            LOGGER.error("Data source problem with with jndiName: {}", jndiName, e);
        }
        LOGGER.info("Wildfly data source attachment success status: {}", dataSource != null);
        return dataSource;
    }

    /**
     * JNDI name in wildfly is different than tomcat. Java standards sucks.
     *
     * @return data source
     */
    @Profile("tomcat")
    @Bean(name = "dataSource", value = "dataSource")
    public DataSource getTomcatDataSource() {
        LOGGER.info("Attaching to tomcat jndi data source");
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("java:comp/env/jdbc/cwsfe_cms");
        DataSource dataSource = (DataSource) jndiObjectFactoryBean.getObject();
        LOGGER.info("Tomcat data source attachment success status: {}", dataSource != null);
        return dataSource;
    }

    @Profile("tomcat-jndi")
    @Bean(name = "dataSource", value = "dataSource")
    public DataSource getTomcatJndiDataSource() {
        LOGGER.info("Attaching to tomcat jndi data source");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
        dataSource.setUrl(environment.getProperty("dataSource.url"));
        dataSource.setUsername(environment.getProperty("dataSource.username"));
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        return dataSource;
    }

    @Profile("tomcat-aws")
    public PropertyPlaceholderConfigurer getAwsPropertyPlaceholderConfigurer() {
        LOGGER.info("Turning on aws environment property placeholders");
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setSearchSystemEnvironment(true);
        return propertyPlaceholderConfigurer;
    }

    @Profile("tomcat-aws")
    @Bean(name = "dataSource", value = "dataSource")
    public DataSource getTomcatAwsDataSource() {
        LOGGER.info("Attaching to tomcat aws data source");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
        dataSource.setUrl(environment.getProperty("dataSource.url"));
        dataSource.setUsername(environment.getProperty("dataSource.username"));
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        return dataSource;
    }

}
