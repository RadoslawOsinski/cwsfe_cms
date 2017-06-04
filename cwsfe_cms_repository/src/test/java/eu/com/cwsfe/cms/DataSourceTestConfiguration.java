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
public class DataSourceTestConfiguration {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "cwsfeCmsDataSource")
    public DataSource getTestCwsfeCmsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("cwsfe_cms.jdbc.driverClassName", "org.postgresql.Driver"));
        dataSource.setUrl(environment.getProperty("cwsfe_cms.jdbc.url", "jdbc:postgresql://localhost:55432/postgres"));
        dataSource.setUsername(environment.getProperty("cwsfe_cms.jdbc.user", "postgres"));
        dataSource.setPassword(environment.getProperty("cwsfe_cms.jdbc.password", "postgres"));
        dataSource.setSchema(environment.getProperty("cwsfe_cms.jdbc.schema", "postgres"));
        return dataSource;
    }

}
