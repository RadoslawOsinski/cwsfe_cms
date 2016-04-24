package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.domains.CmsNewsI18nContentStatus;
import eu.com.cwsfe.cms.model.*;
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

import java.sql.Date;
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
public class CmsNewsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsDAO cmsNewsDAO;
    @Mock
    private CmsAuthorsDAO cmsAuthorsDAO;
    @Mock
    private CmsNewsI18nContentsDAO cmsNewsI18nContentsDAO;
    @Mock
    private CmsFoldersDAO cmsFoldersDAO;
    @Mock
    private NewsTypesDAO newsTypesDAO;
    @Mock
    private CmsLanguagesDAO cmsLanguagesDAO;

    @InjectMocks
    private CmsNewsController cmsNewsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cmsNewsController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/news/News"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/news/News.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testList() throws Exception {
        String sEcho = "1";
        int numberOfNews = 1;
        int ajaxCountNumber = 4;
        int id = 2;
        Date creationDate = new Date(1);
        String author = "author";
        String newsCode = "newsCode";
        List<Object[]> cmsNewses = new ArrayList<>();
        Object[] news = new Object[6];
        news[0] = id;
        news[1] = author;
        news[4] = creationDate;
        news[5] = newsCode;
        cmsNewses.add(news);
        when(cmsNewsDAO.searchByAjax(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(cmsNewses);
        when(cmsNewsDAO.searchByAjaxCount(anyInt(), anyString())).thenReturn(ajaxCountNumber);
        when(cmsNewsDAO.getTotalNumberNotDeleted()).thenReturn(numberOfNews);

        ResultActions resultActions = mockMvc.perform(get("/newsList")
                .param("iDisplayStart", "0")
                .param("iDisplayLength", "2")
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfNews))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(ajaxCountNumber))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(1))
                .andExpect(jsonPath("$.aaData[0].author").value(author))
                .andExpect(jsonPath("$.aaData[0].newsCode").value(newsCode))
                .andExpect(jsonPath("$.aaData[0].creationDate").value("1970-01-01"))
                .andExpect(jsonPath("$.aaData[0].id").value(id));
        verify(cmsNewsDAO, times(1)).searchByAjax(anyInt(), anyInt(), anyInt(), anyString());
        verify(cmsNewsDAO, times(1)).searchByAjaxCount(anyInt(), anyString());
        verify(cmsNewsDAO, times(1)).getTotalNumberNotDeleted();
        verifyNoMoreInteractions(cmsNewsDAO);
    }

    @Test
    public void testAddNews() throws Exception {
        when(cmsNewsDAO.add(any(CmsNews.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addNews")
                .param("authorId", "2")
                .param("newsTypeId", "3")
                .param("newsFolderId", "4")
                .param("newsCode", "newsCode"))
        .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsController.JSON_STATUS).value(CmsNewsController.JSON_STATUS_SUCCESS));
        verify(cmsNewsDAO, times(1)).add(any(CmsNews.class));
        verifyNoMoreInteractions(cmsNewsDAO);
    }

    @Test
    public void testUpdateNewsBasicInfo() throws Exception {
        doNothing().when(cmsNewsDAO).updatePostBasicInfo(any(CmsNews.class));
        when(cmsNewsDAO.get(anyLong())).thenReturn(new CmsNews());
        when(cmsLanguagesDAO.listAll()).thenReturn(new ArrayList<>());
        when(cmsAuthorsDAO.get(anyLong())).thenReturn(new CmsAuthor());
        when(newsTypesDAO.get(anyLong())).thenReturn(new NewsType());
        when(cmsFoldersDAO.get(anyLong())).thenReturn(new CmsFolder());

        ResultActions resultActions = mockMvc.perform(post("/news/updateNewsBasicInfo")
                .param("newsTypeId", "3")
                .param("newsFolderId", "4")
                .param("newsCode", "newsCode")
                .param("status", "NEW"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsController.JSON_STATUS).value(CmsNewsController.JSON_STATUS_SUCCESS));
        verify(cmsNewsDAO, times(1)).updatePostBasicInfo(any(CmsNews.class));
        verifyNoMoreInteractions(cmsNewsDAO);
    }

    @Test
    public void testDeleteFolder() throws Exception {
        doNothing().when(cmsNewsDAO).delete(any(CmsNews.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteNews")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsController.JSON_STATUS).value(CmsNewsController.JSON_STATUS_SUCCESS));
        verify(cmsNewsDAO, times(1)).delete(any(CmsNews.class));
        verifyNoMoreInteractions(cmsNewsDAO);
    }

    @Test
    public void testBrowseNews() throws Exception {
        when(cmsNewsDAO.get(anyLong())).thenReturn(new CmsNews());
        when(cmsLanguagesDAO.listAll()).thenReturn(new ArrayList<>());
        when(cmsAuthorsDAO.get(anyLong())).thenReturn(new CmsAuthor());
        when(newsTypesDAO.get(anyLong())).thenReturn(new NewsType());
        when(cmsFoldersDAO.get(anyLong())).thenReturn(new CmsFolder());

        mockMvc.perform(get("/news/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/news/SingleNews"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/news/SingleNews.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("cmsLanguages", anything()))
                .andExpect(model().attribute("cmsNews", anything()))
                .andExpect(model().attribute("cmsAuthor", anything()))
                .andExpect(model().attribute("newsType", anything()))
                .andExpect(model().attribute("cmsNews", anything()))
                .andExpect(model().attribute("newsFolder", anything()));
    }

    @Test
    public void testGetNewsI18n() throws Exception {
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        String newsTitle = "newsTitle";
        String newsShortcut = "newsShortcut";
        String newsDescription = "newsDescription";
        cmsNewsI18nContent.setNewsTitle(newsTitle);
        cmsNewsI18nContent.setNewsShortcut(newsShortcut);
        cmsNewsI18nContent.setNewsDescription(newsDescription);
        cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.NEW);
        when(cmsNewsI18nContentsDAO.getByLanguageForNews(anyLong(), anyLong())).thenReturn(cmsNewsI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/news/1/2")).andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsController.JSON_STATUS).value(CmsNewsController.JSON_STATUS_SUCCESS))
                .andExpect(jsonPath("$.data.newsTitle").value(newsTitle))
                .andExpect(jsonPath("$.data.newsShortcut").value(newsShortcut))
                .andExpect(jsonPath("$.data.newsDescription").value(newsDescription))
                .andExpect(jsonPath("$.data.status").value(CmsNewsI18nContentStatus.NEW.name()));

        verify(cmsNewsI18nContentsDAO, times(1)).getByLanguageForNews(anyLong(), anyLong());
        verifyNoMoreInteractions(cmsNewsDAO);
    }

    @Test
    public void testAddNewsI18nContent() throws Exception {
        when(cmsNewsI18nContentsDAO.add(any(CmsNewsI18nContent.class))).thenReturn(1L);
        when(cmsNewsDAO.get(anyLong())).thenReturn(new CmsNews());
        when(cmsAuthorsDAO.get(anyLong())).thenReturn(new CmsAuthor());
        when(newsTypesDAO.get(anyLong())).thenReturn(new NewsType());
        when(cmsFoldersDAO.get(anyLong())).thenReturn(new CmsFolder());

        ResultActions resultActions = mockMvc.perform(post("/news/addNewsI18nContent")
                .param("newsId", "1")
                .param("languageId", "2")
                .param("newsTitle", "newsTitle")
                .param("newsShortcut", "newsShortcut")
                .param("newsDescription", "newsDescription"));

        resultActions.andExpect(status().isSeeOther())
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/news/SingleNews.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("cmsLanguages", anything()))
                .andExpect(model().attribute("cmsAuthor", anything()))
                .andExpect(model().attribute("newsType", anything()))
                .andExpect(model().attribute("newsFolder", anything()));
        verify(cmsNewsI18nContentsDAO, times(1)).add(any(CmsNewsI18nContent.class));
        verifyNoMoreInteractions(cmsNewsI18nContentsDAO);
    }

    @Test
    public void testUpdateNewsI18nContent() throws Exception {
        when(cmsNewsI18nContentsDAO.getByLanguageForNews(anyLong(), anyLong())).thenReturn(null);
        when(cmsNewsI18nContentsDAO.add(any(CmsNewsI18nContent.class))).thenReturn(1L);
        when(cmsNewsDAO.get(anyLong())).thenReturn(new CmsNews());
        when(cmsLanguagesDAO.listAll()).thenReturn(new ArrayList<>());
        when(cmsAuthorsDAO.get(anyLong())).thenReturn(new CmsAuthor());
        when(newsTypesDAO.get(anyLong())).thenReturn(new NewsType());
        when(cmsFoldersDAO.get(anyLong())).thenReturn(new CmsFolder());

        ResultActions resultActions = mockMvc.perform(post("/news/updateNewsI18nContent")
                .param("newsId", "1")
                .param("languageId", "2")
                .param("newsTitle", "newsTitle")
                .param("newsShortcut", "newsShortcut")
                .param("newsDescription", "newsDescription"));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsController.JSON_STATUS).value(CmsNewsController.JSON_STATUS_SUCCESS));
        verify(cmsNewsI18nContentsDAO, times(1)).getByLanguageForNews(anyLong(), anyLong());
        verify(cmsNewsI18nContentsDAO, times(1)).add(any(CmsNewsI18nContent.class));
        verifyNoMoreInteractions(cmsNewsI18nContentsDAO);
    }
}