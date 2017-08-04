package eu.com.cwsfe.cms.rest.author;

import eu.com.cwsfe.cms.services.author.CmsAuthorDTO;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Radosław Osiński
 */
@RestController
public class AuthorRestController {

    private final CmsAuthorsService cmsAuthorsService;

    @Autowired
    public AuthorRestController(CmsAuthorsService cmsAuthorsService) {
        this.cmsAuthorsService = cmsAuthorsService;
    }

    /**
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/author/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CmsAuthorDTO getAuthor(
        @PathVariable("id") long authorId, HttpServletResponse response
    ) {
        try {
            return cmsAuthorsService.get(authorId);
        } catch (EmptyResultDataAccessException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

}
