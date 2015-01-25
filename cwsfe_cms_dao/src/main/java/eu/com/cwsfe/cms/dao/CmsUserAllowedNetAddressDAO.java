package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsUserAllowedNetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsUserAllowedNetAddressDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CmsUserAllowedNetAddress mapCmsUserAllowedNetAddress(ResultSet resultSet) throws SQLException {
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setId(resultSet.getLong("ID"));
        cmsUserAllowedNetAddress.setUserId(resultSet.getLong("USER_ID"));
        cmsUserAllowedNetAddress.setInetAddress(resultSet.getString("INET_ADDRESS"));
        return cmsUserAllowedNetAddress;
    }

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_USER_ALLOWED_NET_ADDRESS";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsUserAllowedNetAddress> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        "id, user_id, INET_ADDRESS " +
                        "FROM CMS_USER_ALLOWED_NET_ADDRESS " +
                        "ORDER BY INET_ADDRESS" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsUserAllowedNetAddress(resultSet));
    }

    public int countAddressesForUser(long userId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = userId;
        String query =
                "SELECT count(id) FROM CMS_USER_ALLOWED_NET_ADDRESS where user_id = ?";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<CmsUserAllowedNetAddress> listForUser(long userId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = userId;
        String query =
                "SELECT " +
                        "id, user_id, INET_ADDRESS " +
                        "FROM CMS_USER_ALLOWED_NET_ADDRESS " +
                        "where user_id = ? " +
                        "ORDER BY INET_ADDRESS";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsUserAllowedNetAddress(resultSet));
    }

    public CmsUserAllowedNetAddress get(Long id) {
        String query =
                "SELECT " +
                        "id, user_id, INET_ADDRESS " +
                        "FROM CMS_USER_ALLOWED_NET_ADDRESS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapCmsUserAllowedNetAddress(resultSet));
    }

    public Long add(CmsUserAllowedNetAddress cmsUserAllowedNetAddress) {
        Object[] dbParams = new Object[3];
        Long id = jdbcTemplate.queryForObject("SELECT nextval(" +
                "'CMS_USER_ALLOWED_NET_ADDRESS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsUserAllowedNetAddress.getUserId();
        dbParams[2] = cmsUserAllowedNetAddress.getInetAddress();
        jdbcTemplate.update(
                "INSERT INTO CMS_USER_ALLOWED_NET_ADDRESS(id, user_id, INET_ADDRESS) VALUES (?, ?, ?::inet)",
                dbParams);
        return id;
    }

    public void delete(CmsUserAllowedNetAddress cmsUserAllowedNetAddress) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUserAllowedNetAddress.getId();
        jdbcTemplate.update("DELETE from CMS_USER_ALLOWED_NET_ADDRESS WHERE id = ?", dbParams);
    }

}
