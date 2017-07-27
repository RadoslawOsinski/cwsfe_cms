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
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
//@WebAppConfiguration
//@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
//public class BlogPostImagesControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private BlogPostImagesRepository blogPostImagesDAO;
//    @Mock
//    private ImageStorageService imageStorageService;
//    @Mock
//    private CmsGlobalParamsRepository cmsGlobalParamsDAO;
//
//    @InjectMocks
//    private BlogPostImagesController blogPostImagesController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(blogPostImagesController).build();
//    }
//
//    @Test
//    public void testListBlogPostImages() throws Exception {
//        int iDisplayStart = 0;
//        int iDisplayLength = 2;
//        String sEcho = "1";
//        long blogPostId = 1L;
//        int numberOfBlogPostImages = 1;
//        int totalNumberNotDeleted = 6;
//        List<BlogPostImage> blogPostImages = new ArrayList<>();
//        BlogPostImage blogPostImage = new BlogPostImage();
//        String title = "title";
//        String fileName = "fileName";
//        String url = "http://path to blog image";
//        long id = 1L;
//        blogPostImage.setId(id);
//        blogPostImage.setTitle(title);
//        blogPostImage.setFileName(fileName);
//        blogPostImage.setUrl(url);
//        blogPostImages.add(blogPostImage);
//        when(blogPostImagesDAO.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, blogPostId)).thenReturn(blogPostImages);
//        when(blogPostImagesDAO.searchByAjaxCountWithoutContent(blogPostId)).thenReturn(numberOfBlogPostImages);
//        when(blogPostImagesDAO.getTotalNumberNotDeleted()).thenReturn(totalNumberNotDeleted);
//
//        ResultActions resultActions = mockMvc.perform(get("/blogPosts/blogPostImagesList")
//            .param("iDisplayStart", String.valueOf(iDisplayStart))
//            .param("iDisplayLength", String.valueOf(iDisplayLength))
//            .param("sEcho", sEcho)
//            .param("blogPostId", String.valueOf(blogPostId)));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.sEcho").value(sEcho))
//            .andExpect(jsonPath("$.iTotalRecords").value(totalNumberNotDeleted))
//            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfBlogPostImages))
//            .andExpect(jsonPath("$.aaData").exists())
//            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
//            .andExpect(jsonPath("$.aaData[0].title").value(title))
//            .andExpect(jsonPath("$.aaData[0].fileName").value(fileName))
//            .andExpect(jsonPath("$.aaData[0].url").value(url))
//            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
//        verify(blogPostImagesDAO, times(1)).searchByAjaxWithoutContent(anyInt(), anyInt(), anyLong());
//        verify(blogPostImagesDAO, times(1)).searchByAjaxCountWithoutContent(anyLong());
//        verify(blogPostImagesDAO, times(1)).getTotalNumberNotDeleted();
//        verifyNoMoreInteractions(blogPostImagesDAO);
//    }
//
//    @Test
//    public void testAddBlogPostImage() throws Exception {
//        URL resource = getClass().getResource("/testImage.png");
//        Path path = Paths.get(resource.toURI());
//        byte[] testData = Files.readAllBytes(path);
//        MockMultipartFile file = new MockMultipartFile("file", "testImage.png", "image/png", testData);
//
//        when(blogPostImagesDAO.add(any(BlogPostImage.class))).thenReturn(1L);
//        doNothing().when(blogPostImagesDAO).updateUrl(any(BlogPostImage.class));
//        when(imageStorageService.storeBlogImage(any(BlogPostImage.class))).thenReturn("http://url to image");
//        CmsGlobalParam cmsGlobalParam = mock(CmsGlobalParam.class);
//        when(cmsGlobalParamsDAO.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH")).thenReturn(cmsGlobalParam);
//        when(cmsGlobalParam.getValue()).thenReturn(System.getProperty("java.io.tmpdir"));
//
//        ResultActions resultActions = mockMvc.perform(
//            MockMvcRequestBuilders.fileUpload("/blogPosts/addBlogPostImage")
//                .file(file)
//                .param("blogPostId", "1")
//                .param("title", "testImage")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//        );
//
//        resultActions.andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    public void testDeleteBlogPostImage() throws Exception {
//        doNothing().when(blogPostImagesDAO).delete(any(BlogPostImage.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/blogPosts/deleteBlogPostImage")
//            .param("id", "1"));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + BlogPostImagesController.JSON_STATUS).value(BlogPostImagesController.JSON_STATUS_SUCCESS));
//        verify(blogPostImagesDAO, times(1)).delete(any(BlogPostImage.class));
//        verifyNoMoreInteractions(blogPostImagesDAO);
//    }
//}
