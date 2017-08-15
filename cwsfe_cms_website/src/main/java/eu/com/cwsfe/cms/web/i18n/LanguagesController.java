package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.i18n.CmsLanguagesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
import eu.com.cwsfe.cms.web.mvc.BasicResponse;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public LanguagesDTO listLanguages(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsLanguagesEntity> cmsLanguages = cmsLanguagesService.listAjax(iDisplayStart, iDisplayLength);
        LanguagesDTO languagesDTO = new LanguagesDTO();
        for (int i = 0; i < cmsLanguages.size(); i++) {
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.setOrderNumber(iDisplayStart + i + 1);
            languageDTO.setCode(cmsLanguages.get(i).getCode());
            languageDTO.setName(cmsLanguages.get(i).getName());
            languageDTO.setId(cmsLanguages.get(i).getId());
            languagesDTO.getAaData().add(languageDTO);
        }
        languagesDTO.setsEcho(sEcho);
        final Long numberOfLanguages = cmsLanguagesService.countForAjax();
        languagesDTO.setiTotalRecords(numberOfLanguages);
        languagesDTO.setiTotalDisplayRecords(numberOfLanguages);
        return languagesDTO;
    }

    @RequestMapping(value = "/cmsLanguagesDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public LanguagesDTO listCmsLanguagesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsLanguagesEntity> languages = cmsLanguagesService.listForDropList(term, limit);
        LanguagesDTO languagesDTO = new LanguagesDTO();
        for (CmsLanguagesEntity lang : languages) {
            LanguageDTO languageDTO = new LanguageDTO();
            languageDTO.setId(lang.getId());
            languageDTO.setCode(lang.getCode());
            languageDTO.setName(lang.getName());
            languagesDTO.getAaData().add(languageDTO);
        }
        return languagesDTO;
    }

    @RequestMapping(value = "/addLanguage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addLanguage(
        @ModelAttribute(value = "cmsLanguage") CmsLanguagesEntity cmsLanguage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageNameMustBeSet"));
        if (Locale.forLanguageTag(cmsLanguage.getCode()).getLanguage().isEmpty()) {
            result.rejectValue("code", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("Language2LetterCodeIsInvalid"));
        }
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            Optional<CmsLanguagesEntity> languageByCode = cmsLanguagesService.getByCode(cmsLanguage.getCode());
            if (!languageByCode.isPresent()) {
                cmsLanguagesService.add(cmsLanguage);
                basicResponse = getSuccess();
            } else {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageAlreadyExists"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteLanguage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteLanguage(
        @ModelAttribute(value = "cmsLanguage") CmsLanguagesEntity cmsLanguage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsLanguagesService.delete(cmsLanguage);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
