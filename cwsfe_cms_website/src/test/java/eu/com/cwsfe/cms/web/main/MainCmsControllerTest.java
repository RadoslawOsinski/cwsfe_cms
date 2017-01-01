package eu.com.cwsfe.cms.web.main;

import eu.com.cwsfe.cms.dao.BlogPostCommentsDAO;
import eu.com.cwsfe.cms.domains.BlogPostCommentStatus;
import eu.com.cwsfe.cms.model.BlogPostComment;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class MainCmsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogPostCommentsDAO blogPostCommentsDAO;

    @InjectMocks
    private MainCmsController mainCmsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mainCmsController).build();
    }

    @Test
    public void testPrintDashboard() throws Exception {
        mockMvc.perform(get("/Main"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/main/Main"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/main/Dashboard"))
                .andExpect(model().attribute("additionalCssCode", ""))
                .andExpect(model().attribute("userName", anything()));
    }

    @Test
    public void testListBlogPostComments() throws Exception {
        String sEcho = "1";
        int numberOfBlogPostComment = 1;
        int ajaxCountNumber = 4;
        long id = 2L;
        String comment = "comment";
        String userName = "userName";
        String email = "r@w.eu";
        List<BlogPostComment> blogPostComments = new ArrayList<>();
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setId(id);
        blogPostComment.setStatus(BlogPostCommentStatus.NEW);
        blogPostComment.setCreated(new Timestamp(1));
        blogPostComment.setComment(comment);
        blogPostComment.setUserName(userName);
        blogPostComment.setEmail(email);
        blogPostComments.add(blogPostComment);
        when(blogPostCommentsDAO.searchByAjax(anyInt(), anyInt())).thenReturn(blogPostComments);
        when(blogPostCommentsDAO.searchByAjaxCount()).thenReturn(ajaxCountNumber);
        when(blogPostCommentsDAO.getTotalNumberNotDeleted()).thenReturn(numberOfBlogPostComment);

        ResultActions resultActions = mockMvc.perform(get("/blogPostCommentsList")
                .param("iDisplayStart", "0")
                .param("iDisplayLength", "2")
                .param("sEcho", sEcho));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfBlogPostComment))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(ajaxCountNumber))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(1))
                .andExpect(jsonPath("$.aaData[0].userName").value(userName + "[" + email + "]"))
                .andExpect(jsonPath("$.aaData[0].comment").value(comment))
//                .andExpect(jsonPath("$.aaData[0].created").value("1970-01-01"))
                .andExpect(jsonPath("$.aaData[0].status").value(BlogPostCommentStatus.NEW.name()))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(blogPostCommentsDAO, times(1)).searchByAjax(anyInt(), anyInt());
        verify(blogPostCommentsDAO, times(1)).searchByAjaxCount();
        verify(blogPostCommentsDAO, times(1)).getTotalNumberNotDeleted();
        verifyNoMoreInteractions(blogPostCommentsDAO);
    }
}
