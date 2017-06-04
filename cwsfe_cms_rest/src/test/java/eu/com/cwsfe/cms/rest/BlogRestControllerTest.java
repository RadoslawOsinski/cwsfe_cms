package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.db.configuration.RestTestConfiguration;
import eu.com.cwsfe.cms.dao.BlogPostI18nContentsDAO;
import eu.com.cwsfe.cms.dao.BlogPostsDAO;
import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostI18nContent;
import eu.com.cwsfe.cms.model.Language;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestTestConfiguration.class})
@WebAppConfiguration
public class BlogRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogPostsRepository blogPostsDAO;
    @Mock
    private BlogPostI18nContentsRepository blogPostI18nContentsDAO;
    @Mock
    private CmsLanguagesRepository cmsLanguagesDAO;

    @InjectMocks
    private BlogRestController blogRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogRestController).build();
    }

    @Test
    public void testListBlogPosts() throws Exception {
        long blogPostId = 1L;
        long authorId = 2L;
        Date postCreationDate = new Date(1);
        String postTextCode = "postTextCode";
        long blogPostI18nContentId = 4L;
        String description = "description";
        String shortcut = "shortcut";
        long languageId = 7L;
        String postTitle = "postTitle";
        Language language = new Language();
        language.setId(languageId);
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blogPostId);
        blogPost.setPostAuthorId(authorId);
        blogPost.setPostCreationDate(postCreationDate);
        blogPost.setPostTextCode(postTextCode);
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setId(blogPostI18nContentId);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setLanguageId(languageId);
        blogPostI18nContent.setPostTitle(postTitle);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        List<Object[]> ids = new ArrayList<>();
        ids.add(new Object[]{blogPostId, blogPostI18nContentId});
        when(blogPostsDAO.listForPageWithCategoryAndPaging(anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(ids);
        when(blogPostsDAO.get(anyLong())).thenReturn(blogPost);
        when(blogPostI18nContentsDAO.get(anyLong())).thenReturn(blogPostI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/blogI18nPairs")
            .param("languageCode", "en")
            .param("limit", "1")
            .param("offset", "0")
            .param("categoryId", "1")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$[0].blogPost.id").value((int) blogPostId))
            .andExpect(jsonPath("$[0].blogPost.postAuthorId").value((int) authorId))
            .andExpect(jsonPath("$[0].blogPost.postCreationDate").value((int) postCreationDate.getTime()))
            .andExpect(jsonPath("$[0].blogPost.postTextCode").value(postTextCode))
            .andExpect(jsonPath("$[0].blogPostI18nContent.id").value((int) blogPostI18nContentId))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postDescription").value(description))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postShortcut").value(shortcut))
            .andExpect(jsonPath("$[0].blogPostI18nContent.languageId").value((int) languageId))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postTitle").value(postTitle));
    }

    @Test
    public void testListBlogPostsWithCategory() throws Exception {
        long blogPostId = 1L;
        long authorId = 2L;
        Date postCreationDate = new Date(1);
        String postTextCode = "postTextCode";
        long blogPostI18nContentId = 4L;
        String description = "description";
        String shortcut = "shortcut";
        long languageId = 7L;
        String postTitle = "postTitle";
        Language language = new Language();
        language.setId(languageId);
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blogPostId);
        blogPost.setPostAuthorId(authorId);
        blogPost.setPostCreationDate(postCreationDate);
        blogPost.setPostTextCode(postTextCode);
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setId(blogPostI18nContentId);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setLanguageId(languageId);
        blogPostI18nContent.setPostTitle(postTitle);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        List<Object[]> ids = new ArrayList<>();
        ids.add(new Object[]{blogPostId, blogPostI18nContentId});
        when(blogPostsDAO.listForPageWithPaging(anyLong(), anyInt(), anyInt())).thenReturn(ids);
        when(blogPostsDAO.get(anyLong())).thenReturn(blogPost);
        when(blogPostI18nContentsDAO.get(anyLong())).thenReturn(blogPostI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/blogI18nPairs")
            .param("languageCode", "en")
            .param("limit", "1")
            .param("offset", "0")
            .param("categoryId", "")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$[0].blogPost.id").value((int) blogPostId))
            .andExpect(jsonPath("$[0].blogPost.postAuthorId").value((int) authorId))
            .andExpect(jsonPath("$[0].blogPost.postCreationDate").value((int) postCreationDate.getTime()))
            .andExpect(jsonPath("$[0].blogPost.postTextCode").value(postTextCode))
            .andExpect(jsonPath("$[0].blogPostI18nContent.id").value((int) blogPostI18nContentId))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postDescription").value(description))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postShortcut").value(shortcut))
            .andExpect(jsonPath("$[0].blogPostI18nContent.languageId").value((int) languageId))
            .andExpect(jsonPath("$[0].blogPostI18nContent.postTitle").value(postTitle));
    }

    @Test
    public void testListBlogPostsTotal() throws Exception {
        long total = 1L;
        Long languageId = 2L;
        Language language = new Language();
        language.setId(languageId);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        when(blogPostsDAO.listCountForPageWithPaging(anyLong())).thenReturn(total);

        ResultActions resultActions = mockMvc.perform(get("/rest/blogI18nPairsTotal")
            .param("languageCode", "en")
            .param("categoryId", "")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.total").value((int) total));
    }

    @Test
    public void testListBlogPostsWithCategoryTotal() throws Exception {
        long total = 1L;
        Long languageId = 2L;
        Language language = new Language();
        language.setId(languageId);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        when(blogPostsDAO.listCountForPageWithCategoryAndPaging(anyLong(), anyLong())).thenReturn(total);

        ResultActions resultActions = mockMvc.perform(get("/rest/blogI18nPairsTotal")
            .param("languageCode", "en")
            .param("categoryId", "1")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.total").value((int) total));
    }

    @Test
    public void testSinglePostView() throws Exception {
        long blogPostId = 1L;
        long authorId = 2L;
        Date postCreationDate = new Date(1);
        String postTextCode = "postTextCode";
        long blogPostI18nContentId = 4L;
        String description = "description";
        String shortcut = "shortcut";
        long languageId = 7L;
        String postTitle = "postTitle";
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blogPostId);
        blogPost.setPostAuthorId(authorId);
        blogPost.setPostCreationDate(postCreationDate);
        blogPost.setPostTextCode(postTextCode);
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setId(blogPostI18nContentId);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setLanguageId(languageId);
        blogPostI18nContent.setPostTitle(postTitle);
        when(blogPostsDAO.get(anyLong())).thenReturn(blogPost);
        when(blogPostI18nContentsDAO.get(anyLong())).thenReturn(blogPostI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/blog/singlePost/1/1")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.blogPost.id").value((int) blogPostId))
            .andExpect(jsonPath("$.blogPost.postAuthorId").value((int) authorId))
            .andExpect(jsonPath("$.blogPost.postCreationDate").value((int) postCreationDate.getTime()))
            .andExpect(jsonPath("$.blogPost.postTextCode").value(postTextCode))
            .andExpect(jsonPath("$.blogPostI18nContent.id").value((int) blogPostI18nContentId))
            .andExpect(jsonPath("$.blogPostI18nContent.postDescription").value(description))
            .andExpect(jsonPath("$.blogPostI18nContent.postShortcut").value(shortcut))
            .andExpect(jsonPath("$.blogPostI18nContent.languageId").value((int) languageId))
            .andExpect(jsonPath("$.blogPostI18nContent.postTitle").value(postTitle));
    }
}
