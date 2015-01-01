package eu.com.cwsfe.cms.blog;

import eu.com.cwsfe.cms.mvc.JsonController;
import eu.com.cwsfe.cms.dao.BlogPostCodesDAO;
import eu.com.cwsfe.cms.model.BlogPostCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOGGER = LogManager.getLogger(BlogPostCodeController.class);

    @Autowired
    private BlogPostCodesDAO blogPostCodesDAO;

    @RequestMapping(value = "/blogPosts/blogPostCodesList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listBlogKeywords(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho,
            WebRequest webRequest
    ) {
        Long blogPostId = null;
        try {
            blogPostId = Long.parseLong(webRequest.getParameter("blogPostId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Blog post id is not a number: " + webRequest.getParameter("blogPostId"));
        }
        List<BlogPostCode> dbList = blogPostCodesDAO.searchByAjax(iDisplayStart, iDisplayLength, blogPostId);
        Integer dbListDisplayRecordsSize = blogPostCodesDAO.searchByAjaxCount(blogPostId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final BlogPostCode object = dbList.get(i);
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
        responseDetailsJson.put("iTotalRecords", blogPostCodesDAO.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/addBlogPostCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addNews(
            @ModelAttribute(value = "blogPostCode") BlogPostCode blogPostCode,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "codeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeMustBeSet"));
        final BlogPostCode existingCode = blogPostCodesDAO.getCodeForPostByCodeId(blogPostCode.getBlogPostId(), blogPostCode.getCodeId());
        if (existingCode != null) {
            result.rejectValue("code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeUnique"));
        }
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCodesDAO.add(blogPostCode);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/deleteBlogPostCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String blogPostCodeDelete(
            @ModelAttribute(value = "blogPostCode") BlogPostCode blogPostCode,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "codeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CodeIdMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostCodesDAO.delete(blogPostCode);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
