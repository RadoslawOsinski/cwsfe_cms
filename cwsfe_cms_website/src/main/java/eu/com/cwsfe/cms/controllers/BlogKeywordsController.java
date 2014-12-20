package eu.com.cwsfe.cms.controllers;

import eu.com.cwsfe.cms.dao.BlogKeywordsDAO;
import eu.com.cwsfe.cms.model.BlogKeyword;
import eu.com.cwsfe.cms.model.Breadcrumb;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BlogKeywordsDAO blogKeywordsDAO;

    @RequestMapping(value = "/blogKeywords", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/blogkeywords/BlogKeywords";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/blogkeywords/Blogkeywords.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogKeywords").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogKeywordsManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/blogKeywordsList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listBlogKeywords(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho
    ) {
        final List<BlogKeyword> blogKeywords = blogKeywordsDAO.listAjax(iDisplayStart, iDisplayLength);
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
        final int numberOfBlogKeywords = blogKeywordsDAO.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfBlogKeywords);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfBlogKeywords);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addBlogKeyword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addBlogKeyword(
            @ModelAttribute(value = "blogKeyword") BlogKeyword blogKeyword,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "keywordName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("KeywordNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogKeywordsDAO.add(blogKeyword);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteBlogKeyword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deleteFolder(
            @ModelAttribute(value = "blogKeyword") BlogKeyword blogKeyword,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogKeywordsDAO.delete(blogKeyword);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
