package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.db.blog.BlogKeywordsEntity;
import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import eu.com.cwsfe.cms.services.blog.BlogKeywordsService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class BlogKeywordsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlogKeywordsService blogKeywordsService;

    @InjectMocks
    private BlogKeywordsController blogKeywordsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogKeywordsController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/blogKeywords"))
            .andExpect(status().isOk())
            .andExpect(view().name("cms/blogkeywords/BlogKeywords"))
            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/blogkeywords/Blogkeywords.js"))
            .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListBlogKeywords() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        long numberOfBlogKeywords = 1;
        List<BlogKeywordsEntity> blogKeywords = new ArrayList<>();
        BlogKeywordsEntity cmsTextI18nCategory = new BlogKeywordsEntity();
        String keywordName = "keywordName";
        long id = 1L;
        cmsTextI18nCategory.setId(id);
        cmsTextI18nCategory.setKeywordName(keywordName);
        cmsTextI18nCategory.setStatus(NewDeletedStatus.NEW);
        blogKeywords.add(cmsTextI18nCategory);
        when(blogKeywordsService.listAjax(iDisplayStart, iDisplayLength)).thenReturn(blogKeywords);
        when(blogKeywordsService.countForAjax()).thenReturn(numberOfBlogKeywords);

        ResultActions resultActions = mockMvc.perform(get("/blogKeywordsList")
            .param("iDisplayStart", String.valueOf(iDisplayStart))
            .param("iDisplayLength", String.valueOf(iDisplayLength))
            .param("sEcho", sEcho))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.sEcho").value(sEcho))
            .andExpect(jsonPath("$.iTotalRecords").value(numberOfBlogKeywords))
            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfBlogKeywords))
            .andExpect(jsonPath("$.aaData").exists())
            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
            .andExpect(jsonPath("$.aaData[0].keywordName").value(keywordName))
            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(blogKeywordsService, times(1)).listAjax(anyInt(), anyInt());
        verify(blogKeywordsService, times(1)).countForAjax();
        verifyNoMoreInteractions(blogKeywordsService);
    }

    @Test
    public void testAddBlogKeyword() throws Exception {
        String keywordName = "keywordName";
        when(blogKeywordsService.add(any(BlogKeywordsEntity.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addBlogKeyword")
            .param("keywordName", keywordName));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + BlogKeywordsController.JSON_STATUS).value(BlogKeywordsController.JSON_STATUS_SUCCESS));
        verify(blogKeywordsService, times(1)).add(any(BlogKeywordsEntity.class));
        verifyNoMoreInteractions(blogKeywordsService);
    }

    @Test
    public void testAddExistingBlogKeyword() throws Exception {
        String keywordName = "keywordName";
        when(blogKeywordsService.add(any(BlogKeywordsEntity.class))).thenThrow(new DuplicateKeyException("Duplicate!"));

        ResultActions resultActions = mockMvc.perform(post("/addBlogKeyword")
            .param("keywordName", keywordName));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + BlogKeywordsController.JSON_STATUS).value(BlogKeywordsController.JSON_STATUS_FAIL))
            .andExpect(jsonPath("$." + BlogKeywordsController.JSON_ERROR_MESSAGES + "[0]").exists());
        verify(blogKeywordsService, times(1)).add(any(BlogKeywordsEntity.class));
        verifyNoMoreInteractions(blogKeywordsService);
    }

    @Test
    public void testDeleteFolder() throws Exception {
        int id = 1;
        doNothing().when(blogKeywordsService).delete(any(BlogKeywordsEntity.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteBlogKeyword")
            .param("id", String.valueOf(id)))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + BlogKeywordsController.JSON_STATUS).value(BlogKeywordsController.JSON_STATUS_SUCCESS));
        verify(blogKeywordsService, times(1)).delete(any(BlogKeywordsEntity.class));
        verifyNoMoreInteractions(blogKeywordsService);
    }
}
