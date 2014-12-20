package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsAuthorsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_AUTHORS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsAuthor> list() {
        String query =
                "SELECT " +
                        " id, first_name, last_name, google_plus_author_link, status" +
                        " FROM CMS_AUTHORS " +
                        " WHERE status = 'N'" +
                        " ORDER BY last_name, first_name";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsAuthor(resultSet));
    }

    private CmsAuthor mapCmsAuthor(ResultSet resultSet) throws SQLException {
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setId(resultSet.getLong("ID"));
        cmsAuthor.setFirstName(resultSet.getString("FIRST_NAME"));
        cmsAuthor.setLastName(resultSet.getString("LAST_NAME"));
        cmsAuthor.setGooglePlusAuthorLink(resultSet.getString("GOOGLE_PLUS_AUTHOR_LINK"));
        cmsAuthor.setStatus(resultSet.getString("STATUS"));
        return cmsAuthor;
    }

    public List<CmsAuthor> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, first_name, last_name, google_plus_author_link, status" +
                        " FROM CMS_AUTHORS " +
                        " WHERE status = 'N'" +
                        " ORDER BY last_name, first_name" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsAuthor(resultSet));
    }

    public List<CmsAuthor> listAuthorsForDropList(String term, int limit) {
        Object[] dbParams = new Object[3];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = '%' + term + '%';
        dbParams[2] = limit;
        String query =
                "SELECT " +
                        " id, first_name, last_name, google_plus_author_link, status" +
                        " FROM CMS_AUTHORS " +
                        " WHERE status = 'N' AND (lower(first_name) LIKE lower(?) OR lower(last_name) LIKE lower(?)) " +
                        " ORDER BY last_name, first_name" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsAuthor(resultSet));
    }

    @Cacheable(value="cmsAuthorById")
    public CmsAuthor get(Long id) {
        String query =
                "SELECT " +
                        " id, first_name, last_name, google_plus_author_link, status" +
                        " FROM CMS_AUTHORS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsAuthor(resultSet));
    }

    public Long add(CmsAuthor cmsAuthor) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_AUTHORS_S')", Long.class);
        Object[] dbParams = new Object[4];
        dbParams[0] = id;
        dbParams[1] = cmsAuthor.getFirstName();
        dbParams[2] = cmsAuthor.getLastName();
        dbParams[3] = cmsAuthor.getGooglePlusAuthorLink();
        jdbcTemplate.update(
                "INSERT INTO CMS_AUTHORS(id, first_name, last_name, google_plus_author_link, status) VALUES (?, ?, ?, ?, 'N')",
                dbParams
        );
        return id;
    }

    @CacheEvict(value = {"cmsAuthorById"})
    public void update(CmsAuthor cmsAuthor) {
        Object[] dbParams = new Object[4];
        dbParams[0] = cmsAuthor.getFirstName();
        dbParams[1] = cmsAuthor.getLastName();
        dbParams[2] = cmsAuthor.getGooglePlusAuthorLink();
        dbParams[3] = cmsAuthor.getId();
        jdbcTemplate.update(
                "UPDATE CMS_AUTHORS SET first_name = ?, last_name = ?, google_plus_author_link = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsAuthorById"})
    public void delete(CmsAuthor cmsAuthor) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsAuthor.getId();
        jdbcTemplate.update(
                "UPDATE CMS_AUTHORS SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsAuthorById"})
    public void undelete(CmsAuthor cmsAuthor) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsAuthor.getId();
        jdbcTemplate.update(
                "UPDATE CMS_AUTHORS SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
