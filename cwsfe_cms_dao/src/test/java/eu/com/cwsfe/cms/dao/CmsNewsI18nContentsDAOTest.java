package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.CmsNewsI18nContentStatus;
import eu.com.cwsfe.cms.db.domains.CmsNewsStatus;
import eu.com.cwsfe.cms.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsNewsI18nContentsDAO.class,
    CmsNewsDAO.class, NewsTypesDAO.class, CmsFoldersDAO.class, CmsAuthorsDAO.class, CmsLanguagesDAO.class
})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsNewsI18nContentsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsNewsI18nContentsRepository dao;

    @Autowired
    private CmsNewsRepository newsDao;

    @Autowired
    private NewsTypesRepository newsTypesDAO;

    @Autowired
    private CmsFoldersRepository cmsFoldersDAO;

    @Autowired
    private CmsAuthorsRepository authorsDao;

    @Autowired
    private CmsLanguagesRepository cmsLanguagesDAO;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final NewsType NEWS_TYPE = new NewsType();
    private static final CmsFolder FOLDER = new CmsFolder();
    private static final CmsNews NEWS = new CmsNews();
    private static final Language LANGUAGE_EN = new Language();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        NEWS_TYPE.setType("type");
        NEWS_TYPE.setId(newsTypesDAO.add(NEWS_TYPE));

        FOLDER.setFolderName("folder");
        FOLDER.setOrderNumber(1L);
        FOLDER.setId(cmsFoldersDAO.add(FOLDER));

        NEWS.setAuthorId(CMS_AUTHOR.getId());
        NEWS.setNewsCode("news text code");
        NEWS.setStatus(CmsNewsStatus.NEW);
        NEWS.setNewsTypeId(NEWS_TYPE.getId());
        NEWS.setNewsFolderId(FOLDER.getId());
        NEWS.setCreationDate(new Date(4));
        NEWS.setId(newsDao.add(NEWS));

        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());
    }

    @Test
    public void testList() throws Exception {
        //given
        //when
        List<CmsNewsI18nContent> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListForNews() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        dao.add(cmsNewsI18nContent);

        //when
        List<CmsNewsI18nContent> cmsNewsI18nContents = dao.listForNews(NEWS.getId());

        //then
        assertNotNull(cmsNewsI18nContents);
        assertFalse("Should exist", cmsNewsI18nContents.isEmpty());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        Long addedId = dao.add(cmsNewsI18nContent);

        //when
        CmsNewsI18nContent result = dao.get(addedId);

        //then
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getNewsTitle());
        assertEquals(shortcut, result.getNewsShortcut());
        assertEquals(description, result.getNewsDescription());
    }

    @Test
    public void testGetByLanguageForNews() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        dao.add(cmsNewsI18nContent);

        //when
        CmsNewsI18nContent result = dao.getByLanguageForNews(NEWS.getId(), LANGUAGE_EN.getId());

        //then
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getNewsTitle());
        assertEquals(shortcut, result.getNewsShortcut());
        assertEquals(description, result.getNewsDescription());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);

        //when
        Long addedId = dao.add(cmsNewsI18nContent);

        //then
        CmsNewsI18nContent result = dao.get(addedId);
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getNewsTitle());
        assertEquals(shortcut, result.getNewsShortcut());
        assertEquals(description, result.getNewsDescription());
    }

    @Test
    public void testUpdateContentWithStatus() throws Exception {
        //given
        String title = "title";
        String newTitle = "new title";
        String shortcut = "shortcut";
        String newShortcut = "new shortcut";
        String description = "description";
        String newDescription = "new description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setId(dao.add(cmsNewsI18nContent));
        cmsNewsI18nContent.setNewsTitle(newTitle);
        cmsNewsI18nContent.setNewsShortcut(newShortcut);
        cmsNewsI18nContent.setNewsDescription(newDescription);
        cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.DELETED);

        //when
        dao.updateContentWithStatus(cmsNewsI18nContent);

        //then
        CmsNewsI18nContent result = dao.get(cmsNewsI18nContent.getId());
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(newTitle, result.getNewsTitle());
        assertEquals(newShortcut, result.getNewsShortcut());
        assertEquals(newDescription, result.getNewsDescription());
        assertEquals(CmsNewsI18nContentStatus.DELETED, result.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setId(dao.add(cmsNewsI18nContent));

        //when
        dao.delete(cmsNewsI18nContent);

        //then
        CmsNewsI18nContent result = dao.get(cmsNewsI18nContent.getId());
        assertNotNull("News i18n should exists", result);
        assertEquals("News i18n should be deleted", CmsNewsI18nContentStatus.DELETED, result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setId(dao.add(cmsNewsI18nContent));

        //when
        dao.undelete(cmsNewsI18nContent);

        //then
        CmsNewsI18nContent result = dao.get(cmsNewsI18nContent.getId());
        assertNotNull("News i18n should exists", result);
        assertEquals("News i18n should be hidden", CmsNewsI18nContentStatus.HIDDEN, result.getStatus());
    }

    @Test
    public void testPublish() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setNewsId(NEWS.getId());
        cmsNewsI18nContent.setLanguageId(LANGUAGE_EN.getId());
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setId(dao.add(cmsNewsI18nContent));

        //when
        dao.publish(cmsNewsI18nContent);

        //then
        CmsNewsI18nContent result = dao.get(cmsNewsI18nContent.getId());
        assertNotNull("News i18n should exists", result);
        assertEquals("News i18n should be published", CmsNewsI18nContentStatus.PUBLISHED, result.getStatus());
    }
}
