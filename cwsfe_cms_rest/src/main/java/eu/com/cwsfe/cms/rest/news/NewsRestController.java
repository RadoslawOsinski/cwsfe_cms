package eu.com.cwsfe.cms.rest.news;

import eu.com.cwsfe.cms.services.news.CmsNewsI18nContentsService;
import eu.com.cwsfe.cms.services.news.CmsNewsI18nPairDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@RestController
public class NewsRestController {

    private final CmsNewsI18nContentsService cmsNewsI18nContentsService;

    public NewsRestController(
        CmsNewsI18nContentsService cmsNewsI18nContentsService
    ) {
        this.cmsNewsI18nContentsService = cmsNewsI18nContentsService;
    }

    @RequestMapping(value = "/rest/newsI18nPairs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<CmsNewsI18nPairDTO> listByFolderLangAndNewsWithPaging(
        @RequestParam(value = "newsPerPage") int newsPerPage,
        @RequestParam(value = "offset") int offset,
        @RequestParam(value = "folderName") String folderName,
        @RequestParam(value = "languageCode") String languageCode,
        @RequestParam(value = "newsType") String newsType
    ) {
        return cmsNewsI18nContentsService.list(folderName, languageCode, newsType, newsPerPage, offset);
    }

}
