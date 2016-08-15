package eu.com.cwsfe.cms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
public class DataSourceConfiguration {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * JNDI name in wildfly is different than tomcat. Java standards sucks.
     * @return data source
     */
    @Profile("wildfly")
    @Bean
    public DataSource getWildflyDataSource() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/cwsfe_cms");
        return (DataSource) jndiObjectFactoryBean.getObject();
    }

    /**
     * JNDI name in wildfly is different than tomcat. Java standards sucks.
     * @return data source
     */
    @Profile("tomcat")
    @Bean
    public DataSource getTomcatDataSource() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("java:comp/env/jdbc/cwsfe_cms");
        return (DataSource) jndiObjectFactoryBean.getObject();
    }

    @Profile("tomcat-aws")
    @Bean
    public PropertyPlaceholderConfigurer getAwsPropertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setSearchSystemEnvironment(true);
        return propertyPlaceholderConfigurer;
    }

    @Profile("tomcat-aws")
    @Bean
    public DataSource getTomcatAwsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
        dataSource.setUrl(environment.getProperty("dataSource.url"));
        dataSource.setUsername(environment.getProperty("dataSource.username"));
        dataSource.setPassword(environment.getProperty("dataSource.password"));
        return dataSource;
    }

}
