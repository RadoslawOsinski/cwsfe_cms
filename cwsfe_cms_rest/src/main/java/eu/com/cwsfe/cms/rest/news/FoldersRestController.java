package eu.com.cwsfe.cms.rest.news;

import eu.com.cwsfe.cms.services.news.CmsFolderDTO;
import eu.com.cwsfe.cms.services.news.CmsFoldersService;
import eu.com.cwsfe.cms.services.news.CmsTextI18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@RestController
public class FoldersRestController {

    private final CmsFoldersService cmsFoldersService;

    private final CmsTextI18nService cmsTextI18nService;

    @Autowired
    public FoldersRestController(CmsFoldersService cmsFoldersService, CmsTextI18nService cmsTextI18nService) {
        this.cmsFoldersService = cmsFoldersService;
        this.cmsTextI18nService = cmsTextI18nService;
    }

    /**
     * @param languageCode language code
     * @return internationalized folders list
     */
    @RequestMapping(value = "/rest/foldersList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<CmsFolderDTO> foldersList(
        @RequestParam(value = "languageCode") String languageCode,
        HttpServletResponse response
    ) {
        List<CmsFolderDTO> list = cmsFoldersService.list();
        for (CmsFolderDTO cmsFolder : list) {
            Optional<String> folders = cmsTextI18nService.findTranslation(languageCode, "Folders", cmsFolder.getFolderName());
            if (folders.isPresent()) {
                cmsFolder.setFolderName(folders.get());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        }
        return list;
    }

}
