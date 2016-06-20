package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.CmsUserStatus;
import eu.com.cwsfe.cms.model.CmsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsUsersDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUsersDAO.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CmsUsersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isActiveUsernameInDatabase(String username) {
        Object[] dbParams = new Object[1];
        dbParams[0] = username;
        String query = "SELECT COUNT(*) FROM CMS_USERS WHERE USERNAME = ? and status in ('N')";
        Integer numberOfUsers = jdbcTemplate.queryForObject(query, dbParams, Integer.class);
        return numberOfUsers == 1;
    }

    public CmsUser getByUsername(String username) {
        Object[] dbParams = new Object[1];
        dbParams[0] = username;
        String query = "SELECT ID, USERNAME, PASSWORD_HASH, STATUS FROM CMS_USERS WHERE USERNAME = ?";
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsUser(resultSet));
    }

    private CmsUser mapCmsUser(ResultSet resultSet) throws SQLException {
        CmsUser cmsUser = new CmsUser();
        cmsUser.setId(resultSet.getLong("ID"));
        cmsUser.setUserName(resultSet.getString("USERNAME"));
        cmsUser.setPasswordHash(resultSet.getString("PASSWORD_HASH"));
        cmsUser.setStatus(CmsUserStatus.fromCode(resultSet.getString("STATUS")));
        return cmsUser;
    }

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_USERS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsUser> list() {
        String query =
                "SELECT " +
                        " id, username, password_hash, status" +
                        " FROM CMS_USERS " +
                        " WHERE status = 'N'" +
                        " ORDER BY username";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsUser(resultSet));
    }

    public List<CmsUser> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, username, password_hash, status" +
                        " FROM CMS_USERS " +
                        " ORDER BY username" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsUser(resultSet));
    }

    public List<CmsUser> listUsersForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        " id, username, password_hash, status" +
                        " FROM CMS_USERS " +
                        " WHERE status = 'N' AND lower(username) LIKE lower(?) " +
                        " ORDER BY username" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsUser(resultSet));
    }

    @Cacheable(value="cmsUserById")
    public CmsUser get(Long id) {
        String query =
                "SELECT " +
                        " id, username, password_hash, status" +
                        " FROM CMS_USERS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        CmsUser cmsUser = null;
        try {
            cmsUser = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapCmsUser(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("User does not exist for id: {}", id) ;
        }
        return cmsUser;
    }

    public Long add(CmsUser cmsUser) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_USERS_S')", Long.class);
        Object[] dbParams = new Object[3];
        dbParams[0] = id;
        dbParams[1] = cmsUser.getUserName();
        dbParams[2] = cmsUser.getPasswordHash();
        jdbcTemplate.update(
                "INSERT INTO CMS_USERS(id, username, password_hash, status) VALUES (?, ?, ?, 'N')",
                dbParams
        );
        return id;
    }

    @CacheEvict(value = {"cmsUserById"})
    public void update(CmsUser cmsUser) {
        Object[] dbParams = new Object[3];
        dbParams[0] = cmsUser.getUserName();
        dbParams[1] = cmsUser.getPasswordHash();
        dbParams[2] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET username = ?, password_hash = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void updatePostBasicInfo(CmsUser cmsUser) {
        Object[] dbParams = new Object[3];
        dbParams[0] = cmsUser.getUserName();
        dbParams[1] = cmsUser.getStatus().getCode();
        dbParams[2] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET username = ?, status = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void updateWithoutPassword(CmsUser cmsUser) {
        Object[] dbParams = new Object[2];
        dbParams[0] = cmsUser.getUserName();
        dbParams[1] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET username = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void delete(CmsUser cmsUser) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void undelete(CmsUser cmsUser) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void lock(CmsUser cmsUser) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET status = 'L' WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsUserById"})
    public void unlock(CmsUser cmsUser) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUser.getId();
        jdbcTemplate.update(
                "UPDATE CMS_USERS SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
