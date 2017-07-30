package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.db.blog.BlogKeywordsEntity;
import eu.com.cwsfe.cms.services.blog.BlogKeywordsService;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class BlogKeywordsController extends JsonController {

    private final BlogKeywordsService blogKeywordsService;

    @Autowired
    public BlogKeywordsController(BlogKeywordsService blogKeywordsService) {
        this.blogKeywordsService = blogKeywordsService;
    }

    @RequestMapping(value = "/blogKeywords", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/blogkeywords/BlogKeywords";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/blogkeywords/Blogkeywords.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogKeywords").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogKeywordsManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/blogKeywordsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listBlogKeywords(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<BlogKeywordsEntity> blogKeywords = blogKeywordsService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < blogKeywords.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("keywordName", blogKeywords.get(i).getKeywordName());
            formDetailsJson.put("id", blogKeywords.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final long numberOfBlogKeywords = blogKeywordsService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfBlogKeywords);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfBlogKeywords);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addBlogKeyword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addBlogKeyword(
        @ModelAttribute(value = "blogKeyword") BlogKeywordsEntity blogKeyword,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "keywordName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("KeywordNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                blogKeywordsService.add(blogKeyword);
                addJsonSuccess(responseDetailsJson);
            } catch (DuplicateKeyException e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogKeywordsAlreadyExists"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteBlogKeyword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteFolder(
        @ModelAttribute(value = "blogKeyword") BlogKeywordsEntity blogKeyword,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogKeywordsService.delete(blogKeyword);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
