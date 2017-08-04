package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.news.CmsTextI18NCategoriesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.news.CmsTextI18nCategoryService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CmsTextI18nCategoryController extends JsonController {

    private final CmsTextI18nCategoryService cmsTextI18nCategoryService;

    @Autowired
    public CmsTextI18nCategoryController(CmsTextI18nCategoryService cmsTextI18nCategoryService) {
        this.cmsTextI18nCategoryService = cmsTextI18nCategoryService;
    }

    @RequestMapping(value = "/cmsTextI18nCategories", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/textI18nCategories/TextI18nCategories";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/textI18nCategories/TextI18nCategories.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/cmsTextI18nCategories").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TranslationCategoriesManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/cmsTextI18nCategoriesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listCmsTextI18nCategories(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsTextI18NCategoriesEntity> cmsTextI18nCategories = cmsTextI18nCategoryService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsTextI18nCategories.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("category", cmsTextI18nCategories.get(i).getCategory());
            formDetailsJson.put("status", cmsTextI18nCategories.get(i).getStatus());
            formDetailsJson.put("id", cmsTextI18nCategories.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final Long numberOfCategories = cmsTextI18nCategoryService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfCategories);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfCategories);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/cmsTextI18nCategoryDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listCmsLanguagesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsTextI18NCategoriesEntity> cmsTextI18nCategories = cmsTextI18nCategoryService.listForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsTextI18NCategoriesEntity lang : cmsTextI18nCategories) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", lang.getId());
            formDetailsJson.put("category", lang.getCategory());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }


    @RequestMapping(value = "/addCmsTextI18nCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addTextI18nCategory(
        @ModelAttribute(value = "cmsTextI18nCategory") CmsTextI18NCategoriesEntity cmsTextI18nCategory,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "category", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CategoryMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsTextI18nCategoryService.add(cmsTextI18nCategory);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteCmsTextI18nCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteTextI18nCategory(
        @ModelAttribute(value = "cmsTextI18nCategory") CmsTextI18NCategoriesEntity cmsTextI18nCategory,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nCategoryMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsTextI18nCategoryService.delete(cmsTextI18nCategory);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
