package eu.com.cwsfe.cms.web.newsletter;

import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailAddressDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailGroupDAO;
import eu.com.cwsfe.cms.domains.NewsletterMailAddressStatus;
import eu.com.cwsfe.cms.model.Language;
import eu.com.cwsfe.cms.model.NewsletterMailAddress;
import eu.com.cwsfe.cms.model.NewsletterMailGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class NewsletterMailGroupsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsletterMailGroupDAO newsletterMailGroupDAO;
    @Mock
    private NewsletterMailAddressDAO newsletterMailAddressDAO;
    @Mock
    private CmsLanguagesDAO cmsLanguagesDAO;

    @InjectMocks
    private NewsletterMailGroupsController newsletterMailGroupsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(newsletterMailGroupsController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/newsletterMailGroups"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/newsletterMailGroups/NewsletterMailGroups"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/newsletterMailGroups/NewsletterMailGroups.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListNewsletterMailGroupsForDropList() throws Exception {
        long id = 1l;
        String name = "name";
        List<NewsletterMailGroup> newsletterMailGroups = new ArrayList<>();
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setId(id);
        newsletterMailGroup.setName(name);
        newsletterMailGroups.add(newsletterMailGroup);
        when(newsletterMailGroupDAO.listNewsletterMailGroupsForDropList(anyString(), anyInt())).thenReturn(newsletterMailGroups);

        ResultActions resultActions = mockMvc.perform(get("/newsletterMailGroupsDropList")
                        .param("term", "term")
                        .param("limit", "1")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].id").value((int) id))
                .andExpect(jsonPath("$.data[0].newsletterMailGroupName").value(name));
        verify(newsletterMailGroupDAO, times(1)).listNewsletterMailGroupsForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(newsletterMailGroupDAO);
    }

    @Test
    public void testListNewsletterMailGroups() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfNewsletterMailGroups = 1;
        List<NewsletterMailGroup> newsletterMailGroups = new ArrayList<>();
        NewsletterMailGroup newsType = new NewsletterMailGroup();
        String name = "name";
        long id = 1l;
        newsType.setId(id);
        newsType.setName(name);
        newsType.setLanguageId(2l);
        newsletterMailGroups.add(newsType);
        when(newsletterMailGroupDAO.searchByAjax(anyInt(), anyInt(), anyString(), anyLong())).thenReturn(newsletterMailGroups);
        when(newsletterMailGroupDAO.searchByAjaxCount(anyString(), anyLong())).thenReturn(numberOfNewsletterMailGroups);
        Language language = new Language();
        String languageCode = "pl";
        language.setCode(languageCode);
        when(cmsLanguagesDAO.getById(anyLong())).thenReturn(language);

        ResultActions resultActions = mockMvc.perform(get("/newsletterMailGroupsList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfNewsletterMailGroups))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfNewsletterMailGroups))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].language2LetterCode").value(languageCode))
                .andExpect(jsonPath("$.aaData[0].newsletterMailGroupName").value(name))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(newsletterMailGroupDAO, times(1)).searchByAjax(anyInt(), anyInt(), anyString(), anyLong());
        verify(newsletterMailGroupDAO, times(1)).searchByAjaxCount(anyString(), anyLong());
        verifyNoMoreInteractions(newsletterMailGroupDAO);
    }

    @Test
    public void testAddNewsletterMailGroup() throws Exception {
        when(newsletterMailGroupDAO.add(any(NewsletterMailGroup.class))).thenReturn(1l);

        ResultActions resultActions = mockMvc.perform(post("/addNewsletterMailGroup")
                .param("name", "name")
                .param("languageId", "1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailGroupDAO, times(1)).add(any(NewsletterMailGroup.class));
        verifyNoMoreInteractions(newsletterMailGroupDAO);
    }

    @Test
    public void testDeleteNewsletterMailGroup() throws Exception {
        int id = 1;
        doNothing().when(newsletterMailGroupDAO).delete(any(NewsletterMailGroup.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteNewsletterMailGroup")
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailGroupDAO, times(1)).delete(any(NewsletterMailGroup.class));
        verifyNoMoreInteractions(newsletterMailGroupDAO);
    }

    @Test
    public void testBrowseNewsletterMailGroup() throws Exception {
        NewsletterMailGroup newsletterMailGroup = new NewsletterMailGroup();
        newsletterMailGroup.setLanguageId(1l);
        when(newsletterMailGroupDAO.get(anyLong())).thenReturn(newsletterMailGroup);
        Language language = new Language();
        String languageCode = "pl";
        language.setCode(languageCode);
        when(cmsLanguagesDAO.getById(anyLong())).thenReturn(language);

        mockMvc.perform(get("/newsletterMailGroups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/newsletterMailGroups/SingleNewsletterMailGroup"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/newsletterMailGroups/SingleNewsletterMailGroup.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("newsletterMailGroup", anything()))
                .andExpect(model().attribute("newsletterMailGroupLanguageCode", languageCode));
    }

    @Test
    public void testNewsletterMailAddressesList() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfNewsletterMailAddress = 1;
        List<NewsletterMailAddress> newsletterMailAddresses = new ArrayList<>();
        NewsletterMailAddress newsletterMailAddress = new NewsletterMailAddress();
        String email = "email";
        long id = 1l;
        newsletterMailAddress.setId(id);
        newsletterMailAddress.setEmail(email);
        newsletterMailAddress.setStatus(NewsletterMailAddressStatus.NEW);
        newsletterMailAddresses.add(newsletterMailAddress);
        when(newsletterMailAddressDAO.searchByAjax(anyInt(), anyInt(), anyString(), anyLong())).thenReturn(newsletterMailAddresses);
        when(newsletterMailAddressDAO.searchByAjaxCount(anyString(), anyLong())).thenReturn(numberOfNewsletterMailAddress);

        ResultActions resultActions = mockMvc.perform(get("/newsletterMailGroups/newsletterMailAddressesList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfNewsletterMailAddress))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfNewsletterMailAddress))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].#").value(iDisplayStart + 1))
                .andExpect(jsonPath("$.aaData[0].email").value(email))
                .andExpect(jsonPath("$.aaData[0].status").value(NewsletterMailAddressStatus.NEW.name()))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(newsletterMailAddressDAO, times(1)).searchByAjax(anyInt(), anyInt(), anyString(), anyLong());
        verify(newsletterMailAddressDAO, times(1)).searchByAjaxCount(anyString(), anyLong());
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testUpdateNewsletterMailGroup() throws Exception {
        doNothing().when(newsletterMailGroupDAO).update(any(NewsletterMailGroup.class));

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/updateNewsletterMailGroup")
                        .param("id", "1")
                        .param("name", "name")
                        .param("languageId", "2")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailGroupDAO, times(1)).update(any(NewsletterMailGroup.class));
        verifyNoMoreInteractions(newsletterMailGroupDAO);
    }

    @Test
    public void testAddNewsletterMailAddressesEmailValid() throws Exception {
        when(newsletterMailAddressDAO.getByEmailAndMailGroup(anyString(), anyLong())).thenThrow(new EmptyResultDataAccessException(1));
        when(newsletterMailAddressDAO.getByConfirmString(anyString())).thenReturn(null);
        when(newsletterMailAddressDAO.getByUnSubscribeString(anyString())).thenReturn(null);
        when(newsletterMailAddressDAO.add(any(NewsletterMailAddress.class))).thenReturn(1l);

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/addNewsletterMailAddresses")
                        .param("mailGroupId", "1")
                        .param("email", "Radoslaw.Osinski@cwsfe.pl")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailAddressDAO, times(1)).getByEmailAndMailGroup(anyString(), anyLong());
        verify(newsletterMailAddressDAO, times(1)).getByConfirmString(anyString());
        verify(newsletterMailAddressDAO, times(1)).getByUnSubscribeString(anyString());
        verify(newsletterMailAddressDAO, times(1)).add(any(NewsletterMailAddress.class));
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testAddExistingNewsletterMailAddressesEmailValid() throws Exception {
        when(newsletterMailAddressDAO.getByEmailAndMailGroup(anyString(), anyLong())).thenReturn(new NewsletterMailAddress());
        when(newsletterMailAddressDAO.getByConfirmString(anyString())).thenReturn(null);
        when(newsletterMailAddressDAO.getByUnSubscribeString(anyString())).thenReturn(null);
        when(newsletterMailAddressDAO.add(any(NewsletterMailAddress.class))).thenReturn(1l);

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/addNewsletterMailAddresses")
                        .param("mailGroupId", "1")
                        .param("email", "existing@cwsfe.pl")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_FAIL))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_ERROR_MESSAGES + "[0]").exists());
        verify(newsletterMailAddressDAO, times(1)).getByEmailAndMailGroup(anyString(), anyLong());
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testAddNewsletterMailAddressesEmailInvalid() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/addNewsletterMailAddresses")
                        .param("mailGroupId", "1")
                        .param("email", "email")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_FAIL));
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testDeleteNewsletterMailAddress() throws Exception {
        doNothing().when(newsletterMailAddressDAO).delete(any(NewsletterMailAddress.class));

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/deleteNewsletterMailAddress")
                .param("id", "1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailAddressDAO, times(1)).delete(any(NewsletterMailAddress.class));
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testActivateNewsletterMailAddress() throws Exception {
        doNothing().when(newsletterMailAddressDAO).activate(any(NewsletterMailAddress.class));

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/activateNewsletterMailAddress")
                .param("id", "1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailAddressDAO, times(1)).activate(any(NewsletterMailAddress.class));
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }

    @Test
    public void testDeactivateNewsletterMailAddress() throws Exception {
        doNothing().when(newsletterMailAddressDAO).deactivate(any(NewsletterMailAddress.class));

        ResultActions resultActions = mockMvc.perform(post("/newsletterMailGroups/deactivateNewsletterMailAddress")
                .param("id", "1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterMailGroupsController.JSON_STATUS).value(NewsletterMailGroupsController.JSON_STATUS_SUCCESS));
        verify(newsletterMailAddressDAO, times(1)).deactivate(any(NewsletterMailAddress.class));
        verifyNoMoreInteractions(newsletterMailAddressDAO);
    }
}