package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.dao.CmsRolesDAO;
import eu.com.cwsfe.cms.model.CmsRole;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class RolesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsRolesRepository cmsRolesDAO;

    @InjectMocks
    private RolesController rolesController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rolesController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/roles"))
            .andExpect(status().isOk())
            .andExpect(view().name("cms/roles/Roles"))
            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/roles/Roles.js"))
            .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListRoles() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int count = 1;
        ArrayList<CmsRole> cmsRoles = new ArrayList<>();
        CmsRole cmsRole = new CmsRole();
        long roleId = 2L;
        String roleName = "roleName";
        String roleCode = "roleCode";
        cmsRole.setId(roleId);
        cmsRole.setRoleName(roleName);
        cmsRole.setRoleCode(roleCode);
        cmsRoles.add(cmsRole);
        when(cmsRolesDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsRoles);
        when(cmsRolesDAO.countForAjax()).thenReturn(count);

        ResultActions resultActions = mockMvc.perform(get("/rolesList")
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
            .andExpect(jsonPath("$.aaData[0].roleName").value(roleName))
            .andExpect(jsonPath("$.aaData[0].roleCode").value(roleCode));
        verify(cmsRolesDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsRolesDAO, times(1)).countForAjax();
        verifyNoMoreInteractions(cmsRolesDAO);
    }

    @Test
    public void testListRolesForDropList() throws Exception {
        int limit = 1;
        ArrayList<CmsRole> cmsRoles = new ArrayList<>();
        CmsRole cmsRole = new CmsRole();
        long roleId = 2L;
        String roleName = "roleName";
        String roleCode = "roleCode";
        cmsRole.setId(roleId);
        cmsRole.setRoleName(roleName);
        cmsRole.setRoleCode(roleCode);
        cmsRoles.add(cmsRole);
        when(cmsRolesDAO.listRolesForDropList(anyString(), anyInt())).thenReturn(cmsRoles);

        ResultActions resultActions = mockMvc.perform(get("/rolesDropList")
            .param("term", roleName)
            .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data[0].id").value((int) roleId))
            .andExpect(jsonPath("$.data[0].roleName").value(roleName));
        verify(cmsRolesDAO, times(1)).listRolesForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(cmsRolesDAO);
    }
}
