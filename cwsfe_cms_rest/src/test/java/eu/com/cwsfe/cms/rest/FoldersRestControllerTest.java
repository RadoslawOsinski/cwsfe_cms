package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.configuration.RestTestConfiguration;
import eu.com.cwsfe.cms.dao.CmsFoldersDAO;
import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
import eu.com.cwsfe.cms.model.CmsFolder;
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestTestConfiguration.class})
@WebAppConfiguration
public class FoldersRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CmsFoldersDAO cmsFoldersDAO;
    @Mock
    private CmsTextI18nDAO cmsTextI18nDAO;

    @InjectMocks
    private FoldersRestController foldersRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(foldersRestController).build();
    }

    @Test
    public void testFoldersList() throws Exception {
        long id = 1L;
        String folderName = "name";
        String folderNameI18n = "folderNameI18n";
        long orderNumber = 2L;
        ArrayList<CmsFolder> cmsFolders = new ArrayList<>();
        CmsFolder folder = new CmsFolder();
        folder.setId(id);
        folder.setFolderName(folderName);
        folder.setOrderNumber(orderNumber);
        cmsFolders.add(folder);
        when(cmsFoldersDAO.list()).thenReturn(cmsFolders);
        when(cmsTextI18nDAO.findTranslation(anyString(), anyString(), anyString())).thenReturn(folderNameI18n);

        ResultActions resultActions = mockMvc.perform(get("/rest/foldersList")
            .param("languageCode", "en")
            .accept(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
            .andExpect(jsonPath("$[0].id").value((int) id))
            .andExpect(jsonPath("$[0].folderName").value(folderNameI18n))
            .andExpect(jsonPath("$[0].orderNumber").value((int) orderNumber));
    }

}
