//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.*;
//import eu.com.cwsfe.cms.db.i18n.CmsLanguagesRepository;
//import eu.com.cwsfe.cms.db.news.CmsFoldersRepository;
//import eu.com.cwsfe.cms.db.news.CmsNewsI18nContentsRepository;
//import eu.com.cwsfe.cms.db.news.CmsNewsRepository;
//import eu.com.cwsfe.cms.db.news.NewsTypesRepository;
//import eu.com.cwsfe.cms.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
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
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class NewsRestController {
//
//    private final CmsNewsRepository cmsNewsRepository;
//    private final CmsFoldersRepository cmsFoldersRepository;
//    private final NewsTypesRepository newsTypesRepository;
//    private final CmsLanguagesRepository cmsLanguagesRepository;
//    private final CmsNewsI18nContentsRepository cmsNewsI18nContentsRepository;
//
//    @Autowired
//    public NewsRestController(CmsLanguagesRepository cmsLanguagesRepository, CmsNewsI18nContentsRepository cmsNewsI18nContentsRepository, CmsFoldersRepository cmsFoldersRepository, NewsTypesRepository newsTypesRepository, CmsNewsRepository cmsNewsRepository) {
//        this.cmsLanguagesRepository = cmsLanguagesRepository;
//        this.cmsNewsI18nContentsRepository = cmsNewsI18nContentsRepository;
//        this.cmsFoldersRepository = cmsFoldersRepository;
//        this.newsTypesRepository = newsTypesRepository;
//        this.cmsNewsRepository = cmsNewsRepository;
//    }
//
//    @RequestMapping(value = "/rest/news", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsDTO getNewsByNewsTypeFolderAndNewsCode(
//        @RequestParam(value = "newsType") String newsTypeValue,
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "newsCode") String newsCode
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersRepository.getByFolderName(folderName);
//        NewsTypeDTO newsType = newsTypesRepository.getByType(newsTypeValue);
//        return cmsNewsRepository.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
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
//        CmsFolderDTO cmsFolder = cmsFoldersRepository.getByFolderName(folderName);
//        LanguageDTO language = cmsLanguagesRepository.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagesRepository.getByCode("en");
//        }
//        List<Object[]> objects = cmsNewsRepository.listByFolderLangAndNewsWithPaging(cmsFolder.getId().intValue(), language.getId(), newsType, newsPerPage, offset);
//        List<CmsNewsI18nPairDTO> results = new ArrayList<>();
//        for (Object[] object : objects) {
//            CmsNewsI18nPairDTO cmsNewsI18nPair = new CmsNewsI18nPairDTO(
//                cmsNewsRepository.get((long) object[0]),
//                cmsNewsI18nContentsRepository.get((long) object[1])
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
//        CmsFolderDTO cmsFolder = cmsFoldersRepository.getByFolderName(folderName);
//        LanguageDTO language = cmsLanguagesRepository.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagesRepository.getByCode("en");
//        }
//        Integer total = cmsNewsRepository.countListByFolderLangAndNewsWithPaging(cmsFolder.getId().intValue(), language.getId(), newsType);
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
//        LanguageDTO currentPLang = cmsLanguagesRepository.getByCode(languageCode);
//        if (currentPLang == null) {
//            currentPLang = cmsLanguagesRepository.getByCode("en");
//        }
//        return cmsNewsI18nContentsRepository.getByLanguageForNews(newsId, currentPLang.getId());
//    }
//
//    @RequestMapping(value = "/rest/singleNewsI18nContent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsI18nContentDTO get(
//        @RequestParam(value = "id") Long id
//    ) {
//        return cmsNewsI18nContentsRepository.get(id);
//    }
//
//    @RequestMapping(value = "/rest/newsI18nContentByNews", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public CmsNewsI18nContentDTO getNewsI18nContentByNews(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "newsType") String newsTypeValue,
//        @RequestParam(value = "folderName") String folderName,
//        @RequestParam(value = "newsCode") String newsCode
//    ) {
//        CmsFolderDTO cmsFolder = cmsFoldersRepository.getByFolderName(folderName);
//        NewsTypeDTO newsType = newsTypesRepository.getByType(newsTypeValue);
//        CmsNewsDTO cmsNews = cmsNewsRepository.getByNewsTypeFolderAndNewsCode(newsType.getId(), cmsFolder.getId(), newsCode);
//        LanguageDTO currentPLang = cmsLanguagesRepository.getByCode(languageCode);
//        if (currentPLang == null) {
//            currentPLang = cmsLanguagesRepository.getByCode("en");
//        }
//        return cmsNewsI18nContentsRepository.getByLanguageForNews(cmsNews.getId(), currentPLang.getId());
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//}
