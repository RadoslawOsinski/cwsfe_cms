package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.news.CmsNewsTypesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.news.NewsTypesService;
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
class NewsTypesController extends JsonController {

    private final NewsTypesService newsTypesService;

    @Autowired
    public NewsTypesController(NewsTypesService newsTypesService) {
        this.newsTypesService = newsTypesService;
    }

    @RequestMapping(value = "/newsTypes", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/newsTypes/NewsTypes";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsTypes/NewsTypes.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsTypes").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypesManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/newsTypesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public NewsTypesDTO listNewsTypes(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsNewsTypesEntity> cmsNewsTypes = newsTypesService.listAjax(iDisplayStart, iDisplayLength);
        NewsTypesDTO newsTypesDTO = new NewsTypesDTO();
        for (int i = 0; i < cmsNewsTypes.size(); i++) {
            NewsTypeDTO newsTypeDTO = new NewsTypeDTO();
            newsTypeDTO.setOrderNumber(iDisplayStart + i + 1);
            newsTypeDTO.setType(cmsNewsTypes.get(i).getType());
            newsTypeDTO.setId(cmsNewsTypes.get(i).getId());
            newsTypesDTO.getAaData().add(newsTypeDTO);
        }
        newsTypesDTO.setsEcho(sEcho);
        final Long numberOfNewsTypes = newsTypesService.countForAjax();
        newsTypesDTO.setiTotalRecords(numberOfNewsTypes);
        newsTypesDTO.setiTotalDisplayRecords(numberOfNewsTypes);
        return newsTypesDTO;
    }

    @RequestMapping(value = "/news/newsTypesDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public NewsTypesDTO listNewsTypesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsNewsTypesEntity> results = newsTypesService.listNewsTypesForDropList(term, limit);
        NewsTypesDTO newsTypesDTO = new NewsTypesDTO();
        for (CmsNewsTypesEntity newsType : results) {
            NewsTypeDTO newsTypeDTO = new NewsTypeDTO();
            newsTypeDTO.setId(newsType.getId());
            newsTypeDTO.setType(newsType.getType());
            newsTypesDTO.getAaData().add(newsTypeDTO);
        }
        return newsTypesDTO;
    }

    @RequestMapping(value = "/addNewsType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addNewsType(
        @ModelAttribute(value = "newsType") CmsNewsTypesEntity newsType,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "type", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            Optional<CmsNewsTypesEntity> existingNewsType = newsTypesService.getByType(newsType.getType());
            if (!existingNewsType.isPresent()) {
                newsTypesService.add(newsType);
                basicResponse = getSuccess();
            } else {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeAlreadyAdded"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteNewsType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteNewsType(
        @ModelAttribute(value = "cmsNewsType") CmsNewsTypesEntity cmsNewsType,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            newsTypesService.delete(cmsNewsType);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
