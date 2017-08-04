package eu.com.cwsfe.cms.rest.news;

import eu.com.cwsfe.cms.services.news.CmsTextI18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@RestController
public class CmsTextI18nRestController {

    private final CmsTextI18nService cmsTextI18nService;

    @Autowired
    public CmsTextI18nRestController(CmsTextI18nService cmsTextI18nService) {
        this.cmsTextI18nService = cmsTextI18nService;
    }

    @RequestMapping(value = "/rest/getTranslation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public String getTranslation(
        @RequestParam(value = "languageCode") String languageCode,
        @RequestParam(value = "category") String category,
        @RequestParam(value = "key") String key,
        HttpServletResponse response
    ) {
        Optional<String> translation = cmsTextI18nService.findTranslation(languageCode, category, key);
        if (translation.isPresent()) {
            return translation.get();
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }

}
