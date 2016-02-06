package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.CmsUserStatus;
import eu.com.cwsfe.cms.model.CmsUser;
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
public class CmsUsersDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsUsersDAO dao;
    
    @Test
    public void testIsActiveUsernameInDatabase() throws Exception {
        //given
        String username = "test";
        String password = "password";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        dao.add(cmsUser);

        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase(username);

        //then
        assertNotNull(activeUsernameInDatabase);
        assertTrue("New added user should be active", activeUsernameInDatabase);
    }

    @Test
    public void testNotExistingUserIsNotActiveUsernameInDatabase() throws Exception {
        //given
        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase("abc");

        //then
        assertNotNull(activeUsernameInDatabase);
        assertFalse("Non existing user should not be active", activeUsernameInDatabase);
    }

    @Test
    public void testLockedUserIsNotActiveUsernameInDatabase() throws Exception {
        //given
        String username = "testLocked";
        String password = "password";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setStatus(CmsUserStatus.LOCKED);
        cmsUser.setPasswordHash(hash);
        cmsUser.setId(dao.add(cmsUser));
        dao.lock(cmsUser);

        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase(username);

        //then
        assertNotNull(activeUsernameInDatabase);
        assertFalse("Locked user should not be active", activeUsernameInDatabase);
    }

    @Test
    public void testDeletedUserIsNotActiveUsernameInDatabase() throws Exception {
        //given
        String username = "testDeleted";
        String password = "password";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setStatus(CmsUserStatus.DELETED);
        cmsUser.setPasswordHash(hash);
        cmsUser.setId(dao.add(cmsUser));
        dao.delete(cmsUser);

        //when
        boolean activeUsernameInDatabase = dao.isActiveUsernameInDatabase(username);

        //then
        assertNotNull(activeUsernameInDatabase);
        assertFalse("Locked user should not be active", activeUsernameInDatabase);
    }

    @Test
    public void testGetByUsername() throws Exception {
        //given
        String username = "test";
        String password = "password";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setPassword(password);
        cmsUser.setPasswordHash(hash);
        cmsUser.setStatus(CmsUserStatus.NEW);
        Long addedId = dao.add(cmsUser);

        //when
        CmsUser cmsUserResult = dao.getByUsername(username);

        //then
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(CmsUserStatus.NEW, cmsUserResult.getStatus());
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
        assertNotNull("There should be few predefined users", list);
        assertTrue("CWSFE_CMS_ADMIN should be predefined", list.stream().filter(w -> "CWSFE_CMS_ADMIN".equals(w.getUserName())).count() > 0);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName("test1");
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        dao.add(cmsUser);
        CmsUser cmsUser2 = new CmsUser();
        cmsUser2.setUserName("test2");
        cmsUser2.setStatus(CmsUserStatus.NEW);
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
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);

        //when
        CmsUser cmsUserResult = dao.get(addedId);

        //then
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(CmsUserStatus.NEW, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String username = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);

        //when
        Long addedId = dao.add(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username, cmsUserResult.getUserName());
        assertEquals(CmsUserStatus.NEW, cmsUserResult.getStatus());
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
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus(CmsUserStatus.NEW);
        cmsUser.setPasswordHash(hash);
        Long addedId = dao.add(cmsUser);
        cmsUser.setId(addedId);
        cmsUser.setUserName(username2);
        cmsUser.setStatus(CmsUserStatus.DELETED);

        //when
        dao.updatePostBasicInfo(cmsUser);

        //then
        CmsUser cmsUserResult = dao.get(addedId);
        assertNotNull(cmsUserResult);
        assertEquals((long) addedId, (long) cmsUserResult.getId());
        assertEquals(username2, cmsUserResult.getUserName());
        assertEquals(CmsUserStatus.DELETED, cmsUserResult.getStatus());
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
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        assertEquals("Unexpected status value for deleted object", CmsUserStatus.DELETED, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        assertEquals("Unexpected status for undeleted object", CmsUserStatus.NEW, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testLock() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        assertEquals("Unexpected status for locked object", CmsUserStatus.LOCKED, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }

    @Test
    public void testUnlock() throws Exception {
        //given
        String username1 = "test";
        String hash = "hash";
        CmsUser cmsUser = new CmsUser();
        cmsUser.setUserName(username1);
        cmsUser.setStatus(CmsUserStatus.NEW);
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
        assertEquals("Unexpected status for unlocked object", CmsUserStatus.NEW, cmsUserResult.getStatus());
        assertEquals(hash, cmsUserResult.getPasswordHash());
    }
}