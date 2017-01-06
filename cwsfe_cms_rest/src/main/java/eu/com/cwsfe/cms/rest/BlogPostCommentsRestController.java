package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.BlogPostCommentsDAO;
import eu.com.cwsfe.cms.model.BlogPostComment;
import eu.com.cwsfe.cms.rest.validator.CmsValidationRestException;
import eu.com.cwsfe.cms.rest.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Radosław Osiński
 */
@RestController
public class BlogPostCommentsRestController {

    private final BlogPostCommentsDAO blogPostCommentsDAO;

    @Autowired
    public BlogPostCommentsRestController(BlogPostCommentsDAO blogPostCommentsDAO) {
        this.blogPostCommentsDAO = blogPostCommentsDAO;
    }

    /**
     * @return internationalized blog keywords list
     */
    @RequestMapping(value = "/rest/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Map<String, Integer> countCommentsForPostI18n(
        @RequestParam(value = "blogPostI18nContentId") long blogPostI18nContentId
    ) {
        Map<String, Integer> result = new HashMap<>(1);
        result.put("count", blogPostCommentsDAO.countCommentsForPostI18n(blogPostI18nContentId));
        return result;
    }

    @RequestMapping(value = "/rest/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Map<String, String> addComment(
        @ModelAttribute(value = "blogPostComment") BlogPostComment blogPostComment,
        ModelMap modelMap, BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "blogPostI18nContentId", "Blog post must be set");
        ValidationUtils.rejectIfEmpty(result, "userName", "Username must be set");
        ValidationUtils.rejectIfEmpty(result, "email", "Email must be set");
        if (blogPostComment.getEmail() != null && !EmailValidator.isValidEmailAddress(blogPostComment.getEmail())) {
            result.rejectValue("email", "Email is invalid");
        }
        ValidationUtils.rejectIfEmpty(result, "comment", "Comment must be set");
        if (!result.hasErrors()) {
            blogPostComment.setCreated(new Date());
            blogPostCommentsDAO.add(blogPostComment);
        } else {
            String addCommentErrorMessage = "";
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                addCommentErrorMessage += result.getAllErrors().get(i).getCode() + "<br/>";
            }
            throw new CmsValidationRestException(addCommentErrorMessage);
        }
        return new HashMap<>(1);
    }

    @ExceptionHandler(value = CmsValidationRestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleParameterValidation() {
        //handling error code 400
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
        //handling error code 404
    }
}
