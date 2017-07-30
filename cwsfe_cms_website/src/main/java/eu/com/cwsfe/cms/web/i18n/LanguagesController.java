package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.i18n.CmsLanguagesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
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
class LanguagesController extends JsonController {

    private final CmsLanguagesService cmsLanguagesService;

    @Autowired
    public LanguagesController(CmsLanguagesService cmsLanguagesService) {
        this.cmsLanguagesService = cmsLanguagesService;
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/languages/Languages";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/languages/Languages.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/languages").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguagesManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/languagesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listLanguages(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsLanguagesEntity> cmsLanguages = cmsLanguagesService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsLanguages.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("code", cmsLanguages.get(i).getCode());
            formDetailsJson.put("name", cmsLanguages.get(i).getName());
            formDetailsJson.put("id", cmsLanguages.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfLanguages = cmsLanguagesService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfLanguages);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfLanguages);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/cmsLanguagesDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listCmsLanguagesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsLanguagesEntity> languages = cmsLanguagesService.listForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsLanguagesEntity lang : languages) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", lang.getId());
            formDetailsJson.put("code", lang.getCode());
            formDetailsJson.put("name", lang.getName());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addLanguage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addLanguage(
        @ModelAttribute(value = "cmsLanguage") CmsLanguagesEntity cmsLanguage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageNameMustBeSet"));
        if (Locale.forLanguageTag(cmsLanguage.getCode()).getLanguage().isEmpty()) {
            result.rejectValue("code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("Language2LetterCodeIsInvalid"));
        }
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsLanguagesService.add(cmsLanguage);
                addJsonSuccess(responseDetailsJson);
            } catch (DuplicateKeyException e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageAlreadyExists"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteLanguage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteLanguage(
        @ModelAttribute(value = "cmsLanguage") CmsLanguagesEntity cmsLanguage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsLanguagesService.delete(cmsLanguage);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
