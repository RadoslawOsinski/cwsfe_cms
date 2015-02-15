package eu.com.cwsfe.cms.web.author;

import eu.com.cwsfe.cms.dao.CmsAuthorsDAO;
import eu.com.cwsfe.cms.model.CmsAuthor;
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

import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-controller-test.xml"})
@WebAppConfiguration
public class AuthorsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsAuthorsDAO cmsAuthorsDAO;

    @InjectMocks
    private AuthorsController authorsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorsController).build();
    }

    @Test
    public void testDefaultView() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("cms/authors/Authors"))
                .andExpect(model().attribute("mainJavaScript", "/resources-cwsfe-cms/js/cms/authors/Authors.js"))
                .andExpect(model().attribute("breadcrumbs", anything()));
    }

    @Test
    public void testListAuthors() throws Exception {
        int iDisplayStart = 0;
        int iDisplayLength = 2;
        String sEcho = "1";
        int numberOfAuthors = 1;
        ArrayList<CmsAuthor> authors = new ArrayList<>();
        CmsAuthor cmsAuthor = new CmsAuthor();
        long authorId = 2l;
        String lastName = "Osinski";
        String firstName = "Radoslaw";
        String googlePlusAuthorLink = "https://plus.google.com/u/0/+RadosławOsiński/";
        cmsAuthor.setId(authorId);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        authors.add(cmsAuthor);
        when(cmsAuthorsDAO.listAjax(iDisplayStart, iDisplayLength)).thenReturn(authors);
        when(cmsAuthorsDAO.countForAjax()).thenReturn(numberOfAuthors);

        ResultActions resultActions = mockMvc.perform(get("/authorsList")
                .param("iDisplayStart", String.valueOf(iDisplayStart))
                .param("iDisplayLength", String.valueOf(iDisplayLength))
                .param("sEcho", sEcho))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.sEcho").value(sEcho))
                .andExpect(jsonPath("$.iTotalRecords").value(numberOfAuthors))
                .andExpect(jsonPath("$.iTotalDisplayRecords").value(numberOfAuthors))
                .andExpect(jsonPath("$.aaData").exists())
                .andExpect(jsonPath("$.aaData[0].id").value((int) authorId))
                .andExpect(jsonPath("$.aaData[0].lastName").value(lastName))
                .andExpect(jsonPath("$.aaData[0].firstName").value(firstName))
                .andExpect(jsonPath("$.aaData[0].googlePlusAuthorLink").value(googlePlusAuthorLink));
        verify(cmsAuthorsDAO, times(1)).listAjax(anyInt(), anyInt());
        verify(cmsAuthorsDAO, times(1)).countForAjax();
        verifyNoMoreInteractions(cmsAuthorsDAO);
    }

    @Test
    public void testListAuthorsForDropList() throws Exception {
        int limit = 1;
        ArrayList<CmsAuthor> authors = new ArrayList<>();
        CmsAuthor cmsAuthor = new CmsAuthor();
        long authorId = 2l;
        String lastName = "Osinski";
        String firstName = "Radoslaw";
        String googlePlusAuthorLink = "https://plus.google.com/u/0/+RadosławOsiński/";
        cmsAuthor.setId(authorId);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        authors.add(cmsAuthor);
        when(cmsAuthorsDAO.listAuthorsForDropList(anyString(), anyInt())).thenReturn(authors);

        ResultActions resultActions = mockMvc.perform(get("/authorsDropList")
                .param("term", lastName)
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].id").value((int) authorId))
                .andExpect(jsonPath("$.data[0].lastName").value(lastName))
                .andExpect(jsonPath("$.data[0].firstName").value(firstName))
                .andExpect(jsonPath("$.data[0].googlePlusAuthorLink").value(googlePlusAuthorLink));
        verify(cmsAuthorsDAO, times(1)).listAuthorsForDropList(anyString(), anyInt());
        verifyNoMoreInteractions(cmsAuthorsDAO);
    }

    @Test
    public void testAddAuthor() throws Exception {
        CmsAuthor cmsAuthor = new CmsAuthor();
        long authorId = 2l;
        String lastName = "Osinski";
        String firstName = "Radoslaw";
        String googlePlusAuthorLink = "https://plus.google.com/u/0/+RadosławOsiński/";
        cmsAuthor.setId(authorId);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        when(cmsAuthorsDAO.add(any(CmsAuthor.class))).thenReturn(1l);

        ResultActions resultActions = mockMvc.perform(post("/addAuthor")
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsAuthorsDAO, times(1)).add(any(CmsAuthor.class));
        verifyNoMoreInteractions(cmsAuthorsDAO);
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        int id = 1;
        CmsAuthor cmsAuthor = new CmsAuthor();
        long authorId = 2l;
        String lastName = "Osinski";
        String firstName = "Radoslaw";
        String googlePlusAuthorLink = "https://plus.google.com/u/0/+RadosławOsiński/";
        cmsAuthor.setId(authorId);
        cmsAuthor.setLastName(lastName);
        cmsAuthor.setFirstName(firstName);
        cmsAuthor.setGooglePlusAuthorLink(googlePlusAuthorLink);
        doNothing().when(cmsAuthorsDAO).delete(any(CmsAuthor.class));

        ResultActions resultActions = mockMvc.perform(post("/deleteAuthor")
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$." + AuthorsController.JSON_STATUS).value(AuthorsController.JSON_STATUS_SUCCESS));
        verify(cmsAuthorsDAO, times(1)).delete(any(CmsAuthor.class));
        verifyNoMoreInteractions(cmsAuthorsDAO);
    }
}
