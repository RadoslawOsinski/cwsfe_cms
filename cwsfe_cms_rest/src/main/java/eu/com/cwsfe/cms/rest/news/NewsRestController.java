//package eu.com.cwsfe.cms.rest.news;
//
//import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
//import eu.com.cwsfe.cms.services.news.*;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Radosław Osiński
// */
//@RestController
//public class NewsRestController {
//
//    private final CmsNewsService cmsNewsService;
//    private final CmsFoldersService cmsFoldersService;
//    private final NewsTypesService newsTypesService;
//    private final CmsLanguagesService cmsLanguagesService;
//    private final CmsNewsI18nContentsService cmsNewsI18nContentsService;
//
//    public NewsRestController(
//        CmsNewsService cmsNewsService,
//        CmsFoldersService cmsFoldersService,
//        NewsTypesService newsTypesService,
//        CmsLanguagesService cmsLanguagesService,
//        CmsNewsI18nContentsService cmsNewsI18nContentsService
//    ) {
//        this.cmsNewsService = cmsNewsService;
//        this.cmsFoldersService = cmsFoldersService;
//        this.newsTypesService = newsTypesService;
//        this.cmsLanguagesService = cmsLanguagesService;
//        this.cmsNewsI18nContentsService = cmsNewsI18nContentsService;
//    }
//
//    @RequestMapping(value = "/rest/news", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsDTO getNewsByNewsTypeFolderAndNewsCode(
//        @RequestParam(value = "newsType") String newsTypeValue,
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "newsCode") String newsCode
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersService.getByFolderName(folderName);
//        NewsTypeDTO newsType = newsTypesService.getByType(newsTypeValue);
//        return cmsNewsService.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
//    }
//
//    @RequestMapping(value = "/rest/newsI18nPairs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public List<CmsNewsI18nPairDTO> listByFolderLangAndNewsWithPaging(
//        @RequestParam(value = "newsPerPage") int newsPerPage,
//        @RequestParam(value = "offset") int offset,
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "newsType") String newsType
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersService.getByFolderName(folderName);
//        LanguageDTO language = cmsLanguagesService.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagesService.getByCode("en");
//        }
//        List<Object[]> objects = cmsNewsService.listByFolderLangAndNewsWithPaging(cmsFolder.getId().intValue(), language.getId(), newsType, newsPerPage, offset);
//        List<CmsNewsI18nPairDTO> results = new ArrayList<>();
//        for (Object[] object : objects) {
//            CmsNewsI18nPairDTO cmsNewsI18nPair = new CmsNewsI18nPairDTO(
//                cmsNewsService.get((long) object[0]),
//                cmsNewsI18nContentsService.get((long) object[1])
//            );
//            results.add(cmsNewsI18nPair);
//        }
//        return results;
//    }
//
//    @RequestMapping(value = "/rest/newsI18nPairsTotal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public Map<String, Integer> countListByFolderLangAndNewsWithPaging(
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "newsType") String newsType
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersService.getByFolderName(folderName);
//        LanguageDTO language = cmsLanguagesService.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagesService.getByCode("en");
//        }
//        Integer total = cmsNewsService.countListByFolderLangAndNewsWithPaging(cmsFolder.getId().intValue(), language.getId(), newsType);
//        Map<String, Integer> result = new HashMap<>(1);
//        result.put("total", total);
//        return result;
//    }
//
//    @RequestMapping(value = "/rest/newsI18nContent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsI18nContentDTO getByLanguageForNews(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "newsId") Long newsId
//    ) {
//        LanguageDTO currentPLang = cmsLanguagesService.getByCode(languageCode);
//        if (currentPLang == null) {
//            currentPLang = cmsLanguagesService.getByCode("en");
//        }
//        return cmsNewsI18nContentsService.getByLanguageForNews(newsId, currentPLang.getId());
//    }
//
//    @RequestMapping(value = "/rest/singleNewsI18nContent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsI18nContentDTO get(
//        @RequestParam(value = "id") Long id
//    ) {
//        return cmsNewsI18nContentsService.get(id);
//    }
//
//    @RequestMapping(value = "/rest/newsI18nContentByNews", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsI18nContentDTO getNewsI18nContentByNews(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "newsType") String newsTypeValue,
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "newsCode") String newsCode
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersService.getByFolderName(folderName);
//        NewsTypeDTO newsType = newsTypesService.getByType(newsTypeValue);
//        CmsNewsDTO cmsNews = cmsNewsService.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
//        LanguageDTO currentPLang = cmsLanguagesService.getByCode(languageCode);
//        if (currentPLang == null) {
//            currentPLang = cmsLanguagesService.getByCode("en");
//        }
//        return cmsNewsI18nContentsService.getByLanguageForNews(cmsNews.getId(), currentPLang.getId());
//    }
//
//}
