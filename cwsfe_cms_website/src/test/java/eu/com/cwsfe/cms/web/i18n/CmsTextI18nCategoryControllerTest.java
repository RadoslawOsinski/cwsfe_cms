package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.dao.CmsTextI18nCategoryDAO;
import eu.com.cwsfe.cms.domains.CmsTextI18nCategoryStatus;
import eu.com.cwsfe.cms.model.CmsTextI18nCategory;
import org.junit.Before;
import org.junit.Ignore;
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

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class CmsTextI18nCategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsTextI18nCategoryDAO cmsTextI18nCategoryDAO;

    @InjectMocks
    private CmsTextI18nCategoryController cmsTextI18nCategoryController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cmsTextI18nCategoryController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/cmsTextI18nCategories"))
            .andExpect(status().isOk())
            .andExpect(view().name("cms/textI18nCategories/TextI18nCategories"))
            .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/textI18nCategories/TextI18nCategories.js"))
            .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListCmsTextI18nCategories() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfCategories = 1;
        ArrayList<CmsTextI18nCategory> cmsTextI18nCategories = new ArrayList<>();
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        String category = "category";
        long id = 1L;
        cmsTextI18nCategory.setId(id);
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategory.setStatus(CmsTextI18nCategoryStatus.NEW);
        cmsTextI18nCategories.add(cmsTextI18nCategory);
        when(cmsTextI18nCategoryDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(cmsTextI18nCategories);
        when(cmsTextI18nCategoryDAO.countForAjax()).thenReturn(numberOfCategories);

        ResultActions resultActions = mockMvc.perform(get("/cmsTextI18nCategoriesList")
            .param("iDisplayStart", String.valueOf(iDisplayStart))
            .param("iDisplayLength", String.valueOf(iDisplayLength))
            .param("sEcho", sEcho))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.sEcho").value(sEcho))
            .andExpect(jsonPath("$.iTotalRecords").value(numberOfCategories))
            .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfCategories))
            .andExpect(jsonPath("$.aaData").exists())
            .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
            .andExpect(jsonPath("$.aaData[0].category").value(category))
            .andExpect(jsonPath("$.aaData[0].status").value(CmsTextI18nCategoryStatus.NEW.name()))
            .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(cmsTextI18nCategoryDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsTextI18nCategoryDAO, times(1)).countForAjax();
        verifyNoMoreInteractions(cmsTextI18nCategoryDAO);
    }

    @Test
    public void testListCmsLanguagesForDropList() throws Exception {
        int limit = 1;
        ArrayList<CmsTextI18nCategory> cmsTextI18nCategories = new ArrayList<>();
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        String category = "category";
        long id = 1L;
        cmsTextI18nCategory.setId(id);
        cmsTextI18nCategory.setCategory(category);
        cmsTextI18nCategories.add(cmsTextI18nCategory);
        when(cmsTextI18nCategoryDAO.listForDropList(anyString(), anyInt())).thenReturn(cmsTextI18nCategories);

        ResultActions resultActions = mockMvc.perform(get("/cmsTextI18nCategoryDropList")
            .param("term", category)
            .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data[0].id").value((int) id))
            .andExpect(jsonPath("$.data[0].category").value(category));
        verify(cmsTextI18nCategoryDAO, times(1)).listForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(cmsTextI18nCategoryDAO);
    }

    @Test
    public void testAddTextI18nCategory() throws Exception {
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        String category = "category";
        long id = 1L;
        cmsTextI18nCategory.setId(id);
        cmsTextI18nCategory.setCategory(category);
        when(cmsTextI18nCategoryDAO.add(any(CmsTextI18nCategory.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addCmsTextI18nCategory")
            .param("category", category))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + CmsTextI18nCategoryController.JSON_STATUS).value(CmsTextI18nCategoryController.JSON_STATUS_SUCCESS));
        verify(cmsTextI18nCategoryDAO, times(1)).add(any(CmsTextI18nCategory.class));
        verifyNoMoreInteractions(cmsTextI18nCategoryDAO);
    }

    @Test
    public void testDeleteTextI18nCategory() throws Exception {
        int id = 1;
        doNothing().when(cmsTextI18nCategoryDAO).delete(any(CmsTextI18nCategory.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteCmsTextI18nCategory")
            .param("id", String.valueOf(id)))
            .andExpect(status().isOk());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$." + CmsTextI18nCategoryController.JSON_STATUS).value(CmsTextI18nCategoryController.JSON_STATUS_SUCCESS));
        verify(cmsTextI18nCategoryDAO, times(1)).delete(any(CmsTextI18nCategory.class));
        verifyNoMoreInteractions(cmsTextI18nCategoryDAO);
    }
}
