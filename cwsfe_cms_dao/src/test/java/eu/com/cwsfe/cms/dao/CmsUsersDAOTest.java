package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class CmsUsersDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsUsersDAO dao;
    
    @Test
    public void testIsActiveUsernameInDatabase() throws Exception {
        //given
        String username = "test";
        String password = "password";
        String status = "N";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setStatus(status);
        cmsUser.setPasswordHash(hash);
        dao.add(cmsUser);

        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase(username);

        //then
        assertNotNull(activeUsernameInDatabase);
        assertTrue("New added user should be active", activeUsernameInDatabase);
    }

    @Test
    public void testIsNotActiveUsernameInDatabase() throws Exception {
        //given
        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase("abc");

        //then
        assertNotNull(activeUsernameInDatabase);
        assertFalse("Non existing user should not be active", activeUsernameInDatabase);
    }

    @Test
    public void testGetByUsername() throws Exception {
        //given
        String username = "test";
        String password = "password";
        String status = "N";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setPasswordHash(hash);
        cmsUser.setStatus(status);
        Long addedId = dao.add(cmsUser);

        //when
        CmsUser cmsUserResult = dao.getByUsername(username);

        //then
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(status, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
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
        List<CmsUser> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName("test1");
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        dao.add(cmsUser);
        CmsUser cmsUser2 = new CmsUser();
        cmsUser2.setUserName("test2");
        cmsUser2.setStatus("N");
        cmsUser2.setPasswordHash(hash);
        dao.add(cmsUser2);

        //when
        List<CmsUser> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListUsersForDropList() throws Exception {
        //given
        String username = "test";
        String username2 = "test2";
        String status = "N";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(status);
        cmsUser.setPasswordHash(hash);
        dao.add(cmsUser);
        cmsUser.setUserName(username2);
        dao.add(cmsUser);

        //when
        List<CmsUser> cmsUsers = dao.listUsersForDropList(username, 1);

        //then
        assertNotNull(cmsUsers);
        assertEquals("Page limit was set to 1", 1, cmsUsers.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String username = "test";
        String status = "N";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(status);
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);

        //when
        CmsUser cmsUserResult = dao.get(addedId);

        //then
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(status, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String username = "test";
        String status = "N";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(status);
        cmsUser.setPasswordHash(hash);

        //when
        Long addedId = dao.add(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(status, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String username1 = "test";
        String username2 = "test2";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        cmsUser.setUserName(username2);

        //when
        dao.update(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username2, cmsUserResult.getUserName());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUpdatePostBasicInfo() throws Exception {
        //given
        String username1 = "test";
        String username2 = "test2";
        String status = "N";
        String newStatus = "D";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus(status);
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        cmsUser.setUserName(username2);
        cmsUser.setStatus(newStatus);

        //when
        dao.updatePostBasicInfo(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username2, cmsUserResult.getUserName());
        assertEquals(newStatus, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUpdateWithoutPassword() throws Exception {
        //given
        String username1 = "test";
        String username2 = "test2";
        String password = "password";
        String newPassword = "password2";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setPassword(password);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        cmsUser.setUserName(username2);
        cmsUser.setPassword(newPassword);

        //when
        dao.updateWithoutPassword(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username2, cmsUserResult.getUserName());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);

        //when
        dao.delete(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username1, cmsUserResult.getUserName());
        assertEquals("Unexpected status value for deleted object", "D", cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        dao.delete(cmsUser);

        //when
        dao.undelete(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username1, cmsUserResult.getUserName());
        assertEquals("Unexpected status for undeleted object", "N", cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testLock() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);

        //when
        dao.lock(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username1, cmsUserResult.getUserName());
        assertEquals("Unexpected status for locked object", "L", cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUnlock() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus("N");
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        dao.delete(cmsUser);

        //when
        dao.unlock(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username1, cmsUserResult.getUserName());
        assertEquals("Unexpected status for locked object", "N", cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }
}