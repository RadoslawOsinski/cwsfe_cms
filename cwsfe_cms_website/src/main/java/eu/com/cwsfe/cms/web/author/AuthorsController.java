package eu.com.cwsfe.cms.web.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.author.CmsAuthorsRepository;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
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
public class AuthorsController extends JsonController {

    private final CmsAuthorsRepository cmsAuthorsRepository;

    @Autowired
    public AuthorsController(CmsAuthorsRepository cmsAuthorsRepository) {
        this.cmsAuthorsRepository = cmsAuthorsRepository;
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
    public String listAuthors(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsAuthorsEntity> cmsAuthors = cmsAuthorsRepository.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsAuthors.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("lastName", cmsAuthors.get(i).getLastName());
            formDetailsJson.put("firstName", cmsAuthors.get(i).getFirstName());
            formDetailsJson.put("googlePlusAuthorLink", cmsAuthors.get(i).getGooglePlusAuthorLink() == null ? "" : cmsAuthors.get(i).getGooglePlusAuthorLink());
            formDetailsJson.put("id", cmsAuthors.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfAuthors = cmsAuthorsRepository.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfAuthors);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfAuthors);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/authorsDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listAuthorsForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsAuthorsEntity> cmsAuthors = cmsAuthorsRepository.listAuthorsForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsAuthorsEntity cmsAuthor : cmsAuthors) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", cmsAuthor.getId());
            formDetailsJson.put("lastName", cmsAuthor.getLastName());
            formDetailsJson.put("firstName", cmsAuthor.getFirstName());
            formDetailsJson.put("googlePlusAuthorLink", cmsAuthor.getGooglePlusAuthorLink());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addAuthor(
        @ModelAttribute(value = "cmsAuthor") CmsAuthorsEntity cmsAuthor,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "firstName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FirstNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "lastName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LastNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsAuthorsRepository.add(cmsAuthor);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteAuthor(
        @ModelAttribute(value = "cmsAuthor") CmsAuthorsEntity cmsAuthor,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsAuthorsRepository.delete(cmsAuthor);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
