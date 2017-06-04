package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsGlobalParamsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsGlobalParamsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsGlobalParamsRepository dao;

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
        List<CmsGlobalParam> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        dao.add(cmsGlobalParam);

        //when
        List<CmsGlobalParam> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListForDropList() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        dao.add(cmsGlobalParam);

        //when
        List<CmsGlobalParam> results = dao.listForDropList(code, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        cmsGlobalParam.setId(dao.add(cmsGlobalParam));

        //when
        CmsGlobalParam cmsRoleResult = dao.get(cmsGlobalParam.getId());

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsGlobalParam.getId(), (long) cmsRoleResult.getId());
        assertEquals(code, cmsRoleResult.getCode());
        assertEquals(defaultValue, cmsRoleResult.getDefaultValue());
        assertEquals(value, cmsRoleResult.getValue());
        assertEquals(description, cmsRoleResult.getDescription());
    }

    @Test
    public void testGetByCode() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        cmsGlobalParam.setId(dao.add(cmsGlobalParam));

        //when
        CmsGlobalParam cmsRoleResult = dao.getByCode(code);

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsGlobalParam.getId(), (long) cmsRoleResult.getId());
        assertEquals(code, cmsRoleResult.getCode());
        assertEquals(defaultValue, cmsRoleResult.getDefaultValue());
        assertEquals(value, cmsRoleResult.getValue());
        assertEquals(description, cmsRoleResult.getDescription());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        cmsGlobalParam.setCode(code);

        //when
        cmsGlobalParam.setId(dao.add(cmsGlobalParam));

        //then
        CmsGlobalParam cmsRoleResult = dao.get(cmsGlobalParam.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsGlobalParam.getId(), (long) cmsRoleResult.getId());
        assertEquals(code, cmsRoleResult.getCode());
        assertEquals(defaultValue, cmsRoleResult.getDefaultValue());
        assertEquals(value, cmsRoleResult.getValue());
        assertEquals(description, cmsRoleResult.getDescription());

    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String code = "test";
        String newCode = "test2";
        String defaultValue = "defaultValue";
        String newDefaultValue = "defaultValue2";
        String value = "value";
        String newValue = "value2";
        String description = "description";
        String newDescription = "description2";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        cmsGlobalParam.setId(dao.add(cmsGlobalParam));
        cmsGlobalParam.setCode(newCode);
        cmsGlobalParam.setDefaultValue(newDefaultValue);
        cmsGlobalParam.setValue(newValue);
        cmsGlobalParam.setDescription(newDescription);

        //when
        dao.update(cmsGlobalParam);

        //then
        CmsGlobalParam cmsRoleResult = dao.get(cmsGlobalParam.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsGlobalParam.getId(), (long) cmsRoleResult.getId());
        assertEquals(newCode, cmsRoleResult.getCode());
        assertEquals(newDefaultValue, cmsRoleResult.getDefaultValue());
        assertEquals(newValue, cmsRoleResult.getValue());
        assertEquals(newDescription, cmsRoleResult.getDescription());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String code = "test";
        String defaultValue = "defaultValue";
        String value = "value";
        String description = "description";
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setCode(code);
        cmsGlobalParam.setDefaultValue(defaultValue);
        cmsGlobalParam.setValue(value);
        cmsGlobalParam.setDescription(description);
        cmsGlobalParam.setId(dao.add(cmsGlobalParam));

        //when
        dao.delete(cmsGlobalParam);

        //then
        CmsGlobalParam cmsGlobalParamResult = null;
        try {
            cmsGlobalParamResult = dao.get(cmsGlobalParam.getId());
        } catch (EmptyResultDataAccessException e) {
            assertNotNull("Global param should not exist after deletion", e);
        }
        assertNull("Global param should not exist after deletion", cmsGlobalParamResult);
    }
}
