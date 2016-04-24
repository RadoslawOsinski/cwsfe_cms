package eu.com.cwsfe.cms.web.monitoring;

import org.junit.Before;
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

import static org.hamcrest.Matchers.anything;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class MonitoringGeneralInformationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServerWatch serverWatch;

    @InjectMocks
    private MonitoringGeneralInformationController monitoringGeneralInformationController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monitoringGeneralInformationController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/monitoring/generalInformation"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/monitoring/GeneralInformation"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/monitoring/GeneralInformation"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testGetGeneralMemoryInfo() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/monitoring/generalMemoryInfo"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.usedMemoryInMb").exists())
                .andExpect(jsonPath("$.availableMemoryInMB").exists());
    }
}