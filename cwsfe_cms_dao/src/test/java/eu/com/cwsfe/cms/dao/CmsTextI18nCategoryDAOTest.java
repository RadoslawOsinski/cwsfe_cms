package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.CmsTextI18nCategoryStatus;
import eu.com.cwsfe.cms.model.CmsTextI18nCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsTextI18nCategoryDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsTextI18nCategoryDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsTextI18nCategoryRepository dao;

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
        List<CmsTextI18nCategory> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        dao.add(cmsTextI18nCategory);

        //when
        List<CmsTextI18nCategory> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListForDropList() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        dao.add(cmsTextI18nCategory);

        //when
        List<CmsTextI18nCategory> results = dao.listForDropList(category, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategory.setId(dao.add(cmsTextI18nCategory));

        //when
        CmsTextI18nCategory cmsRoleResult = dao.get(cmsTextI18nCategory.getId());

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsTextI18nCategory.getId(), (long) cmsRoleResult.getId());
        assertEquals(category, cmsRoleResult.getCategory());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);

        //when
        cmsTextI18nCategory.setId(dao.add(cmsTextI18nCategory));

        //then
        CmsTextI18nCategory cmsRoleResult = dao.get(cmsTextI18nCategory.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsTextI18nCategory.getId(), (long) cmsRoleResult.getId());
        assertEquals(category, cmsRoleResult.getCategory());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String category = "test";
        String newCategory = "test2";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategory.setId(dao.add(cmsTextI18nCategory));
        cmsTextI18nCategory.setCategory(newCategory);
        cmsTextI18nCategory.setStatus(CmsTextI18nCategoryStatus.NEW);

        //when
        dao.update(cmsTextI18nCategory);

        //then
        CmsTextI18nCategory cmsRoleResult = dao.get(cmsTextI18nCategory.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsTextI18nCategory.getId(), (long) cmsRoleResult.getId());
        assertEquals(newCategory, cmsRoleResult.getCategory());
        assertEquals(CmsTextI18nCategoryStatus.NEW, cmsRoleResult.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategory.setId(dao.add(cmsTextI18nCategory));

        //when
        dao.delete(cmsTextI18nCategory);

        //then
        CmsTextI18nCategory cmsTextI18nCategoryResult = dao.get(cmsTextI18nCategory.getId());
        assertNotNull(cmsTextI18nCategoryResult);
        assertEquals((long) cmsTextI18nCategory.getId(), (long) cmsTextI18nCategoryResult.getId());
        assertEquals("Unexpected status value for deleted object", CmsTextI18nCategoryStatus.DELETED, cmsTextI18nCategoryResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String category = "test";
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategory.setId(dao.add(cmsTextI18nCategory));
        dao.delete(cmsTextI18nCategory);

        //when
        dao.undelete(cmsTextI18nCategory);

        //then
        CmsTextI18nCategory cmsTextI18nCategoryResult = dao.get(cmsTextI18nCategory.getId());
        assertNotNull(cmsTextI18nCategoryResult);
        assertEquals((long) cmsTextI18nCategory.getId(), (long) cmsTextI18nCategoryResult.getId());
        assertEquals("Unexpected status value for undeleted object", CmsTextI18nCategoryStatus.NEW, cmsTextI18nCategoryResult.getStatus());
    }
}
