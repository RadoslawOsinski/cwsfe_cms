package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogPostCommentsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by Radosław Osiński
 */
@RestController
public class BlogPostCommentsRestController {

    @Autowired
    private BlogPostCommentsDAO blogPostCommentsDAO;

    /**
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public HashMap<String, Integer> countCommentsForPostI18n(
            @RequestParam(value = "blogPostI18nContentId") long blogPostI18nContentId
    ) {
        HashMap<String, Integer> result = new HashMap<>(1);
        result.put("count", blogPostCommentsDAO.countCommentsForPostI18n(blogPostI18nContentId));
        return result;
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
