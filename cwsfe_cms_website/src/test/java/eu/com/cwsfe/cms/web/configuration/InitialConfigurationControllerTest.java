package eu.com.cwsfe.cms.web.configuration;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.dao.CmsRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUserRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsRole;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserRole;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class InitialConfigurationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;
    @Mock
    private CmsUsersDAO cmsUsersDAO;
    @Mock
    private CmsRolesDAO cmsRolesDAO;
    @Mock
    private CmsUserRolesDAO cmsUserRolesDAO;

    @InjectMocks
    private InitialConfigurationController initialConfigurationController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(initialConfigurationController).build();
    }

    @Test
    public void testDoNotShowInitialConfigurationWhenCmsConfigured() throws Exception {
        CmsGlobalParam cwsfeCmsIsConfigured = new CmsGlobalParam();
        cwsfeCmsIsConfigured.setValue("Y");
        when(cmsGlobalParamsDAO.getByCode("CWSFE_CMS_IS_CONFIGURED")).thenReturn(cwsfeCmsIsConfigured);

        mockMvc.perform(get("/configuration/initialConfiguration"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/login/Login"));
    }

    @Test
    public void testShowInitialConfigurationWhenCmsNotConfigured() throws Exception {
        when(cmsGlobalParamsDAO.getByCode("CWSFE_CMS_IS_CONFIGURED")).thenReturn(null);

        mockMvc.perform(get("/configuration/initialConfiguration"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/configuration/InitialConfiguration"));
    }

    @Test
    public void testAddAdminUser() throws Exception {
        CmsGlobalParam cwsfeCmsIsConfigured = new CmsGlobalParam();
        cwsfeCmsIsConfigured.setValue("N");
        when(cmsGlobalParamsDAO.getByCode("CWSFE_CMS_IS_CONFIGURED")).thenReturn(cwsfeCmsIsConfigured);
        when(cmsUsersDAO.add(any(CmsUser.class))).thenReturn(1L);
        when(cmsRolesDAO.getByCode("ROLE_CWSFE_CMS_ADMIN")).thenReturn(new CmsRole());
        doNothing().when(cmsUserRolesDAO).add(any(CmsUserRole.class));
        doNothing().when(cmsGlobalParamsDAO).update(cwsfeCmsIsConfigured);

        ResultActions resultActions = mockMvc.perform(post("/configuration/addAdminUser")
                        .param("userName", "abc")
                        .param("password", "def")
        );

        resultActions
                .andExpect(status().isSeeOther());
    }
}
