//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.db.news.CmsTextI18nRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Created by Radosław Osiński
// */
//todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class CmsTextI18nRestController {
//
//    private final CmsTextI18nRepository cmsTextI18nRepository;
//
//    @Autowired
//    public CmsTextI18nRestController(CmsTextI18nRepository cmsTextI18nRepository) {
//        this.cmsTextI18nRepository = cmsTextI18nRepository;
//    }
//
//    @RequestMapping(value = "/rest/getTranslation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public String getTranslation(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "category") String category,
//        @RequestParam(value = "key") String key
//    ) {
//        return cmsTextI18nRepository.findTranslation(languageCode, category, key);
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//
//}
