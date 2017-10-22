module eu.com.cwsfe.cms.rest {
    requires eu.com.cwsfe.cms.services;

    requires spring.beans;
    requires spring.tx;
    requires spring.web;
    requires javax.servlet.api;
    requires slf4j.api;
    requires swagger.annotations;
    requires eu.com.cwsfe.cms.db;       //todo there should be no such requirement!
}
