package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.news.CmsTextI18NEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
import eu.com.cwsfe.cms.services.news.CmsTextI18nCategoryService;
import eu.com.cwsfe.cms.services.news.CmsTextI18nService;
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
public class CmsTextI18nController extends JsonController {

    private final CmsTextI18nService cmsTextI18nService;
    private final CmsTextI18nCategoryService cmsTextI18nCategoryService;
    private final CmsLanguagesService cmsLanguagesService;

    @Autowired
    public CmsTextI18nController(CmsTextI18nCategoryService cmsTextI18nCategoryService, CmsLanguagesService cmsLanguagesService, CmsTextI18nService cmsTextI18nService) {
        this.cmsTextI18nCategoryService = cmsTextI18nCategoryService;
        this.cmsLanguagesService = cmsLanguagesService;
        this.cmsTextI18nService = cmsTextI18nService;
    }

    @RequestMapping(value = "/cmsTextI18n", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/textI18n/TextI18n";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/textI18n/TextI18n.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/cmsTextI18n").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TranslationsManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/cmsTextI18nList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listCmsTextI18n(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsTextI18NEntity> cmsTextI18ns = cmsTextI18nService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsTextI18ns.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("language", cmsLanguagesService.getById(cmsTextI18ns.get(i).getLangId()).getCode());
//            formDetailsJson.put("category", cmsTextI18nCategoryService.get(cmsTextI18ns.get(i).getI18nCategory()).getCategory());
//            formDetailsJson.put("key", cmsTextI18ns.get(i).getI18nKey());
//            formDetailsJson.put("text", cmsTextI18ns.get(i).getI18nText());
            formDetailsJson.put("id", cmsTextI18ns.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfAuthors = cmsTextI18nService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfAuthors);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfAuthors);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addCmsTextI18n", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addTextI18n(
        @ModelAttribute(value = "cmsTextI18n") CmsTextI18NEntity cmsTextI18n,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "langId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18nCategory", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CategoryMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18nKey", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("KeyMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18nText", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsTextI18nService.add(cmsTextI18n);
                addJsonSuccess(responseDetailsJson);
            } catch (DuplicateKeyException e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nAlreadyExists"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteCmsTextI18n", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteTextI18n(
        @ModelAttribute(value = "cmsTextI18n") CmsTextI18NEntity cmsTextI18n,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsTextI18nService.delete(cmsTextI18n);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
