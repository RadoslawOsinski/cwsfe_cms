package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserAllowedNetAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class CmsUserAllowedNetAddressDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsUserAllowedNetAddressDAO dao;

    @Autowired
    private CmsUsersDAO userDao;

    public static final CmsUser CMS_USER = new CmsUser();

    @Before
    public void setUp() throws Exception {
        CMS_USER.setUserName("test");
        CMS_USER.setPasswordHash("a");
        CMS_USER.setId(userDao.add(CMS_USER));
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
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        dao.add(cmsUserAllowedNetAddress);

        //when
        List<CmsUserAllowedNetAddress> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testCountAddressesForUser() throws Exception {
        //given
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        dao.add(cmsUserAllowedNetAddress);

        //when
        int result = dao.countAddressesForUser(CMS_USER.getId());

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }


    @Test
    public void testListForUser() throws Exception {
        //given
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        dao.add(cmsUserAllowedNetAddress);

        //when
        List<CmsUserAllowedNetAddress> list = dao.listForUser(CMS_USER.getId());

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Number of addresses should be 1", 1, list.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        cmsUserAllowedNetAddress.setId(dao.add(cmsUserAllowedNetAddress));

        //when
        CmsUserAllowedNetAddress cmsUserAllowedNetAddressResult = dao.get(cmsUserAllowedNetAddress.getId());

        //then
        assertNotNull(cmsUserAllowedNetAddressResult);
        assertEquals((long) cmsUserAllowedNetAddress.getId(), (long) cmsUserAllowedNetAddressResult.getId());
        assertEquals((long) CMS_USER.getId(), (long) cmsUserAllowedNetAddressResult.getUserId());
        assertEquals(inetAddress, cmsUserAllowedNetAddressResult.getInetAddress());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);

        //when
        cmsUserAllowedNetAddress.setId(dao.add(cmsUserAllowedNetAddress));

        //then
        CmsUserAllowedNetAddress cmsUserAllowedNetAddressResult = dao.get(cmsUserAllowedNetAddress.getId());
        assertNotNull(cmsUserAllowedNetAddressResult);
        assertEquals((long) cmsUserAllowedNetAddress.getId(), (long) cmsUserAllowedNetAddressResult.getId());
        assertEquals((long) CMS_USER.getId(), (long) cmsUserAllowedNetAddressResult.getUserId());
        assertEquals(inetAddress, cmsUserAllowedNetAddressResult.getInetAddress());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String inetAddress = "127.0.0.1";
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        cmsUserAllowedNetAddress.setUserId(CMS_USER.getId());
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        cmsUserAllowedNetAddress.setId(dao.add(cmsUserAllowedNetAddress));

        //when
        dao.delete(cmsUserAllowedNetAddress);

        //then
        CmsUserAllowedNetAddress cmsUserAllowedNetAddressResult = null;
        try {
            cmsUserAllowedNetAddressResult = dao.get(cmsUserAllowedNetAddress.getId());
        } catch (EmptyResultDataAccessException e) {
            assertNotNull("User IP address should not exist", e);
        }
        assertNull("User IP address should not exist", cmsUserAllowedNetAddressResult);
    }

}