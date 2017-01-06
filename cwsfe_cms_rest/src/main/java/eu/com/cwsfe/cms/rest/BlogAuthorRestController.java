package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsAuthorsDAO;
import eu.com.cwsfe.cms.model.CmsAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Radosław Osiński
 */
@RestController
public class BlogAuthorRestController {

    private final CmsAuthorsDAO cmsAuthorsDAO;

    @Autowired
    public BlogAuthorRestController(CmsAuthorsDAO cmsAuthorsDAO) {
        this.cmsAuthorsDAO = cmsAuthorsDAO;
    }

    /**
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/author/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CmsAuthor getAuthor(
        @PathVariable("id") long authorId
    ) {
        return cmsAuthorsDAO.get(authorId);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
