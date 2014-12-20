package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsletterDispatchedMail;
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
class NewsletterDispatchedMailDAO {

    private static final Logger LOGGER = LogManager.getLogger(NewsletterDispatchedMailDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM NEWSLETTER_DISPATCHED_MAILS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsletterDispatchedMail> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS" +
                        " FROM NEWSLETTER_DISPATCHED_MAILS " +
                        " ORDER BY NEWSLETTER_MAIL_ID, EMAIL ASC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterDispatchedMail(resultSet));
    }

    private NewsletterDispatchedMail mapNewsletterDispatchedMail(ResultSet resultSet) throws SQLException {
        NewsletterDispatchedMail newsletterDispatchedMail = new NewsletterDispatchedMail();
        newsletterDispatchedMail.setId(resultSet.getLong("ID"));
        newsletterDispatchedMail.setNewsletterMailId(resultSet.getLong("NEWSLETTER_MAIL_ID"));
        newsletterDispatchedMail.setEmail(resultSet.getString("EMAIL"));
        newsletterDispatchedMail.setError(resultSet.getString("ERROR"));
        newsletterDispatchedMail.setStatus(resultSet.getString("STATUS"));
        return newsletterDispatchedMail;
    }


    public List<NewsletterDispatchedMail> searchByAjax(
            int iDisplayStart, int iDisplayLength, String searchEmail, Long searchNewsletterDispatchedMailId
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchEmail != null) && !searchEmail.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(EMAIL) like lower(?) ";
            additionalParams.add("%" + searchEmail + "%");
        }
        if (searchNewsletterDispatchedMailId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and NEWSLETTER_MAIL_ID = ? ";
            additionalParams.add(searchNewsletterDispatchedMailId);
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
                        " id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS" +
                        " from NEWSLETTER_DISPATCHED_MAILS " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by NEWSLETTER_MAIL_ID, EMAIL asc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapNewsletterDispatchedMail(resultSet));
    }

    public int searchByAjaxCount(String searchEmail, Long searchNewsletterDispatchedMailId) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchEmail != null) && !searchEmail.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(EMAIL) like lower(?) ";
            additionalParams.add("%" + searchEmail + "%");
        }
        if (searchNewsletterDispatchedMailId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and NEWSLETTER_MAIL_ID = ? ";
            additionalParams.add(searchNewsletterDispatchedMailId);
        }
        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        " select " +
                        " id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS" +
                        " from NEWSLETTER_DISPATCHED_MAILS " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by NEWSLETTER_MAIL_ID, EMAIL asc" +
                        " ) as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public NewsletterDispatchedMail get(Long id) {
        String query =
                "SELECT " +
                        " id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS" +
                        " FROM NEWSLETTER_DISPATCHED_MAILS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterDispatchedMail(resultSet));
    }

    public NewsletterDispatchedMail getByName(String email) {
        String query =
                "SELECT " +
                        " id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS" +
                        " FROM NEWSLETTER_DISPATCHED_MAILS " +
                        "WHERE EMAIL = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = email;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterDispatchedMail(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public Long add(NewsletterDispatchedMail newsletterTemplate) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('NEWSLETTER_DISPATCHED_MAILS_S')", Long.class);
        Object[] dbParams = new Object[5];
        dbParams[0] = id;
        dbParams[1] = newsletterTemplate.getNewsletterMailId();
        dbParams[2] = newsletterTemplate.getEmail();
        dbParams[3] = newsletterTemplate.getError();
        dbParams[4] = newsletterTemplate.getStatus();
        jdbcTemplate.update(
                "INSERT INTO NEWSLETTER_DISPATCHED_MAILS(id, NEWSLETTER_MAIL_ID, EMAIL, ERROR, STATUS) VALUES (?, ?, ?, ?, ?)",
                dbParams
        );
        return id;
    }

    public void update(NewsletterDispatchedMail newsletterTemplate) {
        Object[] dbParams = new Object[5];
        dbParams[0] = newsletterTemplate.getNewsletterMailId();
        dbParams[1] = newsletterTemplate.getEmail();
        dbParams[2] = newsletterTemplate.getError();
        dbParams[3] = newsletterTemplate.getStatus();
        dbParams[4] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_DISPATCHED_MAILS SET NEWSLETTER_MAIL_ID = ?, EMAIL = ?, ERROR = ? = ? WHERE id = ?",
                dbParams
        );
    }

    public void delete(NewsletterDispatchedMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_DISPATCHED_MAILS SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsletterDispatchedMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_DISPATCHED_MAILS SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

    public void setSent(NewsletterDispatchedMail newsletterTemplate) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_DISPATCHED_MAILS SET status = 'S' WHERE id = ?",
                dbParams
        );
    }

    public void setError(NewsletterDispatchedMail newsletterTemplate) {
        Object[] dbParams = new Object[2];
        dbParams[0] = newsletterTemplate.getError();
        dbParams[0] = newsletterTemplate.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_DISPATCHED_MAILS SET ERROR = ?, status = 'E' WHERE id = ?",
                dbParams
        );
    }

}
