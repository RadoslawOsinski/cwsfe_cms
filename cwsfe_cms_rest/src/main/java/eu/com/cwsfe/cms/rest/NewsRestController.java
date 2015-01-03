package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Radosław Osiński
 */
@RestController
public class NewsRestController {

    @Autowired
    private CmsNewsDAO cmsNewsDAO;
    @Autowired
    private CmsFoldersDAO cmsFoldersDAO;
    @Autowired
    private NewsTypesDAO newsTypesDAO;
    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;
    @Autowired
    private CmsNewsI18nContentsDAO cmsNewsI18nContentsDAO;

    @RequestMapping(value = "/rest/news", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CmsNews getNewsByNewsTypeFolderAndNewsCode(
            @RequestParam(value = "newsType") String newsTypeValue,
            @RequestParam(value = "folderName") String folderName,
            @RequestParam(value = "newsCode") String newsCode
    ) {
        CmsFolder cmsFolder = cmsFoldersDAO.getByFolderName(folderName);
        NewsType newsType = newsTypesDAO.getByType(newsTypeValue);
        return cmsNewsDAO.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
    }

    @RequestMapping(value = "/rest/newsI18nContent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CmsNewsI18nContent getByLanguageForNews(
            @RequestParam(value = "languageCode") String languageCode,
            @RequestParam(value = "newsId") Long newsId
    ) {
        Language currentPLang = cmsLanguagesDAO.getByCode(languageCode);
        if (currentPLang == null) {
            currentPLang = cmsLanguagesDAO.getByCode("en");
        }
        return cmsNewsI18nContentsDAO.getByLanguageForNews(newsId, currentPLang.getId());
    }

    @RequestMapping(value = "/rest/newsI18nContentByNews", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CmsNewsI18nContent getNewsI18nContentByNews(
            @RequestParam(value = "languageCode") String languageCode,
            @RequestParam(value = "newsType") String newsTypeValue,
            @RequestParam(value = "folderName") String folderName,
            @RequestParam(value = "newsCode") String newsCode
    ) {
        CmsFolder cmsFolder = cmsFoldersDAO.getByFolderName(folderName);
        NewsType newsType = newsTypesDAO.getByType(newsTypeValue);
        CmsNews cmsNews = cmsNewsDAO.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
        Language currentPLang = cmsLanguagesDAO.getByCode(languageCode);
        if (currentPLang == null) {
            currentPLang = cmsLanguagesDAO.getByCode("en");
        }
        return cmsNewsI18nContentsDAO.getByLanguageForNews(cmsNews.getId(), currentPLang.getId());
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
