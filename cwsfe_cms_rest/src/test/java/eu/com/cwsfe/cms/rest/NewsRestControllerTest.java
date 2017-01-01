package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.configuration.RestTestConfiguration;
import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.domains.CmsNewsI18nContentStatus;
import eu.com.cwsfe.cms.domains.CmsNewsStatus;
import eu.com.cwsfe.cms.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestTestConfiguration.class})
@WebAppConfiguration
public class NewsRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsDAO cmsNewsDAO;
    @Mock
    private CmsFoldersDAO cmsFoldersDAO;
    @Mock
    private NewsTypesDAO newsTypesDAO;
    @Mock
    private CmsLanguagesDAO cmsLanguagesDAO;
    @Mock
    private CmsNewsI18nContentsDAO cmsNewsI18nContentsDAO;

    @InjectMocks
    private NewsRestController newsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(newsRestController).build();
    }

    @Test
    public void testGetNewsByNewsTypeFolderAndNewsCode() throws Exception {
        long id = 1L;
        long authorId = 2L;
        long newsTypeId = 3L;
        long newsFolderId = 4L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setId(1L);
        NewsType newsType = new NewsType();
        newsType.setId(2L);
        String testCode = "testCode";
        CmsNews cmsNews = new CmsNews();
        cmsNews.setId(id);
        cmsNews.setAuthorId(authorId);
        cmsNews.setNewsTypeId(newsTypeId);
        cmsNews.setNewsFolderId(newsFolderId);
        cmsNews.setNewsCode(testCode);
        cmsNews.setStatus(CmsNewsStatus.PUBLISHED);
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(cmsFolder);
        when(newsTypesDAO.getByType(anyString())).thenReturn(newsType);
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenReturn(cmsNews);

        ResultActions resultActions = mockMvc.perform(get("/rest/news")
                .param("newsType", "Services")
                .param("folderName", "Services")
                .param("newsCode", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.authorId").value((int) authorId))
                .andExpect(jsonPath("$.newsTypeId").value((int) newsTypeId))
                .andExpect(jsonPath("$.newsFolderId").value((int) newsFolderId))
                .andExpect(jsonPath("$.newsCode").value(testCode));
    }

    @Ignore("Fix this failing test")
    @Test
    public void testListByFolderLangAndNewsWithPaging() throws Exception {
        long id = 1L;
        long id2 = 2L;
        long authorId = 2L;
        long newsTypeId = 3L;
        long newsFolderId = 4L;
        long languageId = 5L;
        String newsCode = "newsCode";
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        CmsNews cmsNews = new CmsNews();
        cmsNews.setId(id);
        cmsNews.setAuthorId(authorId);
        cmsNews.setNewsTypeId(newsTypeId);
        cmsNews.setNewsFolderId(newsFolderId);
        cmsNews.setNewsCode(newsCode);
        cmsNews.setStatus(CmsNewsStatus.PUBLISHED);
        ArrayList<Object[]> values = new ArrayList<>();
        Object[] value = {1L, 1L};
        values.add(value);
        CmsFolder folder = new CmsFolder();
        folder.setId(7L);
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(folder);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(new Language());
        when(cmsNewsDAO.listByFolderLangAndNewsWithPaging(anyInt(), anyLong(), anyString(), anyInt(), anyInt())).thenReturn(values);
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenReturn(cmsNews);
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setId(id2);
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setLanguageId(languageId);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setNewsId(id);
        when(cmsNewsI18nContentsDAO.get(anyLong())).thenReturn(cmsNewsI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nPairs")
                .param("newsPerPage", "1")
                .param("offset", "1")
                .param("folderName", "folderName")
                .param("languageCode", "languageCode")
                .param("newsType", "newsType")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].cmsNews.id").value((int) id))
                .andExpect(jsonPath("$[0].cmsNews.authorId").value((int) authorId))
                .andExpect(jsonPath("$[0].cmsNews.newsTypeId").value((int) newsTypeId))
                .andExpect(jsonPath("$[0].cmsNews.newsFolderId").value((int) newsFolderId))
                .andExpect(jsonPath("$[0].cmsNews.newsCode").value(newsCode))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.id").value((int) id2))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.newsTitle").value(title))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.newsShortcut").value(shortcut))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.languageId").value((int) languageId))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.newsDescription").value(description))
                .andExpect(jsonPath("$[0].cmsNewsI18nContent.newsId").value((int) id))
                ;
    }

    @Ignore("Fix this failing test")
    @Test
    public void testCountListByFolderLangAndNewsWithPaging() throws Exception {
        CmsFolder folder = new CmsFolder();
        folder.setId(7L);
        int total = 1;
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(folder);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(new Language());
        when(cmsNewsDAO.countListByFolderLangAndNewsWithPaging(anyInt(), anyLong(), anyString())).thenReturn(total);

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nPairsTotal")
                .param("folderName", "folderName")
                .param("languageCode", "languageCode")
                .param("newsType", "newsType")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.total").value(total));
    }

    @Test
    public void testGetByLanguageForNews() throws Exception {
        Language language = new Language();
        language.setId(1L);
        long id = 1L;
        long newsId = 2L;
        String description = "desc";
        long languageId = 3L;
        String shortcut = "shortcut";
        String title = "title";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setId(id);
        cmsNewsI18nContent.setNewsId(newsId);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setLanguageId(languageId);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.NEW);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        when(cmsNewsI18nContentsDAO.getByLanguageForNews(anyLong(), anyLong())).thenReturn(cmsNewsI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nContent")
                .param("languageCode", "en")
                .param("newsId", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.newsId").value((int) newsId))
                .andExpect(jsonPath("$.languageId").value((int) languageId))
                .andExpect(jsonPath("$.newsDescription").value(description))
                .andExpect(jsonPath("$.newsShortcut").value(shortcut))
                .andExpect(jsonPath("$.newsTitle").value(title));
    }

    @Test
    public void testGet() throws Exception {
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        Long id = 1L;
        Long newsId = 2L;
        String description = "description";
        Long languageId = 3L;
        String shortcut = "shortcut";
        String title = "title";
        cmsNewsI18nContent.setId(id);
        cmsNewsI18nContent.setNewsId(newsId);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setLanguageId(languageId);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.NEW);
        when(cmsNewsI18nContentsDAO.get(anyLong())).thenReturn(cmsNewsI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/singleNewsI18nContent")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.newsId").value(newsId.intValue()))
                .andExpect(jsonPath("$.languageId").value(languageId.intValue()))
                .andExpect(jsonPath("$.newsDescription").value(description))
                .andExpect(jsonPath("$.newsShortcut").value(shortcut))
                .andExpect(jsonPath("$.newsTitle").value(title));
    }

    @Test
    public void testGetNewsI18nContentByNews() throws Exception {
        long id = 1L;
        long authorId = 2L;
        long newsTypeId = 3L;
        long newsFolderId = 4L;
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setId(1L);
        NewsType newsType = new NewsType();
        newsType.setId(2L);
        String testCode = "testCode";
        CmsNews cmsNews = new CmsNews();
        cmsNews.setId(id);
        cmsNews.setAuthorId(authorId);
        cmsNews.setNewsTypeId(newsTypeId);
        cmsNews.setNewsFolderId(newsFolderId);
        cmsNews.setNewsCode(testCode);
        cmsNews.setStatus(CmsNewsStatus.NEW);
        Language language = new Language();
        language.setId(1L);
        long newsId = 2L;
        String description = "desc";
        long languageId = 3L;
        String shortcut = "shortcut";
        String title = "title";
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setId(id);
        cmsNewsI18nContent.setNewsId(newsId);
        cmsNewsI18nContent.setNewsDescription(description);
        cmsNewsI18nContent.setLanguageId(languageId);
        cmsNewsI18nContent.setNewsShortcut(shortcut);
        cmsNewsI18nContent.setNewsTitle(title);
        cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.PUBLISHED);
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(cmsFolder);
        when(newsTypesDAO.getByType(anyString())).thenReturn(newsType);
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenReturn(cmsNews);
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(language);
        when(cmsNewsI18nContentsDAO.getByLanguageForNews(anyLong(), anyLong())).thenReturn(cmsNewsI18nContent);

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nContentByNews")
                .param("languageCode", "en")
                .param("newsType", "Services")
                .param("folderName", "Services")
                .param("newsCode", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.newsId").value((int) newsId))
                .andExpect(jsonPath("$.languageId").value((int) languageId))
                .andExpect(jsonPath("$.newsDescription").value(description))
                .andExpect(jsonPath("$.newsShortcut").value(shortcut))
                .andExpect(jsonPath("$.newsTitle").value(title));
    }

    @Ignore("Fix this failing test")
    @Test
    public void testGetNewsI18nContentByNonExistingNews() throws Exception {
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(new CmsFolder());
        when(newsTypesDAO.getByType(anyString())).thenReturn(new NewsType());
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenThrow(new EmptyResultDataAccessException(1));

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nContentByNews")
                .param("languageCode", "en")
                .param("newsType", "Services")
                .param("folderName", "Services")
                .param("newsCode", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Ignore("Fix this failing test")
    @Test
    public void testGetNewsI18nContentByNonExistingI18n() throws Exception {
        when(cmsFoldersDAO.getByFolderName(anyString())).thenReturn(new CmsFolder());
        when(newsTypesDAO.getByType(anyString())).thenReturn(new NewsType());
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenReturn(new CmsNews());
        when(cmsLanguagesDAO.getByCode(anyString())).thenReturn(new Language());
        when(cmsNewsI18nContentsDAO.getByLanguageForNews(anyLong(), anyLong())).thenThrow(new EmptyResultDataAccessException(1));

        ResultActions resultActions = mockMvc.perform(get("/rest/newsI18nContentByNews")
                .param("languageCode", "en")
                .param("newsType", "Services")
                .param("folderName", "Services")
                .param("newsCode", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

}
