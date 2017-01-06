package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Radosław Osiński
 */
@RestController
public class CmsTextI18nRestController {

    private final CmsTextI18nDAO cmsTextI18nDAO;

    @Autowired
    public CmsTextI18nRestController(CmsTextI18nDAO cmsTextI18nDAO) {
        this.cmsTextI18nDAO = cmsTextI18nDAO;
    }

    @RequestMapping(value = "/rest/getTranslation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public String getTranslation(
        @RequestParam(value = "languageCode") String languageCode,
        @RequestParam(value = "category") String category,
        @RequestParam(value = "key") String key
    ) {
        return cmsTextI18nDAO.findTranslation(languageCode, category, key);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }

}
