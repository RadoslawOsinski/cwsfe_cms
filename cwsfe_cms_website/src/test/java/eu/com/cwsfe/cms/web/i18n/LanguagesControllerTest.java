package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.model.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class LanguagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsLanguagesDAO cmsLanguagesDAO;

    @InjectMocks
    private LanguagesController languagesController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(languagesController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/languages"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/languages/Languages"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/languages/Languages.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListLanguages() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfLanguages = 1;
        List<Language> cmsLanguages = new ArrayList<>();
        Language language = new Language();
        String code = "code";
        String name = "name";
        long id = 1L;
        language.setId(id);
        language.setCode(code);
        language.setName(name);
        cmsLanguages.add(language);
        when(cmsLanguagesDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsLanguages);
        when(cmsLanguagesDAO.countForAjax()).thenReturn(numberOfLanguages);

        ResultActions resultActions = mockMvc.perform(get("/languagesList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfLanguages))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfLanguages))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].code").value(code))
                .andExpect(jsonPath("$.aaData[0].name").value(name))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(cmsLanguagesDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsLanguagesDAO, times(1)).countForAjax();
        verifyNoMoreInteractions(cmsLanguagesDAO);
    }

    @Test
    public void testListCmsLanguagesForDropList() throws Exception {
        int limit = 1;
        ArrayList<Language> cmsLanguages = new ArrayList<>();
        Language cmsTextI18nCategory = new Language();
        String code = "code";
        String name = "name";
        long id = 1L;
        cmsTextI18nCategory.setId(id);
        cmsTextI18nCategory.setCode(code);
        cmsTextI18nCategory.setName(name);
        cmsLanguages.add(cmsTextI18nCategory);
        when(cmsLanguagesDAO.listForDropList(anyString(), anyInt())).thenReturn(cmsLanguages);

        ResultActions resultActions = mockMvc.perform(get("/cmsLanguagesDropList")
                .param("term", code)
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].id").value((int) id))
                .andExpect(jsonPath("$.data[0].code").value(code))
                .andExpect(jsonPath("$.data[0].name").value(name));
        verify(cmsLanguagesDAO, times(1)).listForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(cmsLanguagesDAO);
    }

    @Test
    public void testAddLanguage() throws Exception {
        String code = "code";
        String name = "name";
        when(cmsLanguagesDAO.add(any(Language.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addLanguage")
                .param("code", code)
                .param("name", name))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + LanguagesController.JSON_STATUS).value(LanguagesController.JSON_STATUS_SUCCESS));
        verify(cmsLanguagesDAO, times(1)).add(any(Language.class));
        verifyNoMoreInteractions(cmsLanguagesDAO);
    }

    @Test
    public void testAddExistingLanguage() throws Exception {
        String code = "code";
        String name = "name";
        when(cmsLanguagesDAO.add(any(Language.class))).thenThrow(new DuplicateKeyException("Duplicate"));

        ResultActions resultActions = mockMvc.perform(post("/addLanguage")
                .param("code", code)
                .param("name", name));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + LanguagesController.JSON_STATUS).value(LanguagesController.JSON_STATUS_FAIL))
                .andExpect(jsonPath("$." + LanguagesController.JSON_ERROR_MESSAGES + "[0]").exists());
        verify(cmsLanguagesDAO, times(1)).add(any(Language.class));
        verifyNoMoreInteractions(cmsLanguagesDAO);
    }

    @Test
    public void testDeleteLanguage() throws Exception {
        int id = 1;
        doNothing().when(cmsLanguagesDAO).delete(any(Language.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteLanguage")
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + LanguagesController.JSON_STATUS).value(LanguagesController.JSON_STATUS_SUCCESS));
        verify(cmsLanguagesDAO, times(1)).delete(any(Language.class));
        verifyNoMoreInteractions(cmsLanguagesDAO);
    }
}