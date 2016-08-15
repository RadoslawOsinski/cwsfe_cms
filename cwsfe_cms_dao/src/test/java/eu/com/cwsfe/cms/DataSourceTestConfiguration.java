package eu.com.cwsfe.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
@ComponentScan({"eu.com.cwsfe.cms.version"})
@PropertySource(value = "classpath:jdbc_cwsfe_cms_test.properties")
public class DataSourceTestConfiguration {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getTestDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.userName"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public org.springframework.jdbc.core.JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getTestDataSource());
    }

    @Bean
    public org.springframework.jdbc.datasource.DataSourceTransactionManager getDataSourceTransactionManager() {
        return new DataSourceTransactionManager(getTestDataSource());
    }

}
