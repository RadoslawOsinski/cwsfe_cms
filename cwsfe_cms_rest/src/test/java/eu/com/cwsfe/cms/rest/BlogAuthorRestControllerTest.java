package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsAuthorsDAO;
import eu.com.cwsfe.cms.model.CmsAuthor;
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

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-rest-test.xml"})
@WebAppConfiguration
public class BlogAuthorRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsAuthorsDAO cmsAuthorsDAO;

    @InjectMocks
    private BlogAuthorRestController blogAuthorRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogAuthorRestController).build();
    }

    @Test
    public void testGetAuthor() throws Exception {
        long id = 1L;
        String firstName = "Radoslaw";
        String lastName = "Osinski";
        String googlePlusAuthorLink = "https://plus.google.com/+Rados%C5%82awOsi%C5%84ski/";
        CmsAuthor value = new CmsAuthor();
        value.setFirstName(firstName);
        value.setLastName(lastName);
        value.setGooglePlusAuthorLink(googlePlusAuthorLink);
        value.setId(id);
        when(cmsAuthorsDAO.get(anyLong())).thenReturn(value);

        ResultActions resultActions = mockMvc.perform(get("/rest/author/1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.googlePlusAuthorLink").value(googlePlusAuthorLink));
    }
}