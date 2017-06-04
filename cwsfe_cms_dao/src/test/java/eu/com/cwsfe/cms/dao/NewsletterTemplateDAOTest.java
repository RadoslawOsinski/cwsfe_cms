package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.NewsletterTemplateStatus;
import eu.com.cwsfe.cms.model.NewsletterTemplate;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, NewsletterTemplateDAO.class, CmsLanguagesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class NewsletterTemplateDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NewsletterTemplateRepository dao;
    @Autowired
    private CmsLanguagesRepository cmsLanguagesDAO;

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
        List<NewsletterTemplate> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName("name1");
        newsletterTemplate.setLanguageId(cmsLanguagesDAO.getByCode("en").getId());
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        dao.add(newsletterTemplate);
        NewsletterTemplate newsletterTemplate2 = new NewsletterTemplate();
        newsletterTemplate2.setLanguageId(cmsLanguagesDAO.getByCode("en").getId());
        newsletterTemplate2.setName("name2");
        newsletterTemplate2.setStatus(NewsletterTemplateStatus.NEW);
        dao.add(newsletterTemplate2);

        //when
        List<NewsletterTemplate> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
        for (NewsletterTemplate nt : list) {
            assertNotNull(nt.getLanguageId());
            assertNotNull(nt.getStatus());
            assertNotNull(nt.getSubject());
            assertNotNull(nt.getContent());
        }
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        int iDisplayStart = 0;
        int iDisplayLength = 1;

        //when
        List<NewsletterTemplate> results = dao.searchByAjax(iDisplayStart, iDisplayLength, null, null);

        //then
        assertNotNull(results);
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        //when
        int result = dao.searchByAjaxCount(null, null);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGet() throws Exception {
        //given
        String name = "name";
        Long languageId = 1L;
        String subject = "subject";
        String content = "content";
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplate.setSubject(subject);
        newsletterTemplate.setContent(content);
        Long addedId = dao.add(newsletterTemplate);
        newsletterTemplate.setId(addedId);
        dao.update(newsletterTemplate);

        //when
        NewsletterTemplate newsletterTemplateResult = dao.get(addedId);

        //then
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) addedId, (long) newsletterTemplateResult.getId());
        assertEquals(name, newsletterTemplateResult.getName());
        assertEquals(languageId, newsletterTemplateResult.getLanguageId());
        assertEquals(NewsletterTemplateStatus.NEW, newsletterTemplateResult.getStatus());
        assertEquals(subject, newsletterTemplateResult.getSubject());
        assertEquals(content, newsletterTemplateResult.getContent());
    }

    @Test
    public void testGetByName() throws Exception {
        //given
        String name = "name";
        Long languageId = 1L;
        String subject = "subject";
        String content = "content";
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplate.setSubject(subject);
        newsletterTemplate.setContent(content);
        Long addedId = dao.add(newsletterTemplate);
        newsletterTemplate.setId(addedId);
        dao.update(newsletterTemplate);

        //when
        NewsletterTemplate newsletterTemplateResult = dao.getByName(name);

        //then
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) addedId, (long) newsletterTemplateResult.getId());
        assertEquals(name, newsletterTemplateResult.getName());
        assertEquals(languageId, newsletterTemplateResult.getLanguageId());
        assertEquals(NewsletterTemplateStatus.NEW, newsletterTemplateResult.getStatus());
        assertEquals(subject, newsletterTemplateResult.getSubject());
        assertEquals(content, newsletterTemplateResult.getContent());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String name = "name";
        Long languageId = 1L;
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);

        //when
        Long addedId = dao.add(newsletterTemplate);

        //then
        NewsletterTemplate newsletterTemplateResult = dao.get(addedId);
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) addedId, (long) newsletterTemplateResult.getId());
        assertEquals(name, newsletterTemplateResult.getName());
        assertEquals(languageId, newsletterTemplateResult.getLanguageId());
        assertEquals(NewsletterTemplateStatus.NEW, newsletterTemplateResult.getStatus());
        assertEquals("", newsletterTemplateResult.getSubject());
        assertEquals("", newsletterTemplateResult.getContent());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String name = "name";
        String name2 = "name2";
        Long languageId = cmsLanguagesDAO.getByCode("en").getId();
        Long languageId2 = cmsLanguagesDAO.getByCode("en").getId();
        String subject = "subject";
        String subject2 = "subject2";
        String content = "content";
        String content2 = "content2";
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplate.setSubject(subject);
        newsletterTemplate.setContent(content);
        newsletterTemplate.setId(dao.add(newsletterTemplate));
        newsletterTemplate.setName(name2);
        newsletterTemplate.setLanguageId(languageId2);
        newsletterTemplate.setSubject(subject2);
        newsletterTemplate.setContent(content2);

        //when
        dao.update(newsletterTemplate);

        //then
        NewsletterTemplate newsletterTemplateResult = dao.get(newsletterTemplate.getId());
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) newsletterTemplate.getId(), (long) newsletterTemplateResult.getId());
        assertEquals(name2, newsletterTemplateResult.getName());
        assertEquals(languageId2, newsletterTemplateResult.getLanguageId());
        assertEquals(NewsletterTemplateStatus.NEW, newsletterTemplateResult.getStatus());
        assertEquals(subject2, newsletterTemplateResult.getSubject());
        assertEquals(content2, newsletterTemplateResult.getContent());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String name = "name";
        Long languageId = cmsLanguagesDAO.getByCode("en").getId();
        String subject = "subject";
        String content = "content";
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplate.setSubject(subject);
        newsletterTemplate.setContent(content);
        newsletterTemplate.setId(dao.add(newsletterTemplate));

        //when
        dao.delete(newsletterTemplate);

        //then
        NewsletterTemplate newsletterTemplateResult = dao.get(newsletterTemplate.getId());
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) newsletterTemplate.getId(), (long) newsletterTemplateResult.getId());
        assertEquals("Unexpected status value for deleted object", NewsletterTemplateStatus.DELETED, newsletterTemplateResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String name = "name";
        Long languageId = cmsLanguagesDAO.getByCode("en").getId();
        String subject = "subject";
        String content = "content";
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setName(name);
        newsletterTemplate.setLanguageId(languageId);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplate.setSubject(subject);
        newsletterTemplate.setContent(content);
        newsletterTemplate.setId(dao.add(newsletterTemplate));
        dao.delete(newsletterTemplate);

        //when
        dao.undelete(newsletterTemplate);

        //then
        NewsletterTemplate newsletterTemplateResult = dao.get(newsletterTemplate.getId());
        assertNotNull(newsletterTemplateResult);
        assertEquals((long) newsletterTemplate.getId(), (long) newsletterTemplateResult.getId());
        assertEquals("Unexpected status for undeleted object", NewsletterTemplateStatus.NEW, newsletterTemplateResult.getStatus());
    }
}
