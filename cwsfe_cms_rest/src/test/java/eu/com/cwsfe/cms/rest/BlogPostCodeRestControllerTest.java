package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogPostCodesDAO;
import eu.com.cwsfe.cms.model.BlogPostCode;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-rest-test.xml"})
@WebAppConfiguration
public class BlogPostCodeRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogPostCodesDAO blogPostCodesDAO;

    @InjectMocks
    private BlogPostCodeRestController blogPostCodeRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogPostCodeRestController).build();
    }

    @Test
    public void testGetPostCode() throws Exception {
        Long blogPostId = 1l;
        String code = "code";
        String codeId = "codeId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCode(code);
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(blogPostId);
        when(blogPostCodesDAO.getCodeForPostByCodeId(anyLong(), anyString())).thenReturn(blogPostCode);

        ResultActions resultActions = mockMvc.perform(get("/rest/blogPostCode/1/2")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.codeId").value(codeId))
                .andExpect(jsonPath("$.blogPostId").value(blogPostId.intValue()))
                .andExpect(jsonPath("$.code").value(code));
    }

}