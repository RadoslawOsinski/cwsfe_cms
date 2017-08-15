package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.news.CmsTextI18NEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
import eu.com.cwsfe.cms.services.news.CmsTextI18nCategoryService;
import eu.com.cwsfe.cms.services.news.CmsTextI18nService;
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
    public TextI18nsDTO listCmsTextI18n(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsTextI18NEntity> cmsTextI18ns = cmsTextI18nService.listAjax(iDisplayStart, iDisplayLength);
        TextI18nsDTO textI18nsDTO = new TextI18nsDTO();
        for (int i = 0; i < cmsTextI18ns.size(); i++) {
            TextI18nDTO textI18nDTO = new TextI18nDTO();
            textI18nDTO.setOrderNumber(iDisplayStart + i + 1);
            textI18nDTO.setLanguage(cmsLanguagesService.getById(cmsTextI18ns.get(i).getLangId()).getCode());
            textI18nDTO.setCategory(cmsTextI18nCategoryService.get(cmsTextI18ns.get(i).getI18nCategory()).getCategory());
            textI18nDTO.setKey(cmsTextI18ns.get(i).getI18NKey());
            textI18nDTO.setText(cmsTextI18ns.get(i).getI18NText());
            textI18nDTO.setId(cmsTextI18ns.get(i).getId());
            textI18nsDTO.getAaData().add(textI18nDTO);
        }
        textI18nsDTO.setsEcho(sEcho);
        final Long numberOfAuthors = cmsTextI18nService.countForAjax();
        textI18nsDTO.setiTotalRecords(numberOfAuthors);
        textI18nsDTO.setiTotalDisplayRecords(numberOfAuthors);
        return textI18nsDTO;
    }

    @RequestMapping(value = "/addCmsTextI18n", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addTextI18n(
        @ModelAttribute(value = "cmsTextI18n") CmsTextI18NEntity cmsTextI18n,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "langId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18nCategory", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CategoryMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18NKey", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("KeyMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "i18NText", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            Optional<CmsTextI18NEntity> existingTextI18n = cmsTextI18nService.getExisting(cmsTextI18n);
            if (!existingTextI18n.isPresent()) {
                cmsTextI18nService.add(cmsTextI18n);
                basicResponse = getSuccess();
            } else {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nAlreadyExists"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteCmsTextI18n", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteTextI18n(
        @ModelAttribute(value = "cmsTextI18n") CmsTextI18NEntity cmsTextI18n,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsTextI18nService.delete(cmsTextI18n);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
