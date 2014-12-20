package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsletterMailAddress;
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
public class NewsletterMailAddressDAO {

    private static final Logger LOGGER = LogManager.getLogger(NewsletterMailAddressDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM NEWSLETTER_MAIL_ADDRESSES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsletterMailAddress> list() {
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        " ORDER BY email ASC";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapNewsletterMailAddress(resultSet));
    }

    private NewsletterMailAddress mapNewsletterMailAddress(ResultSet resultSet) throws SQLException {
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setId(resultSet.getLong("ID"));
        newsletterMailAddress.setMailGroupId(resultSet.getLong("MAIL_GROUP_ID"));
        newsletterMailAddress.setConfirmString(resultSet.getString("CONFIRM_STRING"));
        newsletterMailAddress.setUnSubscribeString(resultSet.getString("UN_SUBSCRIBE_STRING"));
        newsletterMailAddress.setEmail(resultSet.getString("EMAIL"));
        newsletterMailAddress.setStatus(resultSet.getString("STATUS"));
        return newsletterMailAddress;
    }

    public List<NewsletterMailAddress> listByRecipientGroup(Long recipientGroupId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = recipientGroupId;
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        " WHERE mail_group_id = ?" +
                        " ORDER BY email ASC";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailAddress(resultSet));
    }

    public List<NewsletterMailAddress> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        " ORDER BY email ASC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailAddress(resultSet));
    }

    public List<NewsletterMailAddress> searchByAjax(
            int iDisplayStart, int iDisplayLength, String searchMail, Long mailGroupId
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchMail != null) && !searchMail.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(email) like lower(?) ";
            additionalParams.add("%" + searchMail + "%");
        }
        if (mailGroupId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and mail_group_id = ? ";
            additionalParams.add(mailGroupId);
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
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " from NEWSLETTER_MAIL_ADDRESSES " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by email asc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapNewsletterMailAddress(resultSet));
    }

    public int searchByAjaxCount(String searchMail, Long mailGroupId) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(2);
        if ((searchMail != null) && !searchMail.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(email) like lower(?) ";
            additionalParams.add("%" + searchMail + "%");
        }
        if (mailGroupId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and mail_group_id = ? ";
            additionalParams.add(mailGroupId);
        }
        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        " select " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " from NEWSLETTER_MAIL_ADDRESSES " +
                        " where 1 = 1 " + additionalQuery +
                        " and 1 = 1" +
                        " order by email asc" +
                        " ) as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public NewsletterMailAddress get(Long id) {
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailAddress(resultSet));
    }

    public NewsletterMailAddress getByEmailAndMailGroup(String email, Long mailGroupId) {
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        "WHERE email = ? AND mail_group_id = ?";
        Object[] dbParams = new Object[2];
        dbParams[0] = email;
        dbParams[1] = mailGroupId;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterMailAddress(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public NewsletterMailAddress getByConfirmString(String confirmString) {
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        "WHERE confirm_string = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = confirmString;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterMailAddress(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public NewsletterMailAddress getByUnSubscribeString(String unSubscribeString) {
        String query =
                "SELECT " +
                        " id, mail_group_id, confirm_string, un_subscribe_string, email, status" +
                        " FROM NEWSLETTER_MAIL_ADDRESSES " +
                        "WHERE un_subscribe_string = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = unSubscribeString;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapNewsletterMailAddress(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public Long add(NewsletterMailAddress newsletterMailAddress) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('NEWSLETTER_MAIL_ADDRESSES_S')", Long.class);
        Object[] dbParams = new Object[6];
        dbParams[0] = id;
        dbParams[1] = newsletterMailAddress.getMailGroupId();
        dbParams[2] = newsletterMailAddress.getConfirmString();
        dbParams[3] = newsletterMailAddress.getUnSubscribeString();
        dbParams[4] = newsletterMailAddress.getEmail();
        dbParams[5] = NewsletterMailAddress.STATUS_INACTIVE;
        jdbcTemplate.update(
                "INSERT INTO NEWSLETTER_MAIL_ADDRESSES(id, mail_group_id, confirm_string, un_subscribe_string, email, status) VALUES (?, ?, ?, ?, ?, ?)",
                dbParams
        );
        return id;
    }

    public void update(NewsletterMailAddress newsletterMailAddress) {
        Object[] dbParams = new Object[2];
        dbParams[0] = newsletterMailAddress.getEmail();
        dbParams[1] = newsletterMailAddress.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_ADDRESSES SET email = ? WHERE id = ?",
                dbParams
        );
    }

    public void delete(NewsletterMailAddress newsletterMailAddress) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailAddress.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_ADDRESSES SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsletterMailAddress newsletterMailAddress) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailAddress.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_ADDRESSES SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

    public void activate(NewsletterMailAddress newsletterMailAddress) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailAddress.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_ADDRESSES SET status = 'A' WHERE id = ?",
                dbParams
        );
    }

    public void deactivate(NewsletterMailAddress newsletterMailAddress) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailAddress.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_ADDRESSES SET status = 'I' WHERE id = ?",
                dbParams
        );
    }

}
