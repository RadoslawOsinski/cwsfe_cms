package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class CmsRolesDAOTest  extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsRolesDAO dao;

    @Test
    public void testListUserRoles() throws Exception {
        //given
        Long userId = 1l;

        //when
        List<CmsRole> list = dao.listUserRoles(userId);

        //then
        assertNotNull("There should be few predefined roles", list);
        assertTrue("ROLE_CWSFE_CMS_ADMIN should be predefined", list.stream().filter(w -> "ROLE_CWSFE_CMS_ADMIN".equals(w.getRoleCode())).count() > 0);
    }

    @Test
    public void testCountForAjax() throws Exception {
        //given
        //when
        int result = dao.countForAjax();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testList() throws Exception {
        //given
        //when
        List<CmsRole> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("Embedded admin roles should exist", list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        //when
        List<CmsRole> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListRolesForDropList() throws Exception {
        //given
        String roleName = "ADMIN";

        //when
        List<CmsRole> cmsRoles = dao.listRolesForDropList(roleName, 1);

        //then
        assertNotNull(cmsRoles);
        assertEquals("Page limit was set to 1", 1, cmsRoles.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        Long roleId = 1l;

        //when
        CmsRole cmsRoleResult = dao.get(roleId);

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) roleId, (long) cmsRoleResult.getId());
        assertNotNull(cmsRoleResult.getRoleCode());
        assertNotNull(cmsRoleResult.getRoleName());
    }

    @Test
    public void testGetByCode() throws Exception {
        //given
        String code = "ROLE_CWSFE_CMS_ADMIN";

        //when
        CmsRole cmsRoleResult = dao.getByCode(code);

        //then
        assertNotNull(cmsRoleResult);
        assertNotNull(cmsRoleResult.getRoleName());
        assertEquals(code, cmsRoleResult.getRoleCode());
    }
}