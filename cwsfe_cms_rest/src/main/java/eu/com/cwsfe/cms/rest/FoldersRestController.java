package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@RestController
public class FoldersRestController {

    @Autowired
    private CmsFoldersDAO cmsFoldersDAO;

    @Autowired
    private CmsTextI18nDAO cmsTextI18nDAO;

    /**
     * @param languageCode language code
     * @return internationalized folders list
     */
    @RequestMapping(value = "/rest/foldersList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<CmsFolder> foldersList(
            @RequestParam(value = "languageCode") String languageCode
    ) {
        List<CmsFolder> list = cmsFoldersDAO.list();
        for (CmsFolder cmsFolder : list) {
            cmsFolder.setFolderName(cmsTextI18nDAO.findTranslation(languageCode, "Folders", cmsFolder.getFolderName()));
        }
        return list;
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
