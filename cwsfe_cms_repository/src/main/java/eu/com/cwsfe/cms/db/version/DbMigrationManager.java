package eu.com.cwsfe.cms.db.version;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Automatically updates database
 * <p>
 * Created by Radosław Osiński
 */
@Component
public class DbMigrationManager implements InitializingBean {

    private final DataSource cwsfeCmsDataSource;

    public DbMigrationManager(DataSource cwsfeCmsDataSource) {
        this.cwsfeCmsDataSource = cwsfeCmsDataSource;
    }

    public void updateCmsDatabaseSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(cwsfeCmsDataSource);
        flyway.migrate();
        flyway.setBaselineOnMigrate(true);
        flyway.repair();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateCmsDatabaseSchema();
    }
}
