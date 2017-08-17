module eu.com.cwsfe.cms.db {
    exports eu.com.cwsfe.cms.db.author;
    exports eu.com.cwsfe.cms.db.common;
    exports eu.com.cwsfe.cms.db.configuration;
    exports eu.com.cwsfe.cms.db.i18n;
    exports eu.com.cwsfe.cms.db.keystores;
    exports eu.com.cwsfe.cms.db.news;
    exports eu.com.cwsfe.cms.db.parameters;
    exports eu.com.cwsfe.cms.db.users;
    exports eu.com.cwsfe.cms.db.version;

    requires java.naming;
    requires java.sql;

    requires hibernate.core;
    requires hibernate.jpa;
    requires spring.beans;
    requires spring.core;
    requires spring.context;
    requires spring.jdbc;
    requires spring.orm;
    requires spring.web;
    requires spring.tx;
    requires hazelcast;
    requires flyway.core;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires HikariCP;
}
