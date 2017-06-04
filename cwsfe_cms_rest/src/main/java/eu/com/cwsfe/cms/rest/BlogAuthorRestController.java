package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.services.author.CmsAuthorDTO;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
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

    private final CmsAuthorsService cmsAuthorsService;

    @Autowired
    public BlogAuthorRestController(CmsAuthorsService cmsAuthorsService) {
        this.cmsAuthorsService = cmsAuthorsService;
    }

    /**
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/author/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CmsAuthorDTO getAuthor(
        @PathVariable("id") long authorId
    ) {
        return cmsAuthorsService.get(authorId);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
