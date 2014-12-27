package eu.com.cwsfe.cms.version;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * Automatically updates database
 * <p>
 * Created by Radosław Osiński
 */
public class DbMigrationManager {

    private final DataSource dataSource;

    public DbMigrationManager(DataSource migrationDataSource) {
        this.dataSource = migrationDataSource;
    }

    public void updateCmsDatabaseSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

}
