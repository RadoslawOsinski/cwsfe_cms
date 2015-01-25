package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogKeywordsDAO;
import eu.com.cwsfe.cms.dao.BlogPostKeywordsDAO;
import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
import eu.com.cwsfe.cms.model.BlogKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@RestController
public class BlogKeywordsRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogKeywordsRestController.class);

    @Autowired
    private BlogKeywordsDAO blogKeywordsDAO;

    @Autowired
    private BlogPostKeywordsDAO blogPostKeywordsDAO;

    @Autowired
    private CmsTextI18nDAO cmsTextI18nDAO;

    /**
     * @param languageCode language code
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/blogKeywordsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<BlogKeyword> blogKeywordsList(
            @RequestParam(value = "languageCode") String languageCode
    ) {
        List<BlogKeyword> list = blogKeywordsDAO.list();
        for (BlogKeyword blogKeyword : list) {
            try {
                String keywordTranslation = cmsTextI18nDAO.findTranslation(languageCode, "BlogKeywords", blogKeyword.getKeywordName());
                blogKeyword.setKeywordName(keywordTranslation);
            } catch (EmptyResultDataAccessException e) {
                LOGGER.error("Missing translation for languageCode: {}, category: blog_keyword, key: {}", languageCode, blogKeyword.getKeywordName());
            }
        }
        return list;
    }

    /**
     * @param blogPostId blog post id
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/postKeywords", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<BlogKeyword> blogKeywordsListForPost(
            @RequestParam(value = "blogPostId") long blogPostId,
            @RequestParam(value = "languageCode") String languageCode
    ) {
        List<BlogKeyword> list = blogPostKeywordsDAO.listForPost(blogPostId);
        for (BlogKeyword blogKeyword : list) {
            try {
                String keywordTranslation = cmsTextI18nDAO.findTranslation(languageCode, "BlogKeywords", blogKeyword.getKeywordName());
                blogKeyword.setKeywordName(keywordTranslation);
            } catch (EmptyResultDataAccessException e) {
                LOGGER.error("Missing translation for languageCode: {}, category: blog_keyword, key: {}", languageCode, blogKeyword.getKeywordName());
            }
        }
        return list;
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
