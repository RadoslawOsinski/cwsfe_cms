package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.NewsletterMailGroupStatus;
import eu.com.cwsfe.cms.model.Language;
import eu.com.cwsfe.cms.model.NewsletterMailGroup;
import org.junit.Before;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, NewsletterMailGroupDAO.class, CmsLanguagesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class NewsletterMailGroupDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NewsletterMailGroupDAO dao;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final Language LANGUAGE_EN = new Language();

    @Before
    public void setUp() throws Exception {
        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());
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
        List<NewsletterMailGroup> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListNewsletterMailGroupsForDropList() throws Exception {
        //given
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        String name = "name";
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup);

        //when
        List<NewsletterMailGroup> results = dao.listNewsletterMailGroupsForDropList(name, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String name = "name";
        String name2 = "name2";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup);
        NewsletterMailGroup newsletterMailGroup2 = new NewsletterMailGroup();
        newsletterMailGroup2.setName(name2);
        newsletterMailGroup2.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup2);

        //when
        List<NewsletterMailGroup> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        String name = "name";
        String otherName = "otherName";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup);
        NewsletterMailGroup newsletterMailGroup2 = new NewsletterMailGroup();
        newsletterMailGroup2.setName(otherName);
        newsletterMailGroup2.setStatus(NewsletterMailGroupStatus.NEW);
        newsletterMailGroup2.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup2);

        //when
        List<NewsletterMailGroup> list = dao.searchByAjax(0, 1, newsletterMailGroup.getName(), LANGUAGE_EN.getId());

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        String name = "name";
        String otherName = "otherName";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup);
        NewsletterMailGroup newsletterMailGroup2 = new NewsletterMailGroup();
        newsletterMailGroup2.setName(otherName);
        newsletterMailGroup2.setLanguageId(LANGUAGE_EN.getId());
        dao.add(newsletterMailGroup2);

        //when
        int result = dao.searchByAjaxCount(newsletterMailGroup.getName(), newsletterMailGroup.getLanguageId());

        //then
        assertNotNull("Results should not return null", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGet() throws Exception {
        //given
        String name = "name";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        Long addedId = dao.add(newsletterMailGroup);

        //when
        NewsletterMailGroup newsletterMailGroupResult = dao.get(addedId);

        //then
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals(name, newsletterMailGroupResult.getName());
        assertEquals(LANGUAGE_EN.getId(), newsletterMailGroupResult.getLanguageId());
    }

    @Test
    public void testGetByNameAndLanguage() throws Exception {
        //given
        String name = "name";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        Long addedId = dao.add(newsletterMailGroup);

        //when
        NewsletterMailGroup newsletterMailGroupResult = dao.getByNameAndLanguage(name, LANGUAGE_EN.getId());

        //then
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals(name, newsletterMailGroupResult.getName());
        assertEquals(LANGUAGE_EN.getId(), newsletterMailGroupResult.getLanguageId());

    }

    @Test
    public void testAdd() throws Exception {
        //given
        String name = "name";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());

        //when
        Long addedId = dao.add(newsletterMailGroup);

        //then
        NewsletterMailGroup newsletterMailGroupResult = dao.get(addedId);
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals(name, newsletterMailGroupResult.getName());
        assertEquals(LANGUAGE_EN.getId(), newsletterMailGroupResult.getLanguageId());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String name = "name";
        String otherName = "otherName";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        Long addedId = dao.add(newsletterMailGroup);
        newsletterMailGroup.setId(addedId);
        newsletterMailGroup.setName(otherName);

        //when
        dao.update(newsletterMailGroup);

        //then
        NewsletterMailGroup newsletterMailGroupResult = dao.get(addedId);
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals(otherName, newsletterMailGroupResult.getName());
        assertEquals(LANGUAGE_EN.getId(), newsletterMailGroupResult.getLanguageId());

    }

    @Test
    public void testDelete() throws Exception {
        //given
        String name = "name";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        Long addedId = dao.add(newsletterMailGroup);
        newsletterMailGroup.setId(addedId);

        //when
        dao.delete(newsletterMailGroup);

        //then
        NewsletterMailGroup newsletterMailGroupResult = dao.get(addedId);
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals("Unexpected status value for deleted object", NewsletterMailGroupStatus.DELETED, newsletterMailGroupResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        String name = "name";
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setName(name);
        newsletterMailGroup.setLanguageId(LANGUAGE_EN.getId());
        Long addedId = dao.add(newsletterMailGroup);
        newsletterMailGroup.setId(addedId);

        //when
        dao.undelete(newsletterMailGroup);

        //then
        NewsletterMailGroup newsletterMailGroupResult = dao.get(addedId);
        assertNotNull(newsletterMailGroupResult);
        assertEquals((long) addedId, (long) newsletterMailGroupResult.getId());
        assertEquals("Unexpected status for undeleted object", NewsletterMailGroupStatus.NEW, newsletterMailGroupResult.getStatus());
    }
}
