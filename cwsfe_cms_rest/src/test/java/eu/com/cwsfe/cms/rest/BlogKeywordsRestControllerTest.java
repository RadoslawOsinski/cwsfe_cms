//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.db.blog.BlogKeywordsEntity;
//import eu.com.cwsfe.cms.db.blog.BlogKeywordsRepository;
//import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
//import eu.com.cwsfe.cms.db.configuration.RestTestConfiguration;
//import eu.com.cwsfe.cms.db.news.CmsTextI18nRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Matchers.anyLong;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {RestTestConfiguration.class})
//@WebAppConfiguration
//public class BlogKeywordsRestControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private BlogKeywordsRepository blogKeywordsRepository;
//    @Mock
//    private BlogPostKeywordsRepository blogPostKeywordsRepository;
//    @Mock
//    private CmsTextI18nRepository cmsTextI18nDAO;
//
//    @InjectMocks
//    private BlogKeywordsRestController blogKeywordsRestController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(blogKeywordsRestController).build();
//    }
//
//    @Test
//    public void testBlogKeywordsList() throws Exception {
//        String blogKeywordI18n = "blogKeywordI18n";
//        long id = 1L;
//        List<BlogKeywordsEntity> blogKeywords = new ArrayList<>();
//        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
//        blogKeyword.setId(id);
//        blogKeyword.setKeywordName("keyword");
//        blogKeyword.setStatus(NewDeletedStatus.NEW);
//        blogKeywords.add(blogKeyword);
//        when(blogKeywordsRepository.list()).thenReturn(blogKeywords);
//        when(cmsTextI18nDAO.findTranslation(anyString(), anyString(), anyString())).thenReturn(blogKeywordI18n);
//
//        ResultActions resultActions = mockMvc.perform(get("/rest/blogKeywordsList")
//            .param("languageCode", "en")
//            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$[0].id").value((int) id))
//            .andExpect(jsonPath("$[0].keywordName").value(blogKeywordI18n));
//    }
//
//    @Test
//    public void testBlogKeywordsListWithMissingTranslation() throws Exception {
//        String keyword = "keyword";
//        long id = 1L;
//        List<BlogKeywordsEntity> blogKeywords = new ArrayList<>();
//        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
//        blogKeyword.setId(id);
//        blogKeyword.setKeywordName(keyword);
//        blogKeyword.setStatus(NewDeletedStatus.NEW);
//        blogKeywords.add(blogKeyword);
//        when(blogKeywordsRepository.list()).thenReturn(blogKeywords);
//        when(cmsTextI18nDAO.findTranslation(anyString(), anyString(), anyString())).thenThrow(new EmptyResultDataAccessException(1));
//
//        ResultActions resultActions = mockMvc.perform(get("/rest/blogKeywordsList")
//            .param("languageCode", "en")
//            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$[0].id").value((int) id))
//            .andExpect(jsonPath("$[0].keywordName").value(keyword));
//    }
//
//    @Test
//    public void testBlogKeywordsListForPost() throws Exception {
//        String blogKeywordI18n = "blogKeywordI18n";
//        long id = 1L;
//        List<BlogKeyword> blogKeywords = new ArrayList<>();
//        BlogKeyword blogKeyword = new BlogKeyword();
//        blogKeyword.setId(id);
//        blogKeyword.setKeywordName("keyword");
//        blogKeyword.setStatus(BlogKeywordStatus.NEW);
//        blogKeywords.add(blogKeyword);
//        when(blogPostKeywordsRepository.listForPost(anyLong())).thenReturn(blogKeywords);
//        when(cmsTextI18nDAO.findTranslation(anyString(), anyString(), anyString())).thenReturn(blogKeywordI18n);
//
//        ResultActions resultActions = mockMvc.perform(get("/rest/postKeywords")
//            .param("blogPostId", "1")
//            .param("languageCode", "en")
//            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$[0].id").value((int) id))
//            .andExpect(jsonPath("$[0].keywordName").value(blogKeywordI18n));
//    }
//
//}
