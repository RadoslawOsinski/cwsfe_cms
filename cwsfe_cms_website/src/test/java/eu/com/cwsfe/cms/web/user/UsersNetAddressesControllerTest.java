package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.dao.CmsUserAllowedNetAddressDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserAllowedNetAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class UsersNetAddressesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsUsersDAO cmsUsersDAO;

    @Mock
    private CmsUserAllowedNetAddressDAO cmsUserAllowedNetAddressDAO;

    @InjectMocks
    private UsersNetAddressesController usersNetAddressesController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersNetAddressesController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/usersNetAddresses"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/usersNetAddresses/UsersNetAddresses"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/usersNetAddresses/UsersNetAddresses.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListUsers() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int count = 1;
        long userId = 2L;
        ArrayList<CmsUserAllowedNetAddress> netAddresses = new ArrayList<>();
        CmsUserAllowedNetAddress cmsUserAllowedNetAddress = new CmsUserAllowedNetAddress();
        long id = 1L;
        cmsUserAllowedNetAddress.setId(id);
        String inetAddress = "127.0.0.1";
        cmsUserAllowedNetAddress.setId(id);
        cmsUserAllowedNetAddress.setInetAddress(inetAddress);
        cmsUserAllowedNetAddress.setUserId(userId);
        netAddresses.add(cmsUserAllowedNetAddress);
        when(cmsUserAllowedNetAddressDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(netAddresses);
        when(cmsUserAllowedNetAddressDAO.countForAjax()).thenReturn(count);
        CmsUser cmsUser = new CmsUser();
        String userName = "userName";
        cmsUser.setUserName(userName);
        when(cmsUsersDAO.get(anyLong())).thenReturn(cmsUser);

        ResultActions resultActions = mockMvc.perform(get("/usersNetAddressesList")
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
                .andExpect(jsonPath("$.aaData[0].inetAddress").value(inetAddress))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(cmsUserAllowedNetAddressDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsUserAllowedNetAddressDAO, times(1)).countForAjax();
        verify(cmsUsersDAO, times(1)).get(anyLong());
        verifyNoMoreInteractions(cmsUserAllowedNetAddressDAO);
        verifyNoMoreInteractions(cmsUsersDAO);
    }

    @Test
    public void testAddNetAddress() throws Exception {
        when(cmsUserAllowedNetAddressDAO.add(Matchers.any(CmsUserAllowedNetAddress.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addNetAddress")
                .param("userId", "1")
                .param("inetAddress", "127.0.0.1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + UsersNetAddressesController.JSON_STATUS).value(UsersNetAddressesController.JSON_STATUS_SUCCESS));
        verify(cmsUserAllowedNetAddressDAO, times(1)).add(Matchers.any(CmsUserAllowedNetAddress.class));
        verifyNoMoreInteractions(cmsUserAllowedNetAddressDAO);
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(cmsUserAllowedNetAddressDAO).delete(Matchers.any(CmsUserAllowedNetAddress.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteNetAddress")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + UsersNetAddressesController.JSON_STATUS).value(UsersNetAddressesController.JSON_STATUS_SUCCESS));
        verify(cmsUserAllowedNetAddressDAO, times(1)).delete(Matchers.any(CmsUserAllowedNetAddress.class));
        verifyNoMoreInteractions(cmsUserAllowedNetAddressDAO);
    }
}