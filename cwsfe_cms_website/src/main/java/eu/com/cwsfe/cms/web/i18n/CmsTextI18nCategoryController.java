package eu.com.cwsfe.cms.web.i18n;

import eu.com.cwsfe.cms.db.news.CmsTextI18NCategoriesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.news.CmsTextI18nCategoryService;
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
    public CmsTextI18NCategoriesDTO listCmsTextI18nCategories(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsTextI18NCategoriesEntity> cmsTextI18nCategories = cmsTextI18nCategoryService.listAjax(iDisplayStart, iDisplayLength);
        CmsTextI18NCategoriesDTO cmsTextI18NCategoriesDTO = new CmsTextI18NCategoriesDTO();
        for (int i = 0; i < cmsTextI18nCategories.size(); i++) {
            CmsTextI18NCategoryDTO cmsTextI18NCategoryDTO = new CmsTextI18NCategoryDTO();
            cmsTextI18NCategoryDTO.setOrderNumber(iDisplayStart + i + 1);
            cmsTextI18NCategoryDTO.setCategory(cmsTextI18nCategories.get(i).getCategory());
            cmsTextI18NCategoryDTO.setStatus(cmsTextI18nCategories.get(i).getStatus());
            cmsTextI18NCategoryDTO.setId(cmsTextI18nCategories.get(i).getId());
            cmsTextI18NCategoriesDTO.getAaData().add(cmsTextI18NCategoryDTO);
        }
        cmsTextI18NCategoriesDTO.setsEcho(sEcho);
        final Long numberOfCategories = cmsTextI18nCategoryService.countForAjax();
        cmsTextI18NCategoriesDTO.setiTotalRecords(numberOfCategories);
        cmsTextI18NCategoriesDTO.setiTotalDisplayRecords(numberOfCategories);
        return cmsTextI18NCategoriesDTO;
    }

    @RequestMapping(value = "/cmsTextI18nCategoryDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CmsTextI18NCategoriesDTO listCmsLanguagesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsTextI18NCategoriesEntity> cmsTextI18nCategories = cmsTextI18nCategoryService.listForDropList(term, limit);
        CmsTextI18NCategoriesDTO cmsTextI18NCategoriesDTO = new CmsTextI18NCategoriesDTO();
        for (CmsTextI18NCategoriesEntity lang : cmsTextI18nCategories) {
            CmsTextI18NCategoryDTO cmsTextI18NCategoryDTO = new CmsTextI18NCategoryDTO();
            cmsTextI18NCategoryDTO.setId(lang.getId());
            cmsTextI18NCategoryDTO.setCategory(lang.getCategory());
            cmsTextI18NCategoriesDTO.getAaData().add(cmsTextI18NCategoryDTO);
        }
        return cmsTextI18NCategoriesDTO;
    }


    @RequestMapping(value = "/addCmsTextI18nCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addTextI18nCategory(
        @ModelAttribute(value = "cmsTextI18nCategory") CmsTextI18NCategoriesEntity cmsTextI18nCategory,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "category", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CategoryMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsTextI18nCategoryService.add(cmsTextI18nCategory);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteCmsTextI18nCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteTextI18nCategory(
        @ModelAttribute(value = "cmsTextI18nCategory") CmsTextI18NCategoriesEntity cmsTextI18nCategory,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TextI18nCategoryMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsTextI18nCategoryService.delete(cmsTextI18nCategory);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
