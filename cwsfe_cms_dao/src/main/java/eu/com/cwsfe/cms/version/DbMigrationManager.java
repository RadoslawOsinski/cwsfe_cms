package eu.com.cwsfe.cms.version;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Automatically updates database
 * <p>
 * Created by Radosław Osiński
 */
@Component
public class DbMigrationManager {

    private final DataSource dataSource;

    public DbMigrationManager(DataSource migrationDataSource) {
        this.dataSource = migrationDataSource;
    }

    @PostConstruct
    public void updateCmsDatabaseSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
        flyway.setBaselineOnMigrate(true);
        flyway.repair();
    }

}
