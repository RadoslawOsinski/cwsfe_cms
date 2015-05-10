package eu.com.cwsfe.cms.rest;

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

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-rest-test.xml"})
@WebAppConfiguration
public class NewsImagesRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsNewsImagesDAO cmsNewsImagesDAO;

    @InjectMocks
    private NewsImagesRestController newsImagesRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(newsImagesRestController).build();
    }

    @Test
    public void testGetImagesForNews() throws Exception {
        long thumbnailId = 1l;
        String thumbnailFileName = "thumbnailFileName";
        Integer thumbnailHeight = 2;
        Integer thumbnailWidth = 3;
        String thumbnailTitle =  "thumbnailTitle";
        long imageId = 11l;
        String imageFileName = "imageFileName";
        Integer imageHeight = 21;
        Integer imageWidth = 31;
        String imageTitle =  "imageTitle";
        CmsNewsImage thumbnail = new CmsNewsImage();
        thumbnail.setId(thumbnailId);
        thumbnail.setFileName(thumbnailFileName);
        thumbnail.setHeight(thumbnailHeight);
        thumbnail.setWidth(thumbnailWidth);
        thumbnail.setTitle(thumbnailTitle);
        CmsNewsImage image = new CmsNewsImage();
        image.setId(imageId);
        image.setFileName(imageFileName);
        image.setHeight(imageHeight);
        image.setWidth(imageWidth);
        image.setTitle(imageTitle);
        ArrayList<CmsNewsImage> images = new ArrayList<>();
        images.add(image);
        when(cmsNewsImagesDAO.getThumbnailForNews(anyLong())).thenReturn(thumbnail);
        when(cmsNewsImagesDAO.listImagesForNewsWithoutThumbnails(anyLong())).thenReturn(images);

        ResultActions resultActions = mockMvc.perform(get("/rest/newsImages")
                .param("newsId", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.thumbnailImage.id").value((int) thumbnailId))
                .andExpect(jsonPath("$.thumbnailImage.fileName").value(thumbnailFileName))
                .andExpect(jsonPath("$.thumbnailImage.height").value(thumbnailHeight))
                .andExpect(jsonPath("$.thumbnailImage.width").value(thumbnailWidth))
                .andExpect(jsonPath("$.thumbnailImage.title").value(thumbnailTitle))
                .andExpect(jsonPath("$.newsImages[0].id").value((int) imageId))
                .andExpect(jsonPath("$.newsImages[0].fileName").value(imageFileName))
                .andExpect(jsonPath("$.newsImages[0].height").value(imageHeight))
                .andExpect(jsonPath("$.newsImages[0].width").value(imageWidth))
                .andExpect(jsonPath("$.newsImages[0].title").value(imageTitle));
    }
}