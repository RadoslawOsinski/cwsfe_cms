package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsletterMailGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NewsletterMailGroupDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM NEWSLETTER_MAIL_GROUPS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsletterMailGroup> list() {
        String query =
                "SELECT " +
                        " id, language_id, name, status" +
                        " FROM NEWSLETTER_MAIL_GROUPS " +
                        " WHERE status = 'N'" +
                        " ORDER BY name";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapNewsletterMailGroup(resultSet));
    }

    private NewsletterMailGroup mapNewsletterMailGroup(ResultSet resultSet) throws SQLException {
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setId(resultSet.getLong("ID"));
        newsletterMailGroup.setLanguageId(resultSet.getLong("LANGUAGE_ID"));
        newsletterMailGroup.setName(resultSet.getString("NAME"));
        newsletterMailGroup.setStatus(resultSet.getString("STATUS"));
        return newsletterMailGroup;
    }

    public List<NewsletterMailGroup> listNewsletterMailGroupsForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        " id, language_id, name, status" +
                        " FROM NEWSLETTER_MAIL_GROUPS " +
                        " WHERE status = 'N' AND lower(name) LIKE lower(?) " +
                        " ORDER BY name" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailGroup(resultSet));
    }


    public List<NewsletterMailGroup> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, language_id, name, status" +
                        " FROM NEWSLETTER_MAIL_GROUPS " +
                        " WHERE status = 'N'" +
                        " ORDER BY name" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailGroup(resultSet));
    }

    public List<NewsletterMailGroup> searchByAjax(
            int iDisplayStart, int iDisplayLength, String searchName, Long searchLanguageId
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(1);
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
                        " id, language_id, name, status" +
                        " from NEWSLETTER_MAIL_GROUPS " +
                        " where status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by name desc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapNewsletterMailGroup(resultSet));
    }

    public int searchByAjaxCount(String searchName, Long searchLanguageId) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(5);
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
                        " id, language_id, name, status" +
                        " from NEWSLETTER_MAIL_GROUPS " +
                        " where status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by name desc" +
                        " ) as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public NewsletterMailGroup get(Long id) {
        String query =
                "SELECT " +
                        " id, language_id, name, status" +
                        " FROM NEWSLETTER_MAIL_GROUPS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailGroup(resultSet));
    }

    public NewsletterMailGroup getByNameAndLanguage(String name, Long languageId) {
        String query =
                "SELECT " +
                        " id, language_id, name, status" +
                        " FROM NEWSLETTER_MAIL_GROUPS " +
                        "WHERE name = ? AND language_id = ?";
        Object[] dbParams = new Object[2];
        dbParams[0] = name;
        dbParams[1] = languageId;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsletterMailGroup(resultSet));
    }

    public Long add(NewsletterMailGroup newsletterMailGroup) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('NEWSLETTER_MAIL_GROUPS_S')", Long.class);
        Object[] dbParams = new Object[3];
        dbParams[0] = id;
        dbParams[1] = newsletterMailGroup.getLanguageId();
        dbParams[2] = newsletterMailGroup.getName();
        jdbcTemplate.update(
                "INSERT INTO NEWSLETTER_MAIL_GROUPS(id, language_id, name, status) VALUES (?, ?, ?, 'N')",
                dbParams
        );
        return id;
    }

    public void update(NewsletterMailGroup newsletterMailGroup) {
        Object[] dbParams = new Object[3];
        dbParams[0] = newsletterMailGroup.getLanguageId();
        dbParams[1] = newsletterMailGroup.getName();
        dbParams[2] = newsletterMailGroup.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_GROUPS SET language_id = ?, name = ? WHERE id = ?",
                dbParams
        );
    }

    public void delete(NewsletterMailGroup newsletterMailGroup) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailGroup.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_GROUPS SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsletterMailGroup newsletterMailGroup) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsletterMailGroup.getId();
        jdbcTemplate.update(
                "UPDATE NEWSLETTER_MAIL_GROUPS SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
