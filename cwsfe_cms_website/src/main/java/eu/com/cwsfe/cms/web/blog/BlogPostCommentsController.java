package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.db.blog.CmsBlogPostCommentsEntity;
import eu.com.cwsfe.cms.services.blog.BlogPostCommentsService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class BlogPostCommentsController extends JsonController {

    private final BlogPostCommentsService blogPostCommentsService;

    @Autowired
    public BlogPostCommentsController(BlogPostCommentsService blogPostCommentsService) {
        this.blogPostCommentsService = blogPostCommentsService;
    }

    @RequestMapping(value = "/publishBlogPostComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String blogPostCommentPublish(
        @ModelAttribute(value = "blogPostComment") CmsBlogPostCommentsEntity blogPostComment,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CommentMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCommentsService.publish(blogPostComment);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blockBlogPostComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String blogPostCommentBlock(
        @ModelAttribute(value = "blogPostComment") CmsBlogPostCommentsEntity blogPostComment,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CommentMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCommentsService.block(blogPostComment);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/markAsSpamBlogPostComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String blogPostCommentMarkAsSpam(
        @ModelAttribute(value = "blogPostComment") CmsBlogPostCommentsEntity blogPostComment,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CommentMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCommentsService.markAsSpam(blogPostComment);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteBlogPostComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String blogPostCommentDelete(
        @ModelAttribute(value = "blogPostComment") CmsBlogPostCommentsEntity blogPostComment,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CommentMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCommentsService.delete(blogPostComment);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
