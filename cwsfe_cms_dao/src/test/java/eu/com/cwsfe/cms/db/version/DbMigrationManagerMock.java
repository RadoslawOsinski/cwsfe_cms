package eu.com.cwsfe.cms.db.version;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by Radoslaw Osinski.
 */
@Component(value = "dbMigrationManager")
@DependsOn("cwsfeCmsDataSource")
public class DbMigrationManagerMock extends DbMigrationManager {

    public DbMigrationManagerMock(DataSource migrationDataSource) {
        super(migrationDataSource);
    }

    @Override
    public void updateCmsDatabaseSchema() {
        //do nothing
    }
}
