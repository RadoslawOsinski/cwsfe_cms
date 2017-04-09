package eu.com.cwsfe.cms.db.version;

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

    private final DataSource cwsfeCmsDataSource;

    public DbMigrationManager(DataSource cwsfeCmsDataSource) {
        this.cwsfeCmsDataSource = cwsfeCmsDataSource;
    }

    @PostConstruct
    public void updateCmsDatabaseSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(cwsfeCmsDataSource);
        flyway.migrate();
        flyway.setBaselineOnMigrate(true);
        flyway.repair();
    }

}
