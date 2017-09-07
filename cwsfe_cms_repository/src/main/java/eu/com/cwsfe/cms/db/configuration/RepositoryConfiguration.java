package eu.com.cwsfe.cms.db.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.SchemaAutoTooling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by Radoslaw Osinski.
 */
@Configuration
@EnableTransactionManagement
public class RepositoryConfiguration {

    private DataSource cwsfeCmsDataSource;
    private Environment environment;

    @Autowired
    public void setCwsfeCmsDataSource(DataSource cwsfeCmsDataSource) {
        this.cwsfeCmsDataSource = cwsfeCmsDataSource;
    }

    /**
     * Sets environment.
     *
     * @param environment the environment
     */
    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Configure properties for JPA instances.
     *
     * @return instance of Properties
     */
    @DependsOn({"hazelcastInstance"})
    @Bean
    public Properties getCwsfeCmsJPAProperties() {
        final Properties properties = new Properties();

        properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto", SchemaAutoTooling.NONE.name().toLowerCase()));
        properties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql", "false"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format_sql", "false"));
        properties.setProperty("hibernate.archive.autodetection", environment.getProperty("hibernate.archive.autodetection", "true"));
        if (environment.getProperty("hibernate.default_schema") != null) {
            properties.setProperty("hibernate.default_schema", environment.getProperty("hibernate.default_schema"));
        }
        properties.setProperty("hibernate.id.new_generator_mappings", Boolean.FALSE.toString());

        properties.setProperty("hibernate.cache.use_second_level_cache", environment.getProperty("hibernate.cache.use_second_level_cache", Boolean.TRUE.toString()));
        properties.setProperty("hibernate.cache.use_query_cache", environment.getProperty("hibernate.cache.use_query_cache", Boolean.TRUE.toString()));

        properties.setProperty("hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastCacheRegionFactory");
        properties.setProperty("hibernate.generate_statistics", environment.getProperty("hibernate.generate_statistics", Boolean.FALSE.toString()));
        properties.setProperty("hibernate.cache.hazelcast.use_lite_member", Boolean.TRUE.toString());
        properties.setProperty("hibernate.cache.use_minimal_puts", Boolean.TRUE.toString());
        properties.setProperty("hibernate.cache.hazelcast.use_native_client", Boolean.FALSE.toString());
        properties.setProperty("hibernate.cache.hazelcast.instance_name", environment.getProperty("cwsfe.eu.com.cwsfe.cms.hazelcast.instanceName", "cwsfeCmsHazelcastInstance1"));
        properties.setProperty("hibernate.cache.hazelcast.native_client_address", environment.getProperty("cwsfe.eu.com.cwsfe.cms.hazelcast.members", ""));
        properties.setProperty("hibernate.cache.hazelcast.native_client_group", environment.getProperty("cwsfe.eu.com.cwsfe.cms.hazelcast.name", ""));
        properties.setProperty("hibernate.cache.hazelcast.native_client_password", environment.getProperty("cwsfe.eu.com.cwsfe.cms.hazelcast.password", "changeMyValueOnProductionEnvironment"));

        return properties;
    }

    /**
     * Gets annotation session factory bean.
     *
     * @return the annotation session factory bean
     */
    @Bean(name = "sessionFactory")
    @DependsOn({"dbMigrationManager", "hazelcastInstance"})
    public LocalSessionFactoryBean getAnnotationSessionFactoryBean() {
        final LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setPackagesToScan("eu.com.cwsfe.cms.db");
        localSessionFactoryBean.setDataSource(cwsfeCmsDataSource);
        localSessionFactoryBean.setHibernateProperties(getCwsfeCmsJPAProperties());
        return localSessionFactoryBean;
    }

    @Bean(name = "transactionManager")
    @DependsOn("sessionFactory")
    public PlatformTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        final HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory);
        return hibernateTransactionManager;
    }

    /**
     * Exporter m bean exporter.
     *
     * @param sessionFactory the session factory
     * @return the m bean exporter
     */
    @Bean
    public MBeanExporter exporter(SessionFactory sessionFactory) {
        MBeanExporter mBeanExporter = new MBeanExporter();
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        mBeanExporter.setBeans(Collections.singletonMap("Hibernate:application=Statistics", sessionFactory.getStatistics()));
        return mBeanExporter;
    }

    /**
     * Mbean server m bean server factory bean.
     *
     * @return the m bean server factory bean
     */
    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mBeanServerFactoryBean = new MBeanServerFactoryBean();
        mBeanServerFactoryBean.setLocateExistingServerIfPossible(true);
        return mBeanServerFactoryBean;
    }

}
