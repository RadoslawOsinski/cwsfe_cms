package eu.com.cwsfe.cms.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.IfProfileValue;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Radoslaw Osinski
 */
@IfProfileValue(name = "test-groups", values = {"integration_tests_local"})
@Configuration
public class IntegrationTestsDataSource {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getIntegrationTestsLocalhostDataSource() throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("cwsfe_cms.jdbc.driverClassName", "org.postgresql.Driver"));
        dataSource.setUrl(environment.getProperty("cwsfe_cms.jdbc.url"));
        dataSource.setUsername(environment.getProperty("cwsfe_cms.jdbc.user"));
        dataSource.setPassword(environment.getProperty("cwsfe_cms.jdbc.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() throws IOException {
        return new JdbcTemplate(getIntegrationTestsLocalhostDataSource());
    }
}
