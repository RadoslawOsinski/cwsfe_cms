package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.dao.BlogPostCodesDAO;
import eu.com.cwsfe.cms.model.BlogPostCode;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class BlogPostCodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogPostCodesDAO blogPostCodesDAO;

    @InjectMocks
    private BlogPostCodeController blogPostCodeController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogPostCodeController).build();
    }

    @Test
    public void testListBlogPostCodes() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfCodes = 1;
        int numberOfCodesNotDeleted = 7;
        long id = 1L;
        String codeId = "codeId";
        String code = "code";
        ArrayList<BlogPostCode> blogPostCodes = new ArrayList<>();
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setBlogPostId(id);
        blogPostCode.setCodeId(codeId);
        blogPostCode.setCode(code);
        blogPostCodes.add(blogPostCode);
        when(blogPostCodesDAO.searchByAjax(anyInt(), anyInt(), anyLong())).thenReturn(blogPostCodes);
        when(blogPostCodesDAO.searchByAjaxCount(anyLong())).thenReturn(numberOfCodes);
        when(blogPostCodesDAO.getTotalNumberNotDeleted()).thenReturn(numberOfCodesNotDeleted);

        ResultActions resultActions = mockMvc.perform(get("/blogPosts/blogPostCodesList")
                .param("blogPostId", "1")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfCodesNotDeleted))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfCodes))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].codeId").value(codeId))
                .andExpect(jsonPath("$.aaData[0].code").value(code))
                .andExpect(jsonPath("$.aaData[0].id").value(codeId));
        verify(blogPostCodesDAO, times(1)).searchByAjax(anyInt(), anyInt(), anyLong());
        verify(blogPostCodesDAO, times(1)).searchByAjaxCount(anyLong());
        verify(blogPostCodesDAO, times(1)).getTotalNumberNotDeleted();
        verifyNoMoreInteractions(blogPostCodesDAO);
    }

    @Test
    public void testAddBlogPostCode() throws Exception {
        when(blogPostCodesDAO.getCodeForPostByCodeId(anyLong(), anyString())).thenReturn(null);
        when(blogPostCodesDAO.add(any(BlogPostCode.class))).thenReturn("a");

        ResultActions resultActions = mockMvc.perform(post("/blogPosts/addBlogPostCode")
                .param("codeId", "1")
                .param("blogPostId", "2")
                .param("code", "code"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + BlogPostCodeController.JSON_STATUS).value(BlogPostCodeController.JSON_STATUS_SUCCESS));
        verify(blogPostCodesDAO, times(1)).getCodeForPostByCodeId(anyLong(), anyString());
        verify(blogPostCodesDAO, times(1)).add(any(BlogPostCode.class));
        verifyNoMoreInteractions(blogPostCodesDAO);
    }

    @Test
    public void testBlogPostCodeDelete() throws Exception {
        int id = 1;
        doNothing().when(blogPostCodesDAO).delete(any(BlogPostCode.class));

        ResultActions resultActions = mockMvc.perform(post("/blogPosts/deleteBlogPostCode")
                .param("codeId", String.valueOf(id))
                .param("blogPostId", String.valueOf(id)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + BlogPostCodeController.JSON_STATUS).value(BlogPostCodeController.JSON_STATUS_SUCCESS));
        verify(blogPostCodesDAO, times(1)).delete(any(BlogPostCode.class));
        verifyNoMoreInteractions(blogPostCodesDAO);
    }
}
