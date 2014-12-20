package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CmsUserRolesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(CmsUserRole cmsUserRole) {
        Object[] dbParams = new Object[2];
        dbParams[0] = cmsUserRole.getCmsUserId();
        dbParams[1] = cmsUserRole.getRoleId();
        jdbcTemplate.update("INSERT INTO CMS_USER_ROLES(cms_user_id, role_id) VALUES(?, ?)", dbParams);
    }

    public void deleteForUser(CmsUserRole cmsUserRole) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsUserRole.getCmsUserId();
        jdbcTemplate.update("delete from CMS_USER_ROLES where cms_user_id = ?", dbParams);
    }

    public void deleteForUser(Long id) {
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        jdbcTemplate.update("delete from CMS_USER_ROLES where cms_user_id = ?", dbParams);
    }

}
