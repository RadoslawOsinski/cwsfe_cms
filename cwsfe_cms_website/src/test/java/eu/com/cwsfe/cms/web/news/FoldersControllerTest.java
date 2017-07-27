//package eu.com.cwsfe.cms.web.news;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
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
//import static org.hamcrest.Matchers.anything;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
//@WebAppConfiguration
//@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
//public class FoldersControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private CmsFoldersRepository cmsFoldersDAO;
//
//    @InjectMocks
//    private FoldersController foldersController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(foldersController).build();
//    }
//
//    @Test
//    public void testDefaultView() throws Exception {
//        mockMvc.perform(get("/folders"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("cms/folders/Folders"))
//            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/folders/Folders.js"))
//            .andExpect(model().attribute("breadcrumbs", anything()));
//    }
//
//    @Test
//    public void testListFolders() throws Exception {
//        int iDisplayStart = 0;
//        int iDisplayLength = 2;
//        String sEcho = "1";
//        int numberOfCmsFolders = 1;
//        List<CmsFolder> cmsFolders = new ArrayList<>();
//        CmsFolder cmsFolder = new CmsFolder();
//        String folderName = "folderName";
//        long id = 1L;
//        long orderNumber = 2L;
//        cmsFolder.setId(id);
//        cmsFolder.setFolderName(folderName);
//        cmsFolder.setOrderNumber(orderNumber);
//        cmsFolders.add(cmsFolder);
//        when(cmsFoldersDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsFolders);
//        when(cmsFoldersDAO.countForAjax()).thenReturn(numberOfCmsFolders);
//
//        ResultActions resultActions = mockMvc.perform(get("/foldersList")
//            .param("iDisplayStart", String.valueOf(iDisplayStart))
//            .param("iDisplayLength", String.valueOf(iDisplayLength))
//            .param("sEcho", sEcho))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.sEcho").value(sEcho))
//            .andExpect(jsonPath("$.iTotalRecords").value(numberOfCmsFolders))
//            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfCmsFolders))
//            .andExpect(jsonPath("$.aaData").exists())
//            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
//            .andExpect(jsonPath("$.aaData[0].folderName").value(folderName))
//            .andExpect(jsonPath("$.aaData[0].orderNumber").value((int) orderNumber))
//            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
//        verify(cmsFoldersDAO, times(1)).listAjax(anyInt(), anyInt());
//        verify(cmsFoldersDAO, times(1)).countForAjax();
//        verifyNoMoreInteractions(cmsFoldersDAO);
//    }
//
//    @Test
//    public void testListFoldersForDropList() throws Exception {
//        int limit = 1;
//        List<CmsFolder> cmsFolders = new ArrayList<>();
//        CmsFolder cmsFolder = new CmsFolder();
//        String folderName = "folderName";
//        long id = 1L;
//        cmsFolder.setId(id);
//        cmsFolder.setFolderName(folderName);
//        cmsFolders.add(cmsFolder);
//        when(cmsFoldersDAO.listFoldersForDropList(anyString(), anyInt())).thenReturn(cmsFolders);
//
//        ResultActions resultActions = mockMvc.perform(get("/news/foldersDropList")
//            .param("term", folderName)
//            .param("limit", String.valueOf(limit)))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.data").exists())
//            .andExpect(jsonPath("$.data[0].id").value((int) id))
//            .andExpect(jsonPath("$.data[0].folderName").value(folderName));
//        verify(cmsFoldersDAO, times(1)).listFoldersForDropList(anyString(), anyInt());
//        verifyNoMoreInteractions(cmsFoldersDAO);
//    }
//
//    @Test
//    public void testAddFolder() throws Exception {
//        CmsFolder cmsFolder = new CmsFolder();
//        String folderName = "folderName";
//        long id = 1L;
//        cmsFolder.setId(id);
//        cmsFolder.setFolderName(folderName);
//        when(cmsFoldersDAO.add(any(CmsFolder.class))).thenReturn(1L);
//
//        ResultActions resultActions = mockMvc.perform(post("/addFolder")
//            .param("folderName", folderName))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + FoldersController.JSON_STATUS).value(FoldersController.JSON_STATUS_SUCCESS));
//        verify(cmsFoldersDAO, times(1)).add(any(CmsFolder.class));
//        verifyNoMoreInteractions(cmsFoldersDAO);
//    }
//
//    @Test
//    public void testDeleteFolder() throws Exception {
//        int id = 1;
//        doNothing().when(cmsFoldersDAO).delete(any(CmsFolder.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/deleteFolder")
//            .param("id", String.valueOf(id)))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + FoldersController.JSON_STATUS).value(FoldersController.JSON_STATUS_SUCCESS));
//        verify(cmsFoldersDAO, times(1)).delete(any(CmsFolder.class));
//        verifyNoMoreInteractions(cmsFoldersDAO);
//    }
//}
