//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.BlogPostKeywordsDAO;
//import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
//import eu.com.cwsfe.cms.db.blog.BlogKeywordsEntity;
//import eu.com.cwsfe.cms.db.blog.BlogKeywordsRepository;
//import eu.com.cwsfe.cms.db.news.CmsTextI18nRepository;
//import eu.com.cwsfe.cms.services.blog.BlogKeywordsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Created by Radosław Osiński
// */
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class BlogKeywordsRestController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BlogKeywordsRestController.class);
//
//    private final BlogKeywordsService blogKeywordsService;
//
//    private final CmsTextI18nRepository cmsTextI18nRepository;
//
//    @Autowired
//    public BlogKeywordsRestController(BlogKeywordsService blogKeywordsService, CmsTextI18nRepository cmsTextI18nRepository) {
//        this.blogKeywordsService = blogKeywordsService;
//        this.cmsTextI18nRepository = cmsTextI18nRepository;
//    }
//
//    /**
//     * @param languageCode language code
//     * @return internationalized blog keywords list
//     */
//    @RequestMapping(value = "/rest/blogKeywordsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public List<BlogKeywordsEntity> blogKeywordsList(
//        @RequestParam(value = "languageCode") String languageCode
//    ) {
//        List<BlogKeywordsEntity> list = blogKeywordsService.list();
//        for (BlogKeywordsEntity blogKeyword : list) {
//            try {
//                String keywordTranslation = cmsTextI18nRepository.findTranslation(languageCode, "BlogKeywords", blogKeyword.getKeywordName());
//                blogKeyword.setKeywordName(keywordTranslation);
//            } catch (EmptyResultDataAccessException e) {
//                LOGGER.error("Missing translation for languageCode: {}, category: blog_keyword, key: {}", languageCode, blogKeyword.getKeywordName(), e);
//            }
//        }
//        return list;
//    }
//
//    /**
//     * @param blogPostId blog post id
//     * @return internationalized blog keywords list
//     */
//    //todo uncomment!
////    @RequestMapping(value = "/rest/postKeywords", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
////    public List<BlogKeywordDTO> blogKeywordsListForPost(
////        @RequestParam(value = "blogPostId") long blogPostId,
////        @RequestParam(value = "languageCode") String languageCode
////    ) {
////        List<BlogKeywordDTO> list = blogPostKeywordsDAO.listForPost(blogPostId);
////        for (BlogKeywordDTO blogKeyword : list) {
////            try {
////                String keywordTranslation = cmsTextI18nRepository.findTranslation(languageCode, "BlogKeywords", blogKeyword.getKeywordName());
////                blogKeyword.setKeywordName(keywordTranslation);
////            } catch (EmptyResultDataAccessException e) {
////                LOGGER.error("Missing translation for languageCode: {}, category: blog_keyword, key: {}", languageCode, blogKeyword.getKeywordName(), e);
////            }
////        }
////        return list;
////    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//}
