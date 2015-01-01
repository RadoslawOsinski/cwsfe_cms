package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsNewsDAO;
import eu.com.cwsfe.cms.model.CmsNews;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-rest-test.xml"})
@WebAppConfiguration
public class NewsRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsDAO cmsNewsDAO;

    @InjectMocks
    private NewsRestController newsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(newsRestController).build();

    }

    @Test
    public void testGetNewsByNewsTypeFolderAndNewsCode() throws Exception {
        long id = 1l;
        long authorId = 2l;
        long newsTypeId = 3l;
        long newsFolderId = 4l;
        String testCode = "testCode";
        String status = "P";
        CmsNews cmsNews = new CmsNews();
        cmsNews.setId(id);
        cmsNews.setAuthorId(authorId);
        cmsNews.setNewsTypeId(newsTypeId);
        cmsNews.setNewsFolderId(newsFolderId);
        cmsNews.setNewsCode(testCode);
        cmsNews.setStatus(status);
        when(cmsNewsDAO.getByNewsTypeFolderAndNewsCode(anyLong(), anyLong(), anyString())).thenReturn(cmsNews);

        ResultActions resultActions = mockMvc.perform(get("/rest/news")
                .param("newsTypeId", "1")
                .param("folderId", "1")
                .param("newsCode", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.authorId").value((int) authorId))
                .andExpect(jsonPath("$.newsTypeId").value((int) newsTypeId))
                .andExpect(jsonPath("$.newsFolderId").value((int) newsFolderId))
                .andExpect(jsonPath("$.newsCode").value(testCode))
                .andExpect(jsonPath("$.status").value(status))
        ;
    }

}
