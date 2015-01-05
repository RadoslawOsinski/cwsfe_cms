package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-rest-test.xml"})
@WebAppConfiguration
public class CmsTextI18nRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CmsTextI18nDAO cmsTextI18nDAO;

    @InjectMocks
    private CmsTextI18nRestController cmsTextI18nRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cmsTextI18nRestController).build();

    }

    @Test
    public void testGetTranslation() throws Exception {
        String text = "text";
        when(cmsTextI18nDAO.findTranslation(anyString(), anyString(), anyString())).thenReturn(text);

        ResultActions resultActions = mockMvc.perform(get("/rest/getTranslation")
                .param("languageCode", "en")
                .param("category", "category")
                .param("key", "key")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().string(text));
    }
}