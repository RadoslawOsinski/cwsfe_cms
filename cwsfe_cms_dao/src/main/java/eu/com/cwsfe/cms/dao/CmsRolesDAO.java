package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsRolesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CmsRole> listUserRoles(Long userId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = userId;
        String query = "" +
                "SELECT ID, ROLE_CODE, ROLE_NAME " +
                "FROM CMS_ROLES cr, cms_user_roles cur " +
                "WHERE cr.id = cur.role_id and cur.cms_user_id = ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapCmsRole(resultSet));
    }

    private CmsRole mapCmsRole(ResultSet resultSet) throws SQLException {
        CmsRole cmsRole = new CmsRole();
        cmsRole.setId(resultSet.getLong("ID"));
        cmsRole.setRoleCode(resultSet.getString("ROLE_CODE"));
        cmsRole.setRoleName(resultSet.getString("ROLE_NAME"));
        return cmsRole;
    }


    public int countForAjax() {
        String query = "SELECT count(id) from CMS_ROLES";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsRole> list() {
        String query =
                "SELECT " +
                        " ID, ROLE_CODE, ROLE_NAME " +
                        " FROM CMS_ROLES " +
                        " order by role_name";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsRole(resultSet));
    }

    public List<CmsRole> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " ID, ROLE_CODE, ROLE_NAME " +
                        " FROM CMS_ROLES " +
                        " order by role_name" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsRole(resultSet));
    }

    public List<CmsRole> listRolesForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        " id, ID, ROLE_CODE, ROLE_NAME " +
                        " FROM CMS_ROLES " +
                        " where lower(role_name) like lower(?) " +
                        " order by role_name" +
                        " limit ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsRole(resultSet));
    }

    @Cacheable(value="cmsRoleById")
    public CmsRole get(Long id) {
        String query =
                "SELECT " +
                        " ID, ROLE_CODE, ROLE_NAME" +
                        " FROM CMS_ROLES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsRole(resultSet));
    }

    @Cacheable(value="cmsRoleByCode")
    public CmsRole getByCode(String roleCode) {
        String query =
                "SELECT " +
                        " ID, ROLE_CODE, ROLE_NAME" +
                        " FROM CMS_ROLES " +
                        "WHERE ROLE_CODE = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = roleCode;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsRole(resultSet));
    }

}
