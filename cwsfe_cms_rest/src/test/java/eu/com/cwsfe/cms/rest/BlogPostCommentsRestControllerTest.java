package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogPostCommentsDAO;
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
public class BlogPostCommentsRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogPostCommentsDAO blogPostCommentsDAO;

    @InjectMocks
    private BlogPostCommentsRestController blogPostCommentsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogPostCommentsRestController).build();
    }

    @Test
    public void testCountCommentsForPostI18n() throws Exception {
        int count = 1;
        when(blogPostCommentsDAO.countCommentsForPostI18n(anyLong())).thenReturn(count);

        ResultActions resultActions = mockMvc.perform(get("/rest/comments")
                .param("blogPostI18nContentId", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.count").value(count));
    }
}