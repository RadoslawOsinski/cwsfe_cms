package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.CmsNewsStatus;
import eu.com.cwsfe.cms.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Radosław Osiński
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsNewsDAO.class, NewsTypesDAO.class, CmsFoldersDAO.class, CmsAuthorsDAO.class, CmsLanguagesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsNewsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsNewsRepository dao;

    @Autowired
    private NewsTypesRepository newsTypesDAO;

    @Autowired
    private CmsFoldersRepository cmsFoldersDAO;

    @Autowired
    private CmsAuthorsRepository authorsDao;

    @Autowired
    private CmsLanguagesRepository cmsLanguagesDAO;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final Language LANGUAGE_EN = new Language();
    private static final NewsType NEWS_TYPE = new NewsType();
    private static final NewsType NEWS_TYPE2 = new NewsType();
    private static final CmsFolder FOLDER = new CmsFolder();
    private static final CmsFolder FOLDER2 = new CmsFolder();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        NEWS_TYPE.setType("type");
        NEWS_TYPE.setId(newsTypesDAO.add(NEWS_TYPE));

        NEWS_TYPE2.setType("type2");
        NEWS_TYPE2.setId(newsTypesDAO.add(NEWS_TYPE2));

        FOLDER.setFolderName("folder");
        FOLDER.setOrderNumber(1L);
        FOLDER.setId(cmsFoldersDAO.add(FOLDER));

        FOLDER2.setFolderName("folder2");
        FOLDER2.setOrderNumber(2L);
        FOLDER2.setId(cmsFoldersDAO.add(FOLDER2));

        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());
    }

    @Test
    public void testGetTotalNumberNotDeleted() throws Exception {
        //given
        //when
        int result = dao.getTotalNumberNotDeleted();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        int iDisplayStart = 0;
        int iDisplayLength = 1;

        //when
        List<Object[]> results = dao.searchByAjax(iDisplayStart, iDisplayLength, 1, "2");

        //then
        assertNotNull(results);
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        //when
        int result = dao.searchByAjaxCount(1, "2");

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testListAll() throws Exception {
        //given
        //when
        List<CmsNews> list = dao.listAll();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testGet() throws Exception {
        //given
        CmsNews cmsNews = addTestNews();

        //when
        CmsNews result = dao.get(cmsNews.getId());

        //then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreationDate());
        assertEquals(CMS_AUTHOR.getId(), result.getAuthorId());
        assertEquals(FOLDER.getId(), result.getNewsFolderId());
        assertEquals(NEWS_TYPE.getId(), result.getNewsTypeId());
        assertEquals("news text code", result.getNewsCode());
        assertEquals(CmsNewsStatus.NEW, result.getStatus());
    }

    @Test
    public void testGetByNewsTypeFolderAndNewsCodeForNotPublished() throws Exception {
        //given
        addTestNews();

        //when
        try {
            dao.getByNewsTypeFolderAndNewsCode(NEWS_TYPE.getId(), FOLDER.getId(), "news text code");
            fail("There should be no results for not published news");
        } catch (EmptyResultDataAccessException ignored) {
            //then
        }
    }

    @Test
    public void testGetByNewsTypeFolderAndNewsCodeForPublished() throws Exception {
        //given
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));
        news.setId(dao.add(news));
        dao.publish(news);

        //when
        CmsNews result = dao.getByNewsTypeFolderAndNewsCode(NEWS_TYPE.getId(), FOLDER.getId(), "news text code");

        //then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreationDate());
        assertEquals(CMS_AUTHOR.getId(), result.getAuthorId());
        assertEquals(FOLDER.getId(), result.getNewsFolderId());
        assertEquals(NEWS_TYPE.getId(), result.getNewsTypeId());
        assertEquals("news text code", result.getNewsCode());
        assertEquals(CmsNewsStatus.PUBLISHED, result.getStatus());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));

        //when
        news.setId(dao.add(news));

        //then
        CmsNews result = dao.get(news.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreationDate());
        assertEquals(CMS_AUTHOR.getId(), result.getAuthorId());
        assertEquals(FOLDER.getId(), result.getNewsFolderId());
        assertEquals(NEWS_TYPE.getId(), result.getNewsTypeId());
        assertEquals("news text code", result.getNewsCode());
        assertEquals(CmsNewsStatus.NEW, result.getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));
        news.setId(dao.add(news));
        news.setNewsCode("news text code 2");
        news.setStatus(CmsNewsStatus.PUBLISHED);

        //when
        dao.update(news);

        //then
        CmsNews result = dao.get(news.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreationDate());
        assertEquals(CMS_AUTHOR.getId(), result.getAuthorId());
        assertEquals(FOLDER.getId(), result.getNewsFolderId());
        assertEquals(NEWS_TYPE.getId(), result.getNewsTypeId());
        assertEquals("news text code 2", result.getNewsCode());
        assertEquals(CmsNewsStatus.PUBLISHED, result.getStatus());
    }

    @Test
    public void testUpdatePostBasicInfo() throws Exception {
        //given
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));
        news.setId(dao.add(news));
        news.setNewsCode("news text code 2");
        news.setNewsTypeId(NEWS_TYPE2.getId());
        news.setNewsFolderId(FOLDER2.getId());
        news.setStatus(CmsNewsStatus.PUBLISHED);

        //when
        dao.updatePostBasicInfo(news);

        //then
        CmsNews result = dao.get(news.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreationDate());
        assertEquals(CMS_AUTHOR.getId(), result.getAuthorId());
        assertEquals(FOLDER2.getId(), result.getNewsFolderId());
        assertEquals(NEWS_TYPE2.getId(), result.getNewsTypeId());
        assertEquals("news text code 2", result.getNewsCode());
        assertEquals(CmsNewsStatus.PUBLISHED, result.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        CmsNews cmsNews = addTestNews();

        //when
        dao.delete(cmsNews);

        //then
        CmsNews result = dao.get(cmsNews.getId());
        assertEquals(CmsNewsStatus.DELETED, result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        CmsNews cmsNews = addTestNews();
        dao.delete(cmsNews);

        //when
        dao.undelete(cmsNews);

        //then
        CmsNews result = dao.get(cmsNews.getId());
        assertEquals(CmsNewsStatus.HIDDEN, result.getStatus());
    }

    @Test
    public void testPublish() throws Exception {
        //given
        CmsNews cmsNews = addTestNews();

        //when
        dao.publish(cmsNews);

        //then
        CmsNews result = dao.get(cmsNews.getId());
        assertEquals(CmsNewsStatus.PUBLISHED, result.getStatus());
    }

    @Test
    public void testListByFolderLangAndNewsWithPagingForNotPublished() throws Exception {
        //given
        addTestNews();

        //when
        List<Object[]> list = dao.listByFolderLangAndNewsWithPaging(FOLDER.getId().intValue(), LANGUAGE_EN.getId(), NEWS_TYPE.getType(), 0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1 but news are not published", 0, list.size());
    }

    @Test
    public void testListByFolderLangAndNewsWithPagingForPublished() throws Exception {
        //given
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));
        news.setId(dao.add(news));
        dao.publish(news);

        //when
        List<Object[]> list = dao.listByFolderLangAndNewsWithPaging(FOLDER.getId().intValue(), LANGUAGE_EN.getId(), NEWS_TYPE.getType(), 0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1 but news are not published", 0, list.size());
    }

    @Test
    public void testCountListByFolderLangAndNewsWithPaging() throws Exception {
        //given

        //when
        int result = dao.countListByFolderLangAndNewsWithPaging(1, 2L, "3");

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    private CmsNews addTestNews() {
        CmsNews news = new CmsNews();
        news.setAuthorId(CMS_AUTHOR.getId());
        news.setNewsCode("news text code");
        news.setStatus(CmsNewsStatus.NEW);
        news.setNewsTypeId(NEWS_TYPE.getId());
        news.setNewsFolderId(FOLDER.getId());
        news.setCreationDate(new Date(4));
        news.setId(dao.add(news));
        return news;
    }

}
