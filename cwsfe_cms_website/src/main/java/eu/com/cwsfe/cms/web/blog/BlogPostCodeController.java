package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostCodeEntity;
import eu.com.cwsfe.cms.services.blog.BlogPostCodesService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class BlogPostCodeController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostCodeController.class);

    private final BlogPostCodesService blogPostCodesService;

    @Autowired
    public BlogPostCodeController(BlogPostCodesService blogPostCodesService) {
        this.blogPostCodesService = blogPostCodesService;
    }

    @RequestMapping(value = "/blogPosts/blogPostCodesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listBlogPostCodes(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        WebRequest webRequest
    ) {
        Long blogPostId = null;
        try {
            blogPostId = Long.parseLong(webRequest.getParameter("blogPostId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Blog post id is not a number: {}", webRequest.getParameter("blogPostId"));
        }
        List<BlogPostCodeEntity> dbList = blogPostCodesService.searchByAjax(iDisplayStart, iDisplayLength, blogPostId);
        Integer dbListDisplayRecordsSize = blogPostCodesService.searchByAjaxCount(blogPostId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final BlogPostCodeEntity object = dbList.get(i);
            formDetailsJson.put("codeId", object.getCodeId());
            if (object.getCode() == null || object.getCode().isEmpty()) {
                formDetailsJson.put("code", "---");
            } else {
                formDetailsJson.put("code", (object.getCode().length() < 100) ? object.getCode() : object.getCode().substring(0, 97) + "...");
            }
            formDetailsJson.put("id", object.getCodeId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", blogPostCodesService.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/addBlogPostCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addBlogPostCode(
        @ModelAttribute(value = "blogPostCode") BlogPostCodeEntity blogPostCode,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "codeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeMustBeSet"));
        BlogPostCodeEntity existingCode;
        try {
            existingCode = blogPostCodesService.getCodeForPostByCodeId(blogPostCode.getBlogPostId(), blogPostCode.getCodeId());
        } catch (EmptyResultDataAccessException e) {
            existingCode = null;
        }
        if (existingCode != null && existingCode.getCodeId().equals(blogPostCode.getCodeId())) {
            result.rejectValue("code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeUnique"));
        }
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCodesService.add(blogPostCode);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/deleteBlogPostCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String blogPostCodeDelete(
        @ModelAttribute(value = "blogPostCode") BlogPostCodeEntity blogPostCode,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "codeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCodesService.delete(blogPostCode);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
