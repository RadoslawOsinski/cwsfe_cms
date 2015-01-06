package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogKeywordsDAO;
import eu.com.cwsfe.cms.dao.CmsTextI18nDAO;
import eu.com.cwsfe.cms.model.BlogKeyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(BlogKeywordsRestController.class);

    @Autowired
    private BlogKeywordsDAO blogKeywordsDAO;

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
                LOGGER.error("Missing translation for languageCode: " + languageCode + ", category: blog_keyword, key: " + blogKeyword.getKeywordName());
            }
        }
        return list;
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
