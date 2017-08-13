module eu.com.cwsfe.cms.services {
    exports eu.com.cwsfe.cms.services.author;
    exports eu.com.cwsfe.cms.services.breadcrumbs;
    exports eu.com.cwsfe.cms.services.configuration;
    exports eu.com.cwsfe.cms.services.i18n;
    exports eu.com.cwsfe.cms.services.keystores;
    exports eu.com.cwsfe.cms.services.news;
    exports eu.com.cwsfe.cms.services.parameters;
    exports eu.com.cwsfe.cms.services.users;

    requires eu.com.cwsfe.cms.db;

    requires hibernate.core;
    requires spring.context;
    requires spring.tx;
}
