//package eu.com.cwsfe.cms.web.i18n;
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
//public class CmsTextI18nControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private CmsTextI18nRepository cmsTextI18nDAO;
//    @Mock
//    private CmsTextI18nCategoryRepository cmsTextI18nCategoryDAO;
//    @Mock
//    private CmsLanguagesRepository cmsLanguagesDAO;
//
//    @InjectMocks
//    private CmsTextI18nController cmsTextI18nController;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(cmsTextI18nController).build();
//    }
//
//    @Test
//    public void testDefaultView() throws Exception {
//        mockMvc.perform(get("/cmsTextI18n"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("cms/textI18n/TextI18n"))
//            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/textI18n/TextI18n.js"))
//            .andExpect(model().attribute("breadcrumbs", anything()));
//    }
//
//    @Test
//    public void testListCmsTextI18n() throws Exception {
//        int iDisplayStart = 0;
//        int iDisplayLength = 2;
//        String sEcho = "1";
//        int numberOfI18ns = 1;
//        long id = 1L;
//        List<CmsTextI18n> cmsTextI18ns = new ArrayList<>();
//        CmsTextI18n cmsTextI18n = new CmsTextI18n();
//        long categoryId = 1L;
//        long langId = 3L;
//        String key = "key";
//        String i18nText = "i18nText";
//        cmsTextI18n.setId(id);
//        cmsTextI18n.setI18nCategory(categoryId);
//        cmsTextI18n.setI18nText(i18nText);
//        cmsTextI18n.setLangId(langId);
//        cmsTextI18n.setI18nKey(key);
//        cmsTextI18ns.add(cmsTextI18n);
//        Language language = new Language();
//        String languageCode = "en";
//        language.setCode(languageCode);
//        when(cmsTextI18nDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsTextI18ns);
//        when(cmsTextI18nDAO.countForAjax()).thenReturn(numberOfI18ns);
//        when(cmsLanguagesDAO.getById(anyLong())).thenReturn(language);
//        CmsTextI18nCategory category = new CmsTextI18nCategory();
//        String categoryText = "categoryText";
//        category.setCategory(categoryText);
//        when(cmsTextI18nCategoryDAO.get(anyLong())).thenReturn(category);
//
//        ResultActions resultActions = mockMvc.perform(get("/cmsTextI18nList")
//            .param("iDisplayStart", String.valueOf(iDisplayStart))
//            .param("iDisplayLength", String.valueOf(iDisplayLength))
//            .param("sEcho", sEcho))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$.sEcho").value(sEcho))
//            .andExpect(jsonPath("$.iTotalRecords").value(numberOfI18ns))
//            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfI18ns))
//            .andExpect(jsonPath("$.aaData").exists())
//            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
//            .andExpect(jsonPath("$.aaData[0].language").value(languageCode))
//            .andExpect(jsonPath("$.aaData[0].category").value(categoryText))
//            .andExpect(jsonPath("$.aaData[0].key").value(key))
//            .andExpect(jsonPath("$.aaData[0].text").value(i18nText))
//            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
//        verify(cmsTextI18nDAO, times(1)).listAjax(anyInt(), anyInt());
//        verify(cmsTextI18nDAO, times(1)).countForAjax();
//        verifyNoMoreInteractions(cmsTextI18nDAO);
//    }
//
//    @Test
//    public void testAddTextI18n() throws Exception {
//        when(cmsTextI18nDAO.add(any(CmsTextI18n.class))).thenReturn(1L);
//
//        ResultActions resultActions = mockMvc.perform(post("/addCmsTextI18n")
//            .param("langId", "1")
//            .param("i18nCategory", "2")
//            .param("i18nKey", "i18nKey")
//            .param("i18nText", "i18nText"))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + CmsTextI18nController.JSON_STATUS).value(CmsTextI18nController.JSON_STATUS_SUCCESS));
//        verify(cmsTextI18nDAO, times(1)).add(any(CmsTextI18n.class));
//        verifyNoMoreInteractions(cmsTextI18nDAO);
//    }
//
//    @Test
//    public void testAddExistingTextI18n() throws Exception {
//        when(cmsTextI18nDAO.add(any(CmsTextI18n.class))).thenThrow(new DuplicateKeyException("Duplicate!"));
//
//        ResultActions resultActions = mockMvc.perform(post("/addCmsTextI18n")
//            .param("langId", "1")
//            .param("i18nCategory", "2")
//            .param("i18nKey", "i18nKey")
//            .param("i18nText", "i18nText"))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + CmsTextI18nController.JSON_STATUS).value(CmsTextI18nController.JSON_STATUS_FAIL))
//            .andExpect(jsonPath("$." + CmsTextI18nController.JSON_ERROR_MESSAGES + "[0]").exists());
//        verify(cmsTextI18nDAO, times(1)).add(any(CmsTextI18n.class));
//        verifyNoMoreInteractions(cmsTextI18nDAO);
//    }
//
//    @Test
//    public void testDeleteTextI18n() throws Exception {
//        int id = 1;
//        doNothing().when(cmsTextI18nDAO).delete(any(CmsTextI18n.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/deleteCmsTextI18n")
//            .param("id", String.valueOf(id)))
//            .andExpect(status().isOk());
//
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//            .andExpect(jsonPath("$." + CmsTextI18nController.JSON_STATUS).value(CmsTextI18nController.JSON_STATUS_SUCCESS));
//        verify(cmsTextI18nDAO, times(1)).delete(any(CmsTextI18n.class));
//        verifyNoMoreInteractions(cmsTextI18nDAO);
//    }
//}
