package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.dao.CmsNewsImagesDAO;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsNewsImage;
import eu.com.cwsfe.cms.web.images.ImageStorageService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class CmsNewsImagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsImagesRepository cmsNewsImagesDAO;
    @Mock
    private ImageStorageService imageStorageService;
    @Mock
    private CmsGlobalParamsRepository cmsGlobalParamsDAO;

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
        long cmsNewsId = 1L;
        int numberOfCmsNewsImages = 1;
        int totalNumberNotDeleted = 6;
        List<CmsNewsImage> cmsNewsImages = new ArrayList<>();
        CmsNewsImage blogPostImage = new CmsNewsImage();
        String title = "title";
        String fileName = "fileName";
        String url = "http://...path to image";
        long id = 1L;
        blogPostImage.setId(id);
        blogPostImage.setTitle(title);
        blogPostImage.setFileName(fileName);
        blogPostImage.setUrl(url);
        cmsNewsImages.add(blogPostImage);
        when(cmsNewsImagesDAO.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, cmsNewsId)).thenReturn(cmsNewsImages);
        when(cmsNewsImagesDAO.searchByAjaxCountWithoutContent(cmsNewsId)).thenReturn(numberOfCmsNewsImages);
        when(cmsNewsImagesDAO.getTotalNumberNotDeleted()).thenReturn(totalNumberNotDeleted);

        ResultActions resultActions = mockMvc.perform(get("/news/cmsNewsImagesList")
            .param("iDisplayStart", String.valueOf(iDisplayStart))
            .param("iDisplayLength", String.valueOf(iDisplayLength))
            .param("sEcho", sEcho)
            .param("cmsNewsId", String.valueOf(cmsNewsId)));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.sEcho").value(sEcho))
            .andExpect(jsonPath("$.iTotalRecords").value(totalNumberNotDeleted))
            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfCmsNewsImages))
            .andExpect(jsonPath("$.aaData").exists())
            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
            .andExpect(jsonPath("$.aaData[0].title").value(title))
            .andExpect(jsonPath("$.aaData[0].fileName").value(fileName))
            .andExpect(jsonPath("$.aaData[0].url").value(url))
            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(cmsNewsImagesDAO, times(1)).searchByAjaxWithoutContent(anyInt(), anyInt(), anyLong());
        verify(cmsNewsImagesDAO, times(1)).searchByAjaxCountWithoutContent(anyLong());
        verify(cmsNewsImagesDAO, times(1)).getTotalNumberNotDeleted();
        verifyNoMoreInteractions(cmsNewsImagesDAO);
    }

    @Test
    public void testAddCmsNewsImage() throws Exception {
        URL resource = getClass().getResource("/testImage.png");
        Path path = Paths.get(resource.toURI());
        byte[] testData = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile("file", "testImage.png", "image/png", testData);

        when(cmsNewsImagesDAO.add(any(CmsNewsImage.class))).thenReturn(1L);
        doNothing().when(cmsNewsImagesDAO).updateUrl(any(CmsNewsImage.class));
        when(imageStorageService.storeNewsImage(any(CmsNewsImage.class))).thenReturn("http://url to image");
        CmsGlobalParam cmsGlobalParam = mock(CmsGlobalParam.class);
        when(cmsGlobalParamsDAO.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH")).thenReturn(cmsGlobalParam);
        when(cmsGlobalParam.getValue()).thenReturn(System.getProperty("java.io.tmpdir"));

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.fileUpload("/news/addCmsNewsImage")
                .file(file)
                .param("newsId", "1")
                .param("title", "testImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void testDeleteCmsNewsImage() throws Exception {
        doNothing().when(cmsNewsImagesDAO).delete(any(CmsNewsImage.class));

        ResultActions resultActions = mockMvc.perform(post("/news/deleteCmsNewsImage")
            .param("id", "1"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + CmsNewsImagesController.JSON_STATUS).value(CmsNewsImagesController.JSON_STATUS_SUCCESS));
        verify(cmsNewsImagesDAO, times(1)).delete(any(CmsNewsImage.class));
        verifyNoMoreInteractions(cmsNewsImagesDAO);
    }
}
