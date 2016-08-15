package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.domains.CmsUserStatus;
import eu.com.cwsfe.cms.model.CmsRole;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsUsersDAO.class, CmsRolesDAO.class, CmsUserRolesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsUserRolesDAOTest  extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsUsersDAO usersDao;
    @Autowired
    private CmsRolesDAO rolesDao;

    @Autowired
    private CmsUserRolesDAO dao;

    @Test
    public void testListForUser() throws Exception {
        //given
        //when
        List<CmsUserRole> cmsUserRoles = dao.listForUser(1L);
        //then
        assertNotNull("Roles for any user should not return null", cmsUserRoles);
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String username = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        Long userId = usersDao.add(cmsUser);
        CmsRole role = rolesDao.getByCode("ROLE_CWSFE_CMS_ADMIN");
        CmsUserRole cmsUserRole = new CmsUserRole(userId, role.getId());

        //when
        dao.add(cmsUserRole);

        //then
        List<CmsUserRole> results = dao.listForUser(userId);
        assertNotNull(results);
        assertEquals("User has one role assigned", 1, results.size());
    }

    @Test
    public void testDeleteForUser() throws Exception {
        //given
        String username = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        Long userId = usersDao.add(cmsUser);
        CmsRole role = rolesDao.getByCode("ROLE_CWSFE_CMS_ADMIN");
        CmsUserRole cmsUserRole = new CmsUserRole(userId, role.getId());
        dao.add(cmsUserRole);

        //when
        dao.deleteForUser(userId);

        //then
        List<CmsUserRole> results = dao.listForUser(userId);
        assertNotNull(results);
        assertEquals("User should not have role assigned", 0, results.size());
    }

    @Test
    public void testDeleteForUserByUserRole() throws Exception {
        //given
        String username = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        Long userId = usersDao.add(cmsUser);
        CmsRole role = rolesDao.getByCode("ROLE_CWSFE_CMS_ADMIN");
        CmsUserRole cmsUserRole = new CmsUserRole(userId, role.getId());
        dao.add(cmsUserRole);

        //when
        dao.deleteForUser(cmsUserRole);

        //then
        List<CmsUserRole> results = dao.listForUser(userId);
        assertNotNull(results);
        assertEquals("User should not have role assigned", 0, results.size());
    }

}