package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.dao.CmsNewsImagesDAO;
import eu.com.cwsfe.cms.model.CmsNewsImage;
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
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class CmsNewsImagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsImagesDAO cmsNewsImagesDAO;

    @InjectMocks
    private CmsNewsImagesController cmsNewsImagesController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cmsNewsImagesController).build();
    }

    @Test
    public void testList() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        long cmsNewsId = 1l;
        int numberOfCmsNewsImages = 1;
        int totalNumberNotDeleted = 6;
        List<CmsNewsImage> cmsNewsImages = new ArrayList<>();
        CmsNewsImage blogPostImage = new CmsNewsImage();
        String title = "title";
        long id = 1l;
        blogPostImage.setId(id);
        blogPostImage.setTitle(title);
        cmsNewsImages.add(blogPostImage);
        when(cmsNewsImagesDAO.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, cmsNewsId)).thenReturn(cmsNewsImages);
        when(cmsNewsImagesDAO.searchByAjaxCountWithoutContent(cmsNewsId)).thenReturn(numberOfCmsNewsImages);
        when(cmsNewsImagesDAO.getTotalNumberNotDeleted()).thenReturn(totalNumberNotDeleted);

        ResultActions resultActions = mockMvc.perform(get("/news/cmsNewsImagesList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho)
                .param("cmsNewsId", String.valueOf(cmsNewsId)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(totalNumberNotDeleted))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfCmsNewsImages))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].title").value(title))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(cmsNewsImagesDAO, times(1)).searchByAjaxWithoutContent(anyInt(), anyInt(), anyLong());
        verify(cmsNewsImagesDAO, times(1)).searchByAjaxCountWithoutContent(anyLong());
        verify(cmsNewsImagesDAO, times(1)).getTotalNumberNotDeleted();
        verifyNoMoreInteractions(cmsNewsImagesDAO);
    }

    @Test
    public void testDeleteCmsNewsImage() throws Exception {
        doNothing().when(cmsNewsImagesDAO).delete(any(CmsNewsImage.class));

        ResultActions resultActions = mockMvc.perform(post("/news/deleteCmsNewsImage")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + CmsNewsImagesController.JSON_STATUS).value(CmsNewsImagesController.JSON_STATUS_SUCCESS));
        verify(cmsNewsImagesDAO, times(1)).delete(any(CmsNewsImage.class));
        verifyNoMoreInteractions(cmsNewsImagesDAO);
    }
}