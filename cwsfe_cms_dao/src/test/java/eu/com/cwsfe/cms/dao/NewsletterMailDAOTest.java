package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.NewsletterMailStatus;
import eu.com.cwsfe.cms.model.NewsletterMail;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, NewsletterMailDAO.class, NewsletterMailGroupDAO.class, CmsLanguagesDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class NewsletterMailDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NewsletterMailDAO dao;
    @Autowired
    private NewsletterMailGroupDAO newsletterMailGroupDAO;
    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final NewsletterMailGroup RECIPIENT_GROUP = new NewsletterMailGroup();

    @Before
    public void setUp() throws Exception {
        RECIPIENT_GROUP.setLanguageId(cmsLanguagesDAO.getByCode("en").getId());
        RECIPIENT_GROUP.setName("recipientGroup");
        RECIPIENT_GROUP.setId(newsletterMailGroupDAO.add(RECIPIENT_GROUP));
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
    public void testListAjax() throws Exception {
        //given
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName("name1");
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject("subject1");
        newsletterMail.setMailContent("content1");
        dao.add(newsletterMail);
        NewsletterMail newsletterMail2 = new NewsletterMail();
        newsletterMail2.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail2.setName("name2");
        newsletterMail2.setSubject("subject2");
        newsletterMail2.setMailContent("content2");
        newsletterMail2.setStatus(NewsletterMailStatus.NEW);
        dao.add(newsletterMail2);

        //when
        List<NewsletterMail> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
        for (NewsletterMail nm : list) {
            assertNotNull(nm.getName());
            assertNotNull(nm.getRecipientGroupId());
            assertNotNull(nm.getStatus());
            assertNotNull(nm.getSubject());
            assertNotNull(nm.getMailContent());
        }
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        int iDisplayStart = 0;
        int iDisplayLength = 1;

        //when
        List<NewsletterMail> results = dao.searchByAjax(iDisplayStart, iDisplayLength, null, null);

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
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);
        dao.update(newsletterMail);

        //when
        NewsletterMail newsletterMailResult = dao.get(addedId);

        //then
        assertNotNull(newsletterMailResult);
        assertEquals((long) addedId, (long) newsletterMailResult.getId());
        assertEquals(name, newsletterMailResult.getName());
        assertEquals((long) RECIPIENT_GROUP.getId(), (long) newsletterMailResult.getRecipientGroupId());
        assertEquals(subject, newsletterMailResult.getSubject());
        assertEquals(NewsletterMailStatus.NEW, newsletterMailResult.getStatus());
        assertEquals(content, newsletterMailResult.getMailContent());
    }

    @Test
    public void testGetByName() throws Exception {
        //given
        String name = "name";
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);
        dao.update(newsletterMail);

        //when
        NewsletterMail newsletterMailResult = dao.getByName(name);

        //then
        assertNotNull(newsletterMailResult);
        assertEquals((long) addedId, (long) newsletterMailResult.getId());
        assertEquals(name, newsletterMailResult.getName());
        assertEquals((long) RECIPIENT_GROUP.getId(), (long) newsletterMailResult.getRecipientGroupId());
        assertEquals(subject, newsletterMailResult.getSubject());
        assertEquals(NewsletterMailStatus.NEW, newsletterMailResult.getStatus());
        assertEquals(content, newsletterMailResult.getMailContent());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String name = "name";
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);

        //when
        Long addedId = dao.add(newsletterMail);

        //then
        NewsletterMail newsletterMailResult = dao.get(addedId);
        assertNotNull(newsletterMailResult);
        assertEquals((long) addedId, (long) newsletterMailResult.getId());
        assertEquals(name, newsletterMailResult.getName());
        assertEquals((long) RECIPIENT_GROUP.getId(), (long) newsletterMailResult.getRecipientGroupId());
        assertEquals(subject, newsletterMailResult.getSubject());
        assertEquals(NewsletterMailStatus.NEW, newsletterMailResult.getStatus());
        assertEquals(content, newsletterMailResult.getMailContent());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String name = "name";
        String name2 = "name2";
        String subject = "subject";
        String subject2 = "subject2";
        String content = "content";
        String content2 = "content2";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);
        newsletterMail.setName(name2);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setSubject(subject2);
        newsletterMail.setMailContent(content2);
        dao.update(newsletterMail);

        //when
        NewsletterMail newsletterMailResult = dao.get(addedId);

        //then
        assertNotNull(newsletterMailResult);
        assertEquals((long) addedId, (long) newsletterMailResult.getId());
        assertEquals(name2, newsletterMailResult.getName());
        assertEquals((long) RECIPIENT_GROUP.getId(), (long) newsletterMailResult.getRecipientGroupId());
        assertEquals(subject2, newsletterMailResult.getSubject());
        assertEquals(NewsletterMailStatus.NEW, newsletterMailResult.getStatus());
        assertEquals(content2, newsletterMailResult.getMailContent());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String name = "name";
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);

        //when
        dao.delete(newsletterMail);

        //then
        NewsletterMail newsletterMailResult = dao.get(newsletterMail.getId());
        assertNotNull(newsletterMailResult);
        assertEquals((long) newsletterMail.getId(), (long) newsletterMailResult.getId());
        assertEquals("Unexpected status value for deleted object", NewsletterMailStatus.DELETED, newsletterMailResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String name = "name";
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);
        dao.delete(newsletterMail);

        //when
        dao.undelete(newsletterMail);

        //then
        NewsletterMail newsletterMailResult = dao.get(newsletterMail.getId());
        assertNotNull(newsletterMailResult);
        assertEquals((long) newsletterMail.getId(), (long) newsletterMailResult.getId());
        assertEquals("Unexpected status for undeleted object", NewsletterMailStatus.NEW, newsletterMailResult.getStatus());
    }

    @Test
    public void testSend() throws Exception {
        //given
        String name = "name";
        String subject = "subject";
        String content = "content";
        NewsletterMail newsletterMail = new NewsletterMail();
        newsletterMail.setName(name);
        newsletterMail.setRecipientGroupId(RECIPIENT_GROUP.getId());
        newsletterMail.setStatus(NewsletterMailStatus.NEW);
        newsletterMail.setSubject(subject);
        newsletterMail.setMailContent(content);
        Long addedId = dao.add(newsletterMail);
        newsletterMail.setId(addedId);

        //when
        dao.send(newsletterMail);

        //then
        NewsletterMail newsletterMailResult = dao.get(newsletterMail.getId());
        assertNotNull(newsletterMailResult);
        assertEquals((long) newsletterMail.getId(), (long) newsletterMailResult.getId());
        assertEquals("Unexpected status for sended object", NewsletterMailStatus.PREPARING, newsletterMailResult.getStatus());
    }
}
