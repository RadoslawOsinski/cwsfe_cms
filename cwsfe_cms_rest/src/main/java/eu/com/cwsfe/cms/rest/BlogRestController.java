//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.BlogPostI18nContentsDAO;
//import eu.com.cwsfe.cms.dao.BlogPostsDAO;
//import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
//import eu.com.cwsfe.cms.db.blog.BlogPostI18nContentsRepository;
//import eu.com.cwsfe.cms.db.blog.BlogPostsRepository;
//import eu.com.cwsfe.cms.db.i18n.CmsLanguagesRepository;
//import eu.com.cwsfe.cms.services.blog.BlogI18nPairDTO;
//import eu.com.cwsfe.cms.services.blog.BlogPostDTO;
//import eu.com.cwsfe.cms.services.blog.BlogPostI18nContentDTO;
//import eu.com.cwsfe.cms.services.i18n.LanguageDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
///**
// * Created by Radosław Osiński
// */
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class BlogRestController {
//
//    private final BlogPostsRepository blogPostsRepository;
//
//    private final BlogPostI18nContentsRepository blogPostI18nContentsRepository;
//
//    private final CmsLanguagesRepository cmsLanguagesRepository;
//
//    @Autowired
//    public BlogRestController(BlogPostI18nContentsRepository blogPostI18nContentsRepository, BlogPostsRepository blogPostsRepository, CmsLanguagesRepository cmsLanguagescmsLanguagesRepository) {
//        this.blogPostI18nContentsRepository = blogPostI18nContentsRepository;
//        this.blogPostsRepository = blogPostsRepository;
//        this.cmsLanguagesRepository = cmsLanguagescmsLanguagesRepository;
//    }
//
//    @RequestMapping(value = "/rest/blogI18nPairs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public List<BlogI18nPairDTO> listBlogPosts(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "limit") Integer limit,
//        @RequestParam(value = "offset") Integer offset,
//        @RequestParam(value = "categoryId") Long categoryId
//    ) {
//        LanguageDTO language = cmsLanguagescmsLanguagesRepository.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagescmsLanguagesRepository.getByCode("en");
//        }
//        List<Object[]> postI18nIds;
//        if (categoryId != null) {
//            postI18nIds = blogPostsRepository.listForPageWithCategoryAndPaging(categoryId, language.getId(), limit, offset);
//        } else {
//            postI18nIds = blogPostsRepository.listForPageWithPaging(language.getId(), limit, offset);
//        }
//        List<BlogI18nPairDTO> results = new ArrayList<>(postI18nIds.size());
//        for (Object[] postI18nId : postI18nIds) {
//            BlogI18nPairDTO blogI18nPair = new BlogI18nPairDTO(
//                blogPostsRepository.get((long) postI18nId[0]),
//                blogPostI18nContentsRepository.get((long) postI18nId[1])
//            );
//            results.add(blogI18nPair);
//        }
//        return results;
//    }
//
//    @RequestMapping(value = "/rest/blogI18nPairsTotal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public Map<String, Long> listBlogPostsTotal(
//        @RequestParam(value = "languageCode") String languageCode,
//        @RequestParam(value = "categoryId") Long categoryId
//    ) {
//        LanguageDTO language = cmsLanguagescmsLanguagesRepository.getByCode(languageCode);
//        if (language == null) {
//            language = cmsLanguagescmsLanguagesRepository.getByCode("en");
//        }
//        Long total;
//        if (categoryId != null) {
//            total = blogPostsRepository.listCountForPageWithCategoryAndPaging(categoryId, language.getId());
//        } else {
//            total = blogPostsRepository.listCountForPageWithPaging(language.getId());
//        }
//        Map<String, Long> result = new HashMap<>(1);
//        result.put("total", total);
//        return result;
//    }
//
//    @RequestMapping(value = "/rest/blog/singlePost/{blogPostId}/{blogPostI18nContentId}", method = RequestMethod.GET)
//    public BlogI18nPairDTO singlePostView(ModelMap model, Locale locale,
//                                       @PathVariable("blogPostId") Long blogPostId,
//                                       @PathVariable("blogPostI18nContentId") Long blogPostI18nContentId) {
//        BlogPostDTO blogPost = blogPostsRepository.get(blogPostId);
//        BlogPostI18nContentDTO blogPostI18nContent = blogPostI18nContentsRepository.get(blogPostI18nContentId);
//        return new BlogI18nPairDTO(blogPost, blogPostI18nContent);
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//        //handling 404 error
//    }
//}
