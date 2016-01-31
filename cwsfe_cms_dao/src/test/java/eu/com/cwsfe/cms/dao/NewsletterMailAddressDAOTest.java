package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.NewsletterMailAddressStatus;
import eu.com.cwsfe.cms.model.Language;
import eu.com.cwsfe.cms.model.NewsletterMailAddress;
import eu.com.cwsfe.cms.model.NewsletterMailGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class NewsletterMailAddressDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NewsletterMailAddressDAO dao;
    
    @Autowired
    private NewsletterMailGroupDAO newsletterMailGroupDAO;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final NewsletterMailGroup MAIL_GROUP = new NewsletterMailGroup();
    private static final NewsletterMailGroup MAIL_GROUP2 = new NewsletterMailGroup();
    private static final Language LANGUAGE_EN = new Language();

    @Before
    public void setUp() throws Exception {
        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());

        MAIL_GROUP.setLanguageId(LANGUAGE_EN.getId());
        MAIL_GROUP.setName("mailGroup");
        MAIL_GROUP.setId(newsletterMailGroupDAO.add(MAIL_GROUP));

        MAIL_GROUP2.setLanguageId(LANGUAGE_EN.getId());
        MAIL_GROUP2.setName("mailGroup2");
        MAIL_GROUP2.setId(newsletterMailGroupDAO.add(MAIL_GROUP2));
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
        List<NewsletterMailAddress> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListByRecipientGroup() throws Exception {
        //given
        //when
        List<NewsletterMailAddress> list = dao.listByRecipientGroup(1L);

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail("test1@test.pl");
        newsletterMailAddress.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString("confirmString1");
        newsletterMailAddress.setUnSubscribeString("unSubscribeString1");
        dao.add(newsletterMailAddress);
        NewsletterMailAddress newsletterMailAddress2 = new NewsletterMailAddress();
        newsletterMailAddress2.setEmail("test2@test.pl");
        newsletterMailAddress2.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress2.setMailGroupId(MAIL_GROUP2.getId());
        newsletterMailAddress2.setConfirmString("confirmString2");
        newsletterMailAddress2.setUnSubscribeString("unSubscribeString2");
        dao.add(newsletterMailAddress2);

        //when
        List<NewsletterMailAddress> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail("test1@test.pl");
        newsletterMailAddress.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString("confirmString1");
        newsletterMailAddress.setUnSubscribeString("unSubscribeString1");
        dao.add(newsletterMailAddress);
        NewsletterMailAddress newsletterMailAddress2 = new NewsletterMailAddress();
        newsletterMailAddress2.setEmail("test2@test.pl");
        newsletterMailAddress2.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress2.setMailGroupId(MAIL_GROUP2.getId());
        newsletterMailAddress2.setConfirmString("confirmString2");
        newsletterMailAddress2.setUnSubscribeString("unSubscribeString2");
        dao.add(newsletterMailAddress2);

        //when
        List<NewsletterMailAddress> list = dao.searchByAjax(0, 1, newsletterMailAddress.getEmail(), newsletterMailAddress.getMailGroupId());

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail("test1@test.pl");
        newsletterMailAddress.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString("confirmString1");
        newsletterMailAddress.setUnSubscribeString("unSubscribeString1");
        dao.add(newsletterMailAddress);
        NewsletterMailAddress newsletterMailAddress2 = new NewsletterMailAddress();
        newsletterMailAddress2.setEmail("test2@test.pl");
        newsletterMailAddress2.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddress2.setMailGroupId(MAIL_GROUP2.getId());
        newsletterMailAddress2.setConfirmString("confirmString2");
        newsletterMailAddress2.setUnSubscribeString("unSubscribeString2");
        dao.add(newsletterMailAddress2);

        //when
        int result = dao.searchByAjaxCount(newsletterMailAddress.getEmail(), newsletterMailAddress.getMailGroupId());

        //then
        assertNotNull("Results should not return null", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGet() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);

        //when
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);

        //then
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(email, newsletterMailAddressResult.getEmail());
        assertEquals(confirmString, newsletterMailAddressResult.getConfirmString());
        assertEquals(unSubscribeString, newsletterMailAddressResult.getUnSubscribeString());
    }

    @Test
    public void testGetByEmailAndMailGroup() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);

        //when
        NewsletterMailAddress newsletterMailAddressResult = dao.getByEmailAndMailGroup(email, MAIL_GROUP.getId());

        //then
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(email, newsletterMailAddressResult.getEmail());
        assertEquals(confirmString, newsletterMailAddressResult.getConfirmString());
        assertEquals(unSubscribeString, newsletterMailAddressResult.getUnSubscribeString());
    }

    @Test
    public void testGetByConfirmString() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);

        //when
        NewsletterMailAddress newsletterMailAddressResult = dao.getByConfirmString(confirmString);

        //then
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(email, newsletterMailAddressResult.getEmail());
        assertEquals(confirmString, newsletterMailAddressResult.getConfirmString());
        assertEquals(unSubscribeString, newsletterMailAddressResult.getUnSubscribeString());
    }

    @Test
    public void testGetByUnSubscribeString() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);

        //when
        NewsletterMailAddress newsletterMailAddressResult = dao.getByUnSubscribeString(unSubscribeString);

        //then
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(email, newsletterMailAddressResult.getEmail());
        assertEquals(confirmString, newsletterMailAddressResult.getConfirmString());
        assertEquals(unSubscribeString, newsletterMailAddressResult.getUnSubscribeString());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);

        //when
        Long addedId = dao.add(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(email, newsletterMailAddressResult.getEmail());
        assertEquals(confirmString, newsletterMailAddressResult.getConfirmString());
        assertEquals(unSubscribeString, newsletterMailAddressResult.getUnSubscribeString());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String email = "test1@test.pl";
        String newEmail = "test2@test2.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);
        newsletterMailAddress.setId(addedId);
        newsletterMailAddress.setEmail(newEmail);

        //when
        dao.update(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals(newEmail, newsletterMailAddressResult.getEmail());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);
        newsletterMailAddress.setId(addedId);

        //when
        dao.delete(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals("Unexpected status value for deleted object", NewsletterMailAddressStatus.DELETED, newsletterMailAddressResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);
        newsletterMailAddress.setId(addedId);
        dao.delete(newsletterMailAddress);

        //when
        dao.undelete(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals("Unexpected status for undeleted object", NewsletterMailAddressStatus.NEW, newsletterMailAddressResult.getStatus());
    }

    @Test
    public void testActivate() throws Exception {
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);
        newsletterMailAddress.setId(addedId);

        //when
        dao.activate(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals("Unexpected status for active object", NewsletterMailAddressStatus.ACTIVE, newsletterMailAddressResult.getStatus());
    }

    @Test
    public void testDeactivate() throws Exception {
        String email = "test1@test.pl";
        String confirmString = "confirmString";
        String unSubscribeString = "unSubscribeString";
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setMailGroupId(MAIL_GROUP.getId());
        newsletterMailAddress.setConfirmString(confirmString);
        newsletterMailAddress.setUnSubscribeString(unSubscribeString);
        Long addedId = dao.add(newsletterMailAddress);
        newsletterMailAddress.setId(addedId);

        //when
        dao.deactivate(newsletterMailAddress);

        //then
        NewsletterMailAddress newsletterMailAddressResult = dao.get(addedId);
        assertNotNull(newsletterMailAddressResult);
        assertEquals((long) addedId, (long) newsletterMailAddressResult.getId());
        assertEquals("Unexpected status for deactivated object", NewsletterMailAddressStatus.INACTIVE, newsletterMailAddressResult.getStatus());
    }
}