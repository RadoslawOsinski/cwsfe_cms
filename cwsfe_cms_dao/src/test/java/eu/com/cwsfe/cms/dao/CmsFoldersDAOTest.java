package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.domains.CmsFolderStatus;
import eu.com.cwsfe.cms.model.CmsFolder;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsFoldersDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsFoldersDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsFoldersDAO dao;

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
        List<CmsFolder> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        dao.add(cmsFolder);

        //when
        List<CmsFolder> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListFoldersForDropList() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        dao.add(cmsFolder);

        //when
        List<CmsFolder> results = dao.listFoldersForDropList(folderName, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        cmsFolder.setId(dao.add(cmsFolder));

        //when
        CmsFolder cmsRoleResult = dao.get(cmsFolder.getId());

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsRoleResult.getId());
        assertEquals(folderName, cmsRoleResult.getFolderName());
        assertEquals(orderNumber, (long) cmsRoleResult.getOrderNumber());
    }

    @Test
    public void testGetByFolderName() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        cmsFolder.setId(dao.add(cmsFolder));

        //when
        CmsFolder cmsRoleResult = dao.getByFolderName(cmsFolder.getFolderName());

        //then
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsRoleResult.getId());
        assertEquals(folderName, cmsRoleResult.getFolderName());
        assertEquals(orderNumber, (long) cmsRoleResult.getOrderNumber());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);

        //when
        cmsFolder.setId(dao.add(cmsFolder));

        //then
        CmsFolder cmsRoleResult = dao.get(cmsFolder.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsRoleResult.getId());
        assertEquals(folderName, cmsRoleResult.getFolderName());
        assertEquals(orderNumber, (long) cmsRoleResult.getOrderNumber());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String folderName = "test";
        String newFolderName = "test2";
        long orderNumber = 1L;
        long newOrderNumber = 2L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        cmsFolder.setId(dao.add(cmsFolder));
        cmsFolder.setFolderName(newFolderName);
        cmsFolder.setOrderNumber(newOrderNumber);

        //when
        dao.update(cmsFolder);

        //then
        CmsFolder cmsRoleResult = dao.get(cmsFolder.getId());
        assertNotNull(cmsRoleResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsRoleResult.getId());
        assertEquals(newFolderName, cmsRoleResult.getFolderName());
        assertEquals(newOrderNumber, (long) cmsRoleResult.getOrderNumber());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        cmsFolder.setId(dao.add(cmsFolder));

        //when
        dao.delete(cmsFolder);

        //then
        CmsFolder cmsFolderResult = dao.get(cmsFolder.getId());
        assertNotNull(cmsFolderResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsFolderResult.getId());
        assertEquals("Unexpected status value for deleted object", CmsFolderStatus.DELETED, cmsFolderResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String folderName = "test";
        long orderNumber = 1L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setFolderName(folderName);
        cmsFolder.setOrderNumber(orderNumber);
        cmsFolder.setId(dao.add(cmsFolder));
        dao.delete(cmsFolder);

        //when
        dao.undelete(cmsFolder);

        //then
        CmsFolder cmsFolderResult = dao.get(cmsFolder.getId());
        assertNotNull(cmsFolderResult);
        assertEquals((long) cmsFolder.getId(), (long) cmsFolderResult.getId());
        assertEquals("Unexpected status value for undeleted object", CmsFolderStatus.NEW, cmsFolderResult.getStatus());
    }
}
