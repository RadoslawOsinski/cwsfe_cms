//package eu.com.cwsfe.cms.web.news;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.dao.DuplicateKeyException;
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
//public class NewsTypesControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private NewsTypesRepository newsTypesDAO;
//
//    @InjectMocks
//    private NewsTypesController newsTypesController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(newsTypesController).build();
//    }
//
//    @Test
//    public void testDefaultView() throws Exception {
//        mockMvc.perform(get("/newsTypes"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("cms/newsTypes/NewsTypes"))
//            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/newsTypes/NewsTypes.js"))
//            .andExpect(model().attribute("breadcrumbs", anything()));
//    }
//
//    @Test
//    public void testListNewsTypes() throws Exception {
//        int iDisplayStart = 0;
//        int iDisplayLength = 2;
//        String sEcho = "1";
//        int numberOfNewsTypes = 1;
//        List<NewsType> cmsNewsTypes = new ArrayList<>();
//        NewsType newsType = new NewsType();
//        String type = "type";
//        long id = 1L;
//        newsType.setId(id);
//        newsType.setType(type);
//        cmsNewsTypes.add(newsType);
//        when(newsTypesDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsNewsTypes);
//        when(newsTypesDAO.countForAjax()).thenReturn(numberOfNewsTypes);
//
//        ResultActions resultActions = mockMvc.perform(get("/newsTypesList")
//            .param("iDisplayStart", String.valueOf(iDisplayStart))
//            .param("iDisplayLength", String.valueOf(iDisplayLength))
//            .param("sEcho", sEcho))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.sEcho").value(sEcho))
//            .andExpect(jsonPath("$.iTotalRecords").value(numberOfNewsTypes))
//            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfNewsTypes))
//            .andExpect(jsonPath("$.aaData").exists())
//            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
//            .andExpect(jsonPath("$.aaData[0].type").value(type))
//            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
//        verify(newsTypesDAO, times(1)).listAjax(anyInt(), anyInt());
//        verify(newsTypesDAO, times(1)).countForAjax();
//        verifyNoMoreInteractions(newsTypesDAO);
//    }
//
//    @Test
//    public void testListNewsTypesForDropList() throws Exception {
//        int limit = 1;
//        List<NewsType> cmsNewsTypes = new ArrayList<>();
//        NewsType newsType = new NewsType();
//        String type = "type";
//        long id = 1L;
//        newsType.setId(id);
//        newsType.setType(type);
//        cmsNewsTypes.add(newsType);
//        when(newsTypesDAO.listNewsTypesForDropList(anyString(), anyInt())).thenReturn(cmsNewsTypes);
//
//        ResultActions resultActions = mockMvc.perform(get("/news/newsTypesDropList")
//            .param("term", type)
//            .param("limit", String.valueOf(limit)))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.data").exists())
//            .andExpect(jsonPath("$.data[0].id").value((int) id))
//            .andExpect(jsonPath("$.data[0].type").value(type));
//        verify(newsTypesDAO, times(1)).listNewsTypesForDropList(anyString(), anyInt());
//        verifyNoMoreInteractions(newsTypesDAO);
//    }
//
//    @Test
//    public void testAddNewsType() throws Exception {
//        String type = "type";
//        when(newsTypesDAO.add(any(NewsType.class))).thenReturn(1L);
//
//        ResultActions resultActions = mockMvc.perform(post("/addNewsType")
//            .param("type", type));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + NewsTypesController.JSON_STATUS).value(NewsTypesController.JSON_STATUS_SUCCESS));
//        verify(newsTypesDAO, times(1)).add(any(NewsType.class));
//        verifyNoMoreInteractions(newsTypesDAO);
//    }
//
//    @Test
//    public void testAddExistingNewsType() throws Exception {
//        String type = "type";
//        when(newsTypesDAO.add(any(NewsType.class))).thenThrow(new DuplicateKeyException("Duplicate"));
//
//        ResultActions resultActions = mockMvc.perform(post("/addNewsType")
//            .param("type", type));
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + NewsTypesController.JSON_STATUS).value(NewsTypesController.JSON_STATUS_FAIL))
//            .andExpect(jsonPath("$." + NewsTypesController.JSON_ERROR_MESSAGES + "[0]").exists());
//        verify(newsTypesDAO, times(1)).add(any(NewsType.class));
//        verifyNoMoreInteractions(newsTypesDAO);
//    }
//
//    @Test
//    public void testDeleteNewsType() throws Exception {
//        int id = 1;
//        doNothing().when(newsTypesDAO).delete(any(NewsType.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/deleteNewsType")
//            .param("id", String.valueOf(id)))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + NewsTypesController.JSON_STATUS).value(NewsTypesController.JSON_STATUS_SUCCESS));
//        verify(newsTypesDAO, times(1)).delete(any(NewsType.class));
//        verifyNoMoreInteractions(newsTypesDAO);
//    }
//}
