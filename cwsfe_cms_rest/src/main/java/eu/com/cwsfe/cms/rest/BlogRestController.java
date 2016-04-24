package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogPostI18nContentsDAO;
import eu.com.cwsfe.cms.dao.BlogPostsDAO;
import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.model.BlogI18nPair;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostI18nContent;
import eu.com.cwsfe.cms.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Radosław Osiński
 */
@RestController
public class BlogRestController {

    @Autowired
    private BlogPostsDAO blogPostsDAO;

    @Autowired
    private BlogPostI18nContentsDAO blogPostI18nContentsDAO;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    @RequestMapping(value = "/rest/blogI18nPairs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<BlogI18nPair> listBlogPosts(
        @RequestParam(value = "languageCode") String languageCode,
        @RequestParam(value = "limit") Integer limit,
        @RequestParam(value = "offset") Integer offset,
        @RequestParam(value = "categoryId") Long categoryId
    ) {
        Language language = cmsLanguagesDAO.getByCode(languageCode);
        if (language == null) {
            language = cmsLanguagesDAO.getByCode("en");
        }
        List<Object[]> postI18nIds;
        if (categoryId != null) {
            postI18nIds = blogPostsDAO.listForPageWithCategoryAndPaging(categoryId, language.getId(), limit, offset);
        } else {
            postI18nIds = blogPostsDAO.listForPageWithPaging(language.getId(), limit, offset);
        }
        List<BlogI18nPair> results = new ArrayList<>(postI18nIds.size());
        for (Object[] postI18nId : postI18nIds) {
            BlogI18nPair blogI18nPair = new BlogI18nPair(
                    blogPostsDAO.get((long) postI18nId[0]),
                    blogPostI18nContentsDAO.get((long) postI18nId[1])
            );
            results.add(blogI18nPair);
        }
        return results;
    }

    @RequestMapping(value = "/rest/blogI18nPairsTotal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Map<String, Long> listBlogPostsTotal(
        @RequestParam(value = "languageCode") String languageCode,
        @RequestParam(value = "categoryId") Long categoryId
    ) {
        Language language = cmsLanguagesDAO.getByCode(languageCode);
        if (language == null) {
            language = cmsLanguagesDAO.getByCode("en");
        }
        Long total;
        if (categoryId != null) {
            total = blogPostsDAO.listCountForPageWithCategoryAndPaging(categoryId, language.getId());
        } else {
            total = blogPostsDAO.listCountForPageWithPaging(language.getId());
        }
        Map<String, Long> result = new HashMap<>(1);
        result.put("total", total);
        return result;
    }

    @RequestMapping(value = "/rest/blog/singlePost/{blogPostId}/{blogPostI18nContentId}", method = RequestMethod.GET)
    public BlogI18nPair singlePostView(ModelMap model, Locale locale,
                                       @PathVariable("blogPostId") Long blogPostId,
                                       @PathVariable("blogPostI18nContentId") Long blogPostI18nContentId) {
        BlogPost blogPost = blogPostsDAO.get(blogPostId);
        BlogPostI18nContent blogPostI18nContent = blogPostI18nContentsDAO.get(blogPostI18nContentId);
        return new BlogI18nPair(blogPost, blogPostI18nContent);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
        //handling 404 error
    }
}
