package eu.com.cwsfe.cms.web.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
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
public class AuthorsController extends JsonController {

    private final CmsAuthorsService cmsAuthorsService;

    @Autowired
    public AuthorsController(CmsAuthorsService cmsAuthorsService) {
        this.cmsAuthorsService = cmsAuthorsService;
    }

    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/authors/Authors";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/authors/Authors.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/authors").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorsManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/authorsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AuthorsDTO listAuthors(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsAuthorsEntity> cmsAuthors = cmsAuthorsService.listAjax(iDisplayStart, iDisplayLength);
        AuthorsDTO authorsDTO = new AuthorsDTO();
        for (int i = 0; i < cmsAuthors.size(); i++) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setOrderNumber(iDisplayStart + i + 1);
            authorDTO.setLastName(cmsAuthors.get(i).getLastName());
            authorDTO.setFirstName(cmsAuthors.get(i).getFirstName());
            authorDTO.setGooglePlusAuthorLink(cmsAuthors.get(i).getGooglePlusAuthorLink() == null ? "" : cmsAuthors.get(i).getGooglePlusAuthorLink());
            authorDTO.setId(cmsAuthors.get(i).getId());
            authorsDTO.getAaData().add(authorDTO);
        }
        authorsDTO.setsEcho(sEcho);
        final Long numberOfAuthors = cmsAuthorsService.countForAjax();
        authorsDTO.setiTotalRecords(numberOfAuthors);
        authorsDTO.setiTotalDisplayRecords(numberOfAuthors);
        return authorsDTO;
    }

    @RequestMapping(value = "/authorsDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AuthorsDTO listAuthorsForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsAuthorsEntity> cmsAuthors = cmsAuthorsService.listAuthorsForDropList(term, limit);
        AuthorsDTO authorsDTO = new AuthorsDTO();
        for (CmsAuthorsEntity cmsAuthor : cmsAuthors) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(cmsAuthor.getId());
            authorDTO.setLastName(cmsAuthor.getLastName());
            authorDTO.setFirstName(cmsAuthor.getFirstName());
            authorDTO.setGooglePlusAuthorLink(cmsAuthor.getGooglePlusAuthorLink());
            authorsDTO.getAaData().add(authorDTO);
        }
        return authorsDTO;
    }

    @RequestMapping(value = "/addAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addAuthor(
        @ModelAttribute(value = "cmsAuthor") CmsAuthorsEntity cmsAuthor,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "firstName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FirstNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "lastName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LastNameMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsAuthorsService.add(cmsAuthor);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteAuthor(
        @ModelAttribute(value = "cmsAuthor") CmsAuthorsEntity cmsAuthor,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsAuthorsService.delete(cmsAuthor);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
