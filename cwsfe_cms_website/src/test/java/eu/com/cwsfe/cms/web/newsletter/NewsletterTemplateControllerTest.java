package eu.com.cwsfe.cms.web.newsletter;

import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.dao.NewsletterTemplateDAO;
import eu.com.cwsfe.cms.domains.NewsletterTemplateStatus;
import eu.com.cwsfe.cms.model.Language;
import eu.com.cwsfe.cms.model.NewsletterTemplate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class NewsletterTemplateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsletterTemplateDAO newsletterTemplateDAO;
    @Mock
    private CmsLanguagesDAO cmsLanguagesDAO;
    @Mock
    private JavaMailSender cmsMailSender;

    @InjectMocks
    private NewsletterTemplateController newsletterTemplateController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(newsletterTemplateController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/newsletterTemplates"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/newsletterTemplates/NewsletterTemplates"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/newsletterTemplates/NewsletterTemplates.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListNewsletterTemplates() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfNewsletterMailAddress = 1;
        List<NewsletterTemplate> newsletterTemplates = new ArrayList<>();
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        String newsletterTemplateSubject = "newsletterTemplateSubject";
        long id = 1L;
        newsletterTemplate.setId(id);
        String name = "name";
        newsletterTemplate.setName(name);
        newsletterTemplate.setSubject(newsletterTemplateSubject);
        newsletterTemplate.setStatus(NewsletterTemplateStatus.NEW);
        newsletterTemplates.add(newsletterTemplate);
        when(newsletterTemplateDAO.searchByAjax(anyInt(), anyInt(), anyString(), anyLong())).thenReturn(newsletterTemplates);
        when(newsletterTemplateDAO.searchByAjaxCount(anyString(), anyLong())).thenReturn(numberOfNewsletterMailAddress);
        Language language = new Language();
        String languageCode = "en";
        language.setCode(languageCode);
        when(cmsLanguagesDAO.getById(anyLong())).thenReturn(language);

        ResultActions resultActions = mockMvc.perform(get("/newsletterTemplatesList")
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
                .andExpect(jsonPath("$.aaData[0].language2LetterCode").value(languageCode))
                .andExpect(jsonPath("$.aaData[0].newsletterTemplateName").value(name))
                .andExpect(jsonPath("$.aaData[0].newsletterTemplateSubject").value(newsletterTemplateSubject))
                .andExpect(jsonPath("$.aaData[0].newsletterTemplateStatus").value(NewsletterTemplateStatus.NEW.name()))
                .andExpect(jsonPath("$.aaData[0].id").value((int) id));
        verify(newsletterTemplateDAO, times(1)).searchByAjax(anyInt(), anyInt(), anyString(), anyLong());
        verify(newsletterTemplateDAO, times(1)).searchByAjaxCount(anyString(), anyLong());
        verifyNoMoreInteractions(newsletterTemplateDAO);
    }

    @Test
    public void testAddNewsletterTemplate() throws Exception {
        when(newsletterTemplateDAO.add(any(NewsletterTemplate.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(post("/addNewsletterTemplate")
                .param("name", "name")
                .param("languageId", "1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterTemplateController.JSON_STATUS).value(NewsletterTemplateController.JSON_STATUS_SUCCESS));
        verify(newsletterTemplateDAO, times(1)).add(any(NewsletterTemplate.class));
        verifyNoMoreInteractions(newsletterTemplateDAO);
    }

    @Test
    public void testDeleteNewsletterTemplate() throws Exception {
        doNothing().when(newsletterTemplateDAO).delete(any(NewsletterTemplate.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteNewsletterTemplate")
                .param("id", "1"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterTemplateController.JSON_STATUS).value(NewsletterTemplateController.JSON_STATUS_SUCCESS));
        verify(newsletterTemplateDAO, times(1)).delete(any(NewsletterTemplate.class));
        verifyNoMoreInteractions(newsletterTemplateDAO);
    }

    @Test
    public void testUnDeleteNewsletterTemplate() throws Exception {
        int id = 1;
        doNothing().when(newsletterTemplateDAO).undelete(any(NewsletterTemplate.class));

        ResultActions resultActions = mockMvc.perform(post("/unDeleteNewsletterTemplate")
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterTemplateController.JSON_STATUS).value(NewsletterTemplateController.JSON_STATUS_SUCCESS));
        verify(newsletterTemplateDAO, times(1)).undelete(any(NewsletterTemplate.class));
        verifyNoMoreInteractions(newsletterTemplateDAO);
    }

    @Test
    public void testBrowseNewsletterTemplate() throws Exception {
        NewsletterTemplate newsletterTemplate = new NewsletterTemplate();
        newsletterTemplate.setLanguageId(1L);
        when(newsletterTemplateDAO.get(anyLong())).thenReturn(newsletterTemplate);
        Language language = new Language();
        String languageCode = "pl";
        language.setCode(languageCode);
        when(cmsLanguagesDAO.getById(anyLong())).thenReturn(language);

        mockMvc.perform(get("/newsletterTemplates/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/newsletterTemplates/SingleNewsletterTemplate"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/newsletterTemplates/SingleNewsletterTemplate.js"))
                .andExpect(model().attribute("breadcrumbs", anything()))
                .andExpect(model().attribute("newsletterTemplate", anything()))
                .andExpect(model().attribute("newsletterTemplateLanguageCode", languageCode));
    }

    @Test
    public void testUpdateNewsletterTemplate() throws Exception {
        doNothing().when(newsletterTemplateDAO).update(any(NewsletterTemplate.class));

        ResultActions resultActions = mockMvc.perform(post("/newsletterTemplates/updateNewsletterTemplate")
                        .param("id", "1")
                        .param("name", "name")
                        .param("languageId", "2")
                        .param("subject", "subject")
                        .param("content", "content")
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + NewsletterTemplateController.JSON_STATUS).value(NewsletterTemplateController.JSON_STATUS_SUCCESS));
        verify(newsletterTemplateDAO, times(1)).update(any(NewsletterTemplate.class));
        verifyNoMoreInteractions(newsletterTemplateDAO);
    }

    @Test
    public void testNewsletterTemplateTestSend() throws Exception {
        //todo proper mocking
//        when(newsletterTemplateDAO.get(anyLong())).thenReturn(new NewsletterTemplate());
//        when(cmsMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
//        doNothing().when(cmsMailSender).send(any(MimeMessage.class));
//
//        ResultActions resultActions = mockMvc.perform(post("/newsletterTemplates/newsletterTemplateTestSend")
//                        .param("id", "1")
//                        .param("email", "Radoslaw.Osinski@gmail.com")
//        );
//
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
//                .andExpect(jsonPath("$." + NewsletterTemplateController.JSON_STATUS).value(NewsletterTemplateController.JSON_STATUS_SUCCESS));
//        verify(newsletterTemplateDAO, times(1)).get(anyLong());
//        verify(cmsMailSender, times(1)).send(any(MimeMessage.class));
//        verifyNoMoreInteractions(newsletterTemplateDAO);
    }
}
