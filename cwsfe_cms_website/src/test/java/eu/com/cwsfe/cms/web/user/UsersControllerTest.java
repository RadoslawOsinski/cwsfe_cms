package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.dao.CmsRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUserRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.domains.CmsUserStatus;
import eu.com.cwsfe.cms.model.CmsRole;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.web.author.AuthorsController;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class UsersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsUsersDAO cmsUsersDAO;
    @Mock
    private CmsRolesDAO cmsRolesDAO;
    @Mock
    private CmsUserRolesDAO cmsUserRolesDAO;

    @InjectMocks
    private UsersController usersController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/users/Users"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/users/Users.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListUsers() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int count = 1;
        List<CmsUser> users = new ArrayList<>();
        CmsUser user = new CmsUser();
        long userId = 2L;
        String userName = "userName";
        user.setId(userId);
        user.setUserName(userName);
        user.setStatus(CmsUserStatus.NEW);
        users.add(user);
        when(cmsUsersDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(users);
        when(cmsUsersDAO.countForAjax()).thenReturn(count);

        ResultActions resultActions = mockMvc.perform(get("/usersList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(count))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(count))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].userName").value(userName))
                .andExpect(jsonPath("$.aaData[0].status").value(CmsUserStatus.NEW.name()))
                .andExpect(jsonPath("$.aaData[0].id").value((int) userId));
        verify(cmsUsersDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsUsersDAO, times(1)).countForAjax();
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testListUsersForDropList() throws Exception {
        int limit = 1;
        List<CmsUser> users = new ArrayList<>();
        CmsUser user = new CmsUser();
        long userId = 2L;
        String userName = "userName";
        user.setId(userId);
        user.setUserName(userName);
        user.setStatus(CmsUserStatus.NEW);
        users.add(user);
        when(cmsUsersDAO.listUsersForDropList(anyString(), anyInt())).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(get("/usersDropList")
                .param("term", userName)
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].id").value((int) userId))
                .andExpect(jsonPath("$.data[0].userName").value(userName));
        verify(cmsUsersDAO, times(1)).listUsersForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testAddUser() throws Exception {
        CmsUser user = new CmsUser();
        long userId = 2L;
        String userName = "userName";
        String passwordHash = "passwordHash";
        user.setId(userId);
        user.setUserName(userName);
        user.setPasswordHash(passwordHash);
        user.setStatus(CmsUserStatus.NEW);
        when(cmsUsersDAO.getByUsername(anyString())).thenThrow(new EmptyResultDataAccessException(1));
        when(cmsUsersDAO.add(Matchers.any(CmsUser.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addUser")
                .param("userName", userName)
                .param("passwordHash", passwordHash))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsUsersDAO, times(1)).getByUsername(anyString());
        verify(cmsUsersDAO, times(1)).add(Matchers.any(CmsUser.class));
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testAddAlreadyExistingUser() throws Exception {
        CmsUser user = new CmsUser();
        long userId = 2L;
        String userName = "CWSFE_CMS_ADMIN";
        String passwordHash = "passwordHash";
        user.setId(userId);
        user.setUserName(userName);
        user.setPasswordHash(passwordHash);
        user.setStatus(CmsUserStatus.NEW);
        when(cmsUsersDAO.getByUsername(anyString())).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(post("/addUser")
                .param("userName", userName)
                .param("passwordHash", passwordHash))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + UsersController.JSON_ERROR_MESSAGES + ".[0].error").exists());
        verify(cmsUsersDAO, times(1)).getByUsername(anyString());
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(cmsUsersDAO).delete(Matchers.any(CmsUser.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteUser")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsUsersDAO, times(1)).delete(Matchers.any(CmsUser.class));
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testLockUser() throws Exception {
        doNothing().when(cmsUsersDAO).lock(Matchers.any(CmsUser.class));

        ResultActions resultActions = mockMvc.perform(post("/lockUser")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsUsersDAO, times(1)).lock(Matchers.any(CmsUser.class));
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testUnlockUser() throws Exception {
        doNothing().when(cmsUsersDAO).unlock(Matchers.any(CmsUser.class));

        ResultActions resultActions = mockMvc.perform(post("/unlockUser")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsUsersDAO, times(1)).unlock(Matchers.any(CmsUser.class));
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testBrowseUser() throws Exception {
        CmsUser cmsUser = new CmsUser();
        long id = 1L;
        cmsUser.setId(id);
        when(cmsUsersDAO.get(anyLong())).thenReturn(cmsUser);
        List<CmsRole> cmsRoles = new ArrayList<>(1);
        CmsRole cmsRole = new CmsRole();
        cmsRole.setId(2L);
        cmsRole.setRoleCode("roleCode");
        cmsRole.setRoleName("roleName");
        cmsRoles.add(cmsRole);
        when(cmsRolesDAO.listUserRoles(anyLong())).thenReturn(cmsRoles);
        when(cmsRolesDAO.list()).thenReturn(cmsRoles);

        ResultActions resultActions = mockMvc.perform(get("/users/1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("cms/users/SingleUser"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/users/SingleUser.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("cmsUser", anything()))
                .andExpect(model().attribute("userSelectedRoles", anything()))
                .andExpect(model().attribute("cmsRoles", anything()));
    }

    @Test
    public void testUserRolesUpdate() throws Exception {
        CmsUser cmsUser = new CmsUser();
        long id = 1L;
        cmsUser.setId(id);
        when(cmsUsersDAO.get(anyLong())).thenReturn(cmsUser);
        List<CmsRole> cmsRoles = new ArrayList<>(1);
        CmsRole cmsRole = new CmsRole();
        cmsRole.setId(2L);
        cmsRole.setRoleCode("roleCode");
        cmsRole.setRoleName("roleName");
        cmsRoles.add(cmsRole);
        when(cmsRolesDAO.listUserRoles(anyLong())).thenReturn(cmsRoles);
        when(cmsRolesDAO.list()).thenReturn(cmsRoles);
        doNothing().when(cmsUserRolesDAO).deleteForUser(anyLong());

        ResultActions resultActions = mockMvc.perform(post("/userRolesUpdate").param("id", String.valueOf(id)));

        resultActions
                .andExpect(status().isSeeOther())
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/users/SingleUser.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("cmsUser", anything()))
                .andExpect(model().attribute("userSelectedRoles", anything()))
                .andExpect(model().attribute("cmsRoles", anything()));
    }

    @Test
    public void testUpdateUserBasicInfo() throws Exception {
        CmsUser user = new CmsUser();
        long userId = 2L;
        String userName = "userName";
        String passwordHash = "passwordHash";
        user.setId(userId);
        user.setUserName(userName);
        user.setPasswordHash(passwordHash);
        user.setStatus(CmsUserStatus.NEW);
        doNothing().when(cmsUsersDAO).updatePostBasicInfo(Matchers.any(CmsUser.class));

        ResultActions resultActions = mockMvc.perform(post("/users/updateUserBasicInfo")
                .param("id", String.valueOf(userId))
                .param("userName", userName)
                .param("status", CmsUserStatus.NEW.name()))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsUsersDAO, times(1)).updatePostBasicInfo(Matchers.any(CmsUser.class));
        verifyNoMoreInteractions(cmsUsersDAO);
    }
}
