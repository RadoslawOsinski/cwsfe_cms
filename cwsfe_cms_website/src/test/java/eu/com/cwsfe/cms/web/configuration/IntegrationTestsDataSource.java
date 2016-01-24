package eu.com.cwsfe.cms.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.IfProfileValue;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Radoslaw Osinski
 */
@IfProfileValue(name = "test-groups", values = {"integration_tests_local"})
@Configuration
public class IntegrationTestsDataSource {


    @Bean
    public Properties getIntegrationTestsLocalhostProperties() throws IOException {
        Resource resource = new ClassPathResource("jdbc_cwsfe_cms_test.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    @Bean
    public DataSource getIntegrationTestsLocalhostDataSource() throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties properties = getIntegrationTestsLocalhostProperties();
        dataSource.setDriverClassName(properties.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(properties.getProperty("jdbc.url"));
        dataSource.setUsername(properties.getProperty("jdbc.userName"));
        dataSource.setPassword(properties.getProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() throws IOException {
        return new JdbcTemplate(getIntegrationTestsLocalhostDataSource());
    }
}
