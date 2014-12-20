package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsletterMail;
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
public class NewsletterMailDAO {

    private static final Logger LOGGER = LogManager.getLogger(NewsletterMailDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM NEWSLETTER_MAIL WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsletterMail> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT" +
                        " FROM NEWSLETTER_MAIL " +
                        " ORDER BY name ASC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMail(resultSet));
    }

    private NewsletterMail mapNewsletterMail(ResultSet resultSet) throws SQLException {
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setId(resultSet.getLong("ID"));
        newsletterMail.setRecipientGroupId(resultSet.getLong("RECIPIENT_GROUP_ID"));
        newsletterMail.setName(resultSet.getString("NAME"));
        newsletterMail.setSubject(resultSet.getString("SUBJECT"));
        newsletterMail.setMailContent(resultSet.getString("MAIL_CONTENT"));
        newsletterMail.setStatus(resultSet.getString("STATUS"));
        return newsletterMail;
    }

    public List<NewsletterMail> searchByAjax(
            int iDisplayStart, int iDisplayLength, String searchName, Long searchRecipientGroupId
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchName != null) && !searchName.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(name) like lower(?) ";
            additionalParams.add("%" + searchName + "%");
        }
        if (searchRecipientGroupId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and RECIPIENT_GROUP_ID = ? ";
            additionalParams.add(searchRecipientGroupId);
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
                        " id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT" +
                        " from NEWSLETTER_MAIL " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by name asc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapNewsletterMail(resultSet));
    }

    public int searchByAjaxCount(String searchName, Long searchRecipientGroupId) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchName != null) && !searchName.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(name) like lower(?) ";
            additionalParams.add("%" + searchName + "%");
        }
        if (searchRecipientGroupId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and RECIPIENT_GROUP_ID = ? ";
            additionalParams.add(searchRecipientGroupId);
        }
        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        " select " +
                        " id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT" +
                        " from NEWSLETTER_MAIL " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by name asc" +
                        " ) as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public NewsletterMail get(Long id) {
        String query =
                "SELECT " +
                        " id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT" +
                        " FROM NEWSLETTER_MAIL " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMail(resultSet));
    }

    public NewsletterMail getByName(String name) {
        String query =
                "SELECT " +
                        " id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT" +
                        " FROM NEWSLETTER_MAIL " +
                        "WHERE name = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = name;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterMail(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public Long add(NewsletterMail newsletterTemplate) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('NEWSLETTER_MAIL_S')", Long.class);
        Object[] dbParams = new Object[5];
        dbParams[0] = id;
        dbParams[1] = newsletterTemplate.getRecipientGroupId();
        dbParams[2] = newsletterTemplate.getName();
        dbParams[3] = newsletterTemplate.getSubject();
        dbParams[4] = newsletterTemplate.getMailContent();
        jdbcTemplate.update(
                "INSERT INTO NEWSLETTER_MAIL(id, RECIPIENT_GROUP_ID, NAME, STATUS, SUBJECT, MAIL_CONTENT) VALUES (?, ?, ?, 'N', ?, ?)",
                dbParams
        );
        return id;
    }

    public void update(NewsletterMail newsletterTemplate) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsletterTemplate.getRecipientGroupId();
        dbParams[1] = newsletterTemplate.getName();
        dbParams[2] = newsletterTemplate.getSubject();
        dbParams[3] = newsletterTemplate.getMailContent();
        dbParams[4] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL SET RECIPIENT_GROUP_ID = ?, NAME = ?, SUBJECT = ?, MAIL_CONTENT = ? WHERE id = ?",
                dbParams
        );
    }

    public void delete(NewsletterMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsletterMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

    public void send(NewsletterMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL SET status = 'P' WHERE id = ?",
                dbParams
        );
    }

}
