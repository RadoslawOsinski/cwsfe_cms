package eu.com.cwsfe.cms.web.login;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class CmsLoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsGlobalParamsRepository cmsGlobalParamsDAO;

    @InjectMocks
    private CmsLoginController cmsLoginController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cmsLoginController).build();
    }

    @Test
    public void testLoginPage() throws Exception {
        String someUrl = "someUrl";
        CmsGlobalParam value = new CmsGlobalParam();
        value.setValue(someUrl);
        when(cmsGlobalParamsDAO.getByCode("MAIN_SITE")).thenReturn(value);

        ResultActions resultActions = mockMvc.perform(get("/"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("cms/login/Login"))
            .andExpect(model().attribute("mainSiteUrl", someUrl));
    }

    @Test
    public void testLogin() throws Exception {
        String someUrl = "someUrl";
        CmsGlobalParam value = new CmsGlobalParam();
        value.setValue(someUrl);
        when(cmsGlobalParamsDAO.getByCode("MAIN_SITE")).thenReturn(value);

        ResultActions resultActions = mockMvc.perform(get("/loginPage"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("cms/login/Login"))
            .andExpect(model().attribute("mainSiteUrl", someUrl));
    }

    @Test
    public void testLoginError() throws Exception {
        String someUrl = "someUrl";
        CmsGlobalParam value = new CmsGlobalParam();
        value.setValue(someUrl);
        when(cmsGlobalParamsDAO.getByCode("MAIN_SITE")).thenReturn(value);

        ResultActions resultActions = mockMvc.perform(get("/loginFailed"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("cms/login/Login"))
            .andExpect(model().attribute("error", "true"))
            .andExpect(model().attribute("mainSiteUrl", someUrl));
    }

    @Test
    public void testLogout() throws Exception {
        String someUrl = "someUrl";
        CmsGlobalParam value = new CmsGlobalParam();
        value.setValue(someUrl);
        when(cmsGlobalParamsDAO.getByCode("MAIN_SITE")).thenReturn(value);

        ResultActions resultActions = mockMvc.perform(get("/logout"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(view().name("cms/login/Login"))
            .andExpect(model().attribute("mainSiteUrl", someUrl));
    }
}
