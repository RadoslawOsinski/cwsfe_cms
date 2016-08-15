package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.domains.NewsTypeStatus;
import eu.com.cwsfe.cms.model.NewsType;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, NewsTypesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class NewsTypesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NewsTypesDAO dao;

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
        List<NewsType> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        NewsType newsType = new NewsType();
        newsType.setType("test1");
        newsType.setStatus(NewsTypeStatus.NEW);
        dao.add(newsType);
        NewsType newsType2 = new NewsType();
        newsType2.setType("test2");
        newsType2.setStatus(NewsTypeStatus.NEW);
        dao.add(newsType2);

        //when
        List<NewsType> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListNewsTypesForDropList() throws Exception {
        //given
        String type = "test";
        String type2 = "test2";
        NewsType newsType = new NewsType();
        newsType.setType(type);
        newsType.setStatus(NewsTypeStatus.NEW);
        dao.add(newsType);
        newsType.setType(type2);
        dao.add(newsType);

        //when
        List<NewsType> newsTypes = dao.listNewsTypesForDropList(type, 1);

        //then
        assertNotNull(newsTypes);
        assertEquals("Page limit was set to 1", 1, newsTypes.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String type = "test";
        NewsType newsType = new NewsType();
        newsType.setType(type);
        newsType.setStatus(NewsTypeStatus.NEW);
        Long addedId = dao.add(newsType);

        //when
        NewsType newsTypeResult = dao.get(addedId);

        //then
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type, newsTypeResult.getType());
        assertEquals(NewsTypeStatus.NEW, newsTypeResult.getStatus());
    }

    @Test
    public void testGetByFolderName() throws Exception {
        //given
        String type = "test";
        NewsType newsType = new NewsType();
        newsType.setType(type);
        newsType.setStatus(NewsTypeStatus.NEW);
        Long addedId = dao.add(newsType);

        //when
        NewsType newsTypeResult = dao.getByType(type);

        //then
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type, newsTypeResult.getType());
        assertEquals(NewsTypeStatus.NEW, newsTypeResult.getStatus());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String type = "test";
        NewsType newsType = new NewsType();
        newsType.setType(type);
        newsType.setStatus(NewsTypeStatus.NEW);

        //when
        Long addedId = dao.add(newsType);

        //then
        NewsType newsTypeResult = dao.get(addedId);
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type, newsTypeResult.getType());
        assertEquals(NewsTypeStatus.NEW, newsTypeResult.getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String type1 = "test";
        String type2 = "test2";
        NewsType newsType = new NewsType();
        newsType.setType(type1);
        newsType.setStatus(NewsTypeStatus.NEW);
        Long addedId = dao.add(newsType);
        newsType.setId(addedId);
        newsType.setType(type2);

        //when
        dao.update(newsType);

        //then
        NewsType newsTypeResult = dao.get(addedId);
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type2, newsTypeResult.getType());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String type1 = "test";
        NewsType newsType = new NewsType();
        newsType.setType(type1);
        newsType.setStatus(NewsTypeStatus.NEW);
        Long addedId = dao.add(newsType);
        newsType.setId(addedId);

        //when
        dao.delete(newsType);

        //then
        NewsType newsTypeResult = dao.get(addedId);
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type1, newsTypeResult.getType());
        assertEquals("Unexpected status value for deleted object", NewsTypeStatus.DELETED, newsTypeResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String type1 = "test";
        NewsType newsType = new NewsType();
        newsType.setType(type1);
        newsType.setStatus(NewsTypeStatus.NEW);
        Long addedId = dao.add(newsType);
        newsType.setId(addedId);
        dao.delete(newsType);

        //when
        dao.undelete(newsType);

        //then
        NewsType newsTypeResult = dao.get(addedId);
        assertNotNull(newsTypeResult);
        assertEquals((long) addedId, (long) newsTypeResult.getId());
        assertEquals(type1, newsTypeResult.getType());
        assertEquals("Unexpected status for undeleted object", NewsTypeStatus.NEW, newsTypeResult.getStatus());
    }
}