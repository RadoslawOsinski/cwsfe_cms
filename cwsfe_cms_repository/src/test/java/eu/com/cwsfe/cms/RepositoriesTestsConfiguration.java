package eu.com.cwsfe.cms;

import eu.com.cwsfe.cms.db.configuration.RepositoryConfiguration;
import eu.com.cwsfe.cms.db.version.DbMigrationManager;
import eu.com.cwsfe.cms.db.version.DbMigrationManagerMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
@Import({
    RepositoryConfiguration.class,
    DataSourceTestConfiguration.class,
    DbMigrationManager.class,
    HazelcastIntegrationTestInstance.class
})
public class RepositoriesTestsConfiguration {
}
