package eu.com.cwsfe.cms.rest.author;

import eu.com.cwsfe.cms.services.author.CmsAuthorDTO;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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
    @ApiOperation(
        value = "Returns author"
    )
    @RequestMapping(value = "/rest/author/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public CmsAuthorDTO getAuthor(
        @ApiParam(value = "Author id in cms") @PathVariable("id") long authorId, HttpServletResponse response
    ) {
        Optional<CmsAuthorDTO> cmsAuthorDTO = cmsAuthorsService.get(authorId);
        if (cmsAuthorDTO.isPresent()) {
            return cmsAuthorDTO.get();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

}
