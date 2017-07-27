//package eu.com.cwsfe.cms.web.blog;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
//@WebAppConfiguration
//@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
//public class BlogPostCommentsControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private BlogPostCommentsRepository blogPostCommentsDAO;
//
//    @InjectMocks
//    private BlogPostCommentsController blogPostCommentsController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(blogPostCommentsController).build();
//    }
//
//    @Test
//    public void testBlogPostCommentPublish() throws Exception {
//        doNothing().when(blogPostCommentsDAO).publish(any(BlogPostComment.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/publishBlogPostComment")
//            .param("id", "1"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + BlogPostCommentsController.JSON_STATUS).value(BlogPostCommentsController.JSON_STATUS_SUCCESS));
//        verify(blogPostCommentsDAO, times(1)).publish(any(BlogPostComment.class));
//        verifyNoMoreInteractions(blogPostCommentsDAO);
//    }
//
//    @Test
//    public void testBlogPostCommentBlock() throws Exception {
//        doNothing().when(blogPostCommentsDAO).block(any(BlogPostComment.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/blockBlogPostComment")
//            .param("id", "1"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + BlogPostCommentsController.JSON_STATUS).value(BlogPostCommentsController.JSON_STATUS_SUCCESS));
//        verify(blogPostCommentsDAO, times(1)).block(any(BlogPostComment.class));
//        verifyNoMoreInteractions(blogPostCommentsDAO);
//    }
//
//    @Test
//    public void testBlogPostCommentMarkAsSpam() throws Exception {
//        doNothing().when(blogPostCommentsDAO).markAsSpam(any(BlogPostComment.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/markAsSpamBlogPostComment")
//            .param("id", "1"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + BlogPostCommentsController.JSON_STATUS).value(BlogPostCommentsController.JSON_STATUS_SUCCESS));
//        verify(blogPostCommentsDAO, times(1)).markAsSpam(any(BlogPostComment.class));
//        verifyNoMoreInteractions(blogPostCommentsDAO);
//    }
//
//    @Test
//    public void testBlogPostCommentDelete() throws Exception {
//        doNothing().when(blogPostCommentsDAO).delete(any(BlogPostComment.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/deleteBlogPostComment")
//            .param("id", "1"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + BlogPostCommentsController.JSON_STATUS).value(BlogPostCommentsController.JSON_STATUS_SUCCESS));
//        verify(blogPostCommentsDAO, times(1)).delete(any(BlogPostComment.class));
//        verifyNoMoreInteractions(blogPostCommentsDAO);
//    }
//}
