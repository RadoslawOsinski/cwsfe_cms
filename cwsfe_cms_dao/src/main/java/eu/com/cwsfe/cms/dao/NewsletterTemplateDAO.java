package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsletterTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author radek
 */
@Repository
public class NewsletterTemplateDAO {

    private static final Logger LOGGER = LogManager.getLogger(NewsletterTemplateDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM NEWSLETTER_TEMPLATES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsletterTemplate> list() {
        String query =
                "SELECT " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " FROM NEWSLETTER_TEMPLATES " +
                        " ORDER BY name ASC";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapNewsletterTemplate(resultSet));
    }

    private NewsletterTemplate mapNewsletterTemplate(ResultSet resultSet) throws SQLException {
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setId(resultSet.getLong("ID"));
        newsletterTemplate.setLanguageId(resultSet.getLong("LANGUAGE_ID"));
        newsletterTemplate.setName(resultSet.getString("NAME"));
        newsletterTemplate.setStatus(resultSet.getString("STATUS"));
        newsletterTemplate.setSubject(resultSet.getString("SUBJECT"));
        newsletterTemplate.setContent(resultSet.getString("CONTENT"));
        return newsletterTemplate;
    }

    public List<NewsletterTemplate> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " FROM NEWSLETTER_TEMPLATES " +
                        " ORDER BY name ASC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterTemplate(resultSet));
    }

    public List<NewsletterTemplate> searchByAjax(
            int iDisplayStart, int iDisplayLength, String searchName, Long searchLanguageId
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchName != null) && !searchName.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(name) like lower(?) ";
            additionalParams.add("%" + searchName + "%");
        }
        if (searchLanguageId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and language_id = ? ";
            additionalParams.add(searchLanguageId);
        }
        numberOfSearchParams++;
        additionalParams.add(iDisplayLength);
        numberOfSearchParams++;
        additionalParams.add(iDisplayStart);
        Object[] dbParams = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParams[i] = additionalParams.get(i);
        }
        String query =
                "select " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " from NEWSLETTER_TEMPLATES " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by name asc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapNewsletterTemplate(resultSet));
    }

    public int searchByAjaxCount(String searchName, Long searchLanguageId) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchName != null) && !searchName.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(name) like lower(?) ";
            additionalParams.add("%" + searchName + "%");
        }
        if (searchLanguageId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and language_id = ? ";
            additionalParams.add(searchLanguageId);
        }
        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        " select " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " from NEWSLETTER_TEMPLATES " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by name asc" +
                        " ) as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public NewsletterTemplate get(Long id) {
        String query =
                "SELECT " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " FROM NEWSLETTER_TEMPLATES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterTemplate(resultSet));
    }

    public NewsletterTemplate getByName(String name) {
        String query =
                "SELECT " +
                        " id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT" +
                        " FROM NEWSLETTER_TEMPLATES " +
                        "WHERE name = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = name;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterTemplate(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public Long add(NewsletterTemplate newsletterTemplate) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('NEWSLETTER_TEMPLATES_S')", Long.class);
        Object[] dbParams = new Object[3];
        dbParams[0] = id;
        dbParams[1] = newsletterTemplate.getLanguageId();
        dbParams[2] = newsletterTemplate.getName();
        jdbcTemplate.update(
                "INSERT INTO NEWSLETTER_TEMPLATES(id, LANGUAGE_ID, NAME, STATUS, SUBJECT, CONTENT) VALUES (?, ?, ?, 'N', '', '')",
                dbParams
        );
        return id;
    }

    public void update(NewsletterTemplate newsletterTemplate) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsletterTemplate.getLanguageId();
        dbParams[1] = newsletterTemplate.getName();
        dbParams[2] = newsletterTemplate.getSubject();
        dbParams[3] = newsletterTemplate.getContent();
        dbParams[4] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_TEMPLATES SET LANGUAGE_ID = ?, NAME = ?, SUBJECT = ?, CONTENT = ? WHERE id = ?",
                dbParams
        );
    }

    public void delete(NewsletterTemplate newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_TEMPLATES SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsletterTemplate newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_TEMPLATES SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
