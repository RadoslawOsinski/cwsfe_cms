package eu.com.cwsfe.cms.controllers;

import eu.com.cwsfe.cms.dao.NewsTypesDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.NewsType;
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
class NewsTypesController extends JsonController {

    @Autowired
    private NewsTypesDAO newsTypesDAO;

    @RequestMapping(value = "/newsTypes", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/newsTypes/NewsTypes";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsTypes/NewsTypes.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsTypes").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypesManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/newsTypesList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listNewsTypes(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho
    ) {
        final List<NewsType> cmsNewsTypes = newsTypesDAO.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsNewsTypes.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("type", cmsNewsTypes.get(i).getType());
            formDetailsJson.put("id", cmsNewsTypes.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfNewsTypes = newsTypesDAO.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfNewsTypes);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfNewsTypes);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/newsTypesDropList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listNewsTypesForDropList(
            @RequestParam String term,
            @RequestParam Integer limit
    ) {
        final List<NewsType> results = newsTypesDAO.listNewsTypesForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (NewsType newsType : results) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", newsType.getId());
            formDetailsJson.put("type", newsType.getType());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNewsType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addNewsType(
            @ModelAttribute(value = "newsType") NewsType newsType,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "type", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsTypesDAO.add(newsType);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNewsType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deleteNewsType(
            @ModelAttribute(value = "cmsNewsType") NewsType cmsNewsType,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsTypesDAO.delete(cmsNewsType);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
