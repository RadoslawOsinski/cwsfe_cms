package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.domains.CmsAuthorStatus;
import eu.com.cwsfe.cms.model.CmsAuthor;
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
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsAuthorsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsAuthorsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsAuthorsDAO dao;

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
        List<CmsAuthor> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        dao.add(cmsAuthor);

        //when
        List<CmsAuthor> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListAuthorsForDropList() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        dao.add(cmsAuthor);

        //when
        List<CmsAuthor> results = dao.listAuthorsForDropList(firstName, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        cmsAuthor.setId(dao.add(cmsAuthor));

        //when
        CmsAuthor cmsAuthorResult = dao.get(cmsAuthor.getId());

        //then
        assertNotNull(cmsAuthorResult);
        assertEquals((long) cmsAuthor.getId(), (long) cmsAuthorResult.getId());
        assertEquals(firstName, cmsAuthorResult.getFirstName());
        assertEquals(lastName, cmsAuthorResult.getLastName());
        assertEquals(googlePlusAuthorLink, cmsAuthorResult.getGooglePlusAuthorLink());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);

        //when
        cmsAuthor.setId(dao.add(cmsAuthor));

        //then
        CmsAuthor cmsAuthorResult = dao.get(cmsAuthor.getId());
        assertNotNull(cmsAuthorResult);
        assertEquals((long) cmsAuthor.getId(), (long) cmsAuthorResult.getId());
        assertEquals(firstName, cmsAuthorResult.getFirstName());
        assertEquals(lastName, cmsAuthorResult.getLastName());
        assertEquals(googlePlusAuthorLink, cmsAuthorResult.getGooglePlusAuthorLink());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String firstName = "firstName";
        String newFirstName = "firstName2";
        String lastName = "lastName";
        String newLastName = "lastName2";
        String googlePlusAuthorLink = "http://google...";
        String newGooglePlusAuthorLink = "http://google2...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        cmsAuthor.setId(dao.add(cmsAuthor));
        cmsAuthor.setFirstName(newFirstName);
        cmsAuthor.setLastName(newLastName);
        cmsAuthor.setGooglePlusAuthorLink(newGooglePlusAuthorLink);

        //when
        dao.update(cmsAuthor);

        //then
        CmsAuthor cmsAuthorResult = dao.get(cmsAuthor.getId());
        assertNotNull(cmsAuthorResult);
        assertEquals((long) cmsAuthor.getId(), (long) cmsAuthorResult.getId());
        assertEquals(newFirstName, cmsAuthorResult.getFirstName());
        assertEquals(newLastName, cmsAuthorResult.getLastName());
        assertEquals(newGooglePlusAuthorLink, cmsAuthorResult.getGooglePlusAuthorLink());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        cmsAuthor.setId(dao.add(cmsAuthor));

        //when
        dao.delete(cmsAuthor);

        //then
        CmsAuthor cmsAuthorResult = dao.get(cmsAuthor.getId());
        assertNotNull(cmsAuthorResult);
        assertEquals((long) cmsAuthor.getId(), (long) cmsAuthorResult.getId());
        assertEquals("Unexpected status value for deleted object", CmsAuthorStatus.DELETED, cmsAuthorResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String firstName = "firstName";
        String lastName = "lastName";
        String googlePlusAuthorLink = "http://google...";
        CmsAuthor cmsAuthor = new CmsAuthor();
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        cmsAuthor.setId(dao.add(cmsAuthor));
        dao.delete(cmsAuthor);

        //when
        dao.undelete(cmsAuthor);

        //then
        CmsAuthor cmsAuthorResult = dao.get(cmsAuthor.getId());
        assertNotNull(cmsAuthorResult);
        assertEquals((long) cmsAuthor.getId(), (long) cmsAuthorResult.getId());
        assertEquals("Unexpected status value for undeleted object", CmsAuthorStatus.NEW, cmsAuthorResult.getStatus());
    }
}
