package eu.com.cwsfe.cms.web.newsletter;

import eu.com.cwsfe.cms.EmailValidator;
import eu.com.cwsfe.cms.UUIDGenerator;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import eu.com.cwsfe.cms.dao.CmsLanguagesDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailAddressDAO;
import eu.com.cwsfe.cms.dao.NewsletterMailGroupDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.NewsletterMailAddress;
import eu.com.cwsfe.cms.model.NewsletterMailGroup;
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
class NewsletterMailGroupsController extends JsonController {

    @Autowired
    private NewsletterMailGroupDAO newsletterMailGroupDAO;
    @Autowired
    private NewsletterMailAddressDAO newsletterMailAddressDAO;
    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    @RequestMapping(value = "/newsletterMailGroups", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/newsletterMailGroups/NewsletterMailGroups";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/newsletterMailGroups/NewsletterMailGroups.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        //todo pozmieniać fromCurrentContextPath() aby działało na produkcji
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMailGroups").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupsManagement")));
        return breadcrumbs;
    }

    private Object setSingleNewsletterMailGroupsAdditionalJS(String contextPath) {
        List<String> jsUrl = new ArrayList<>(3);
        jsUrl.add(contextPath + "/resources-cwsfe-cms/js/cms/newsletterMailGroups/SingleNewsletterMailGroup.js");
        return jsUrl;
    }

    private List<Breadcrumb> getSingleNewsletterMailGroupsBreadcrumbs(Locale locale, Long id) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMailGroups").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupsManagement")));
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/newsletterMailGroups/" + id).build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentNewsletterMailGroup")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/newsletterMailGroupsDropList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listNewsletterMailGroupsForDropList(
            @RequestParam String term,
            @RequestParam Integer limit
    ) {
        final List<NewsletterMailGroup> results = newsletterMailGroupDAO.listNewsletterMailGroupsForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (NewsletterMailGroup newsletterMailGroup : results) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", newsletterMailGroup.getId());
            formDetailsJson.put("newsletterMailGroupName", newsletterMailGroup.getName());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroupsList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listNewsletterMailGroups(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho,
            @RequestParam(required = false) Long searchLanguageId,
            @RequestParam(required = false) String searchName
    ) {
        List<NewsletterMailGroup> dbList = newsletterMailGroupDAO.searchByAjax(iDisplayStart, iDisplayLength, searchName, searchLanguageId);
        Integer dbListDisplayRecordsSize = newsletterMailGroupDAO.searchByAjaxCount(searchName, searchLanguageId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final NewsletterMailGroup objects = dbList.get(i);
            formDetailsJson.put("language2LetterCode", cmsLanguagesDAO.getById(objects.getLanguageId()).getCode());
            formDetailsJson.put("newsletterMailGroupName", objects.getName());
            formDetailsJson.put("id", objects.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNewsletterMailGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addNewsletterMailGroup(
            @ModelAttribute(value = "newsletterMailGroup") NewsletterMailGroup newsletterMailGroup,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailGroupDAO.add(newsletterMailGroup);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNewsletterMailGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deleteNewsletterMailGroup(
            @ModelAttribute(value = "newsletterMailGroup") NewsletterMailGroup newsletterMailGroup,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailGroupDAO.delete(newsletterMailGroup);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/{id}", method = RequestMethod.GET)
    public String browseNewsletterMailGroup(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        //todo
//        model.addAttribute("mainJavaScript", setSingleNewsletterMailGroupsAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleNewsletterMailGroupsBreadcrumbs(locale, id));
        NewsletterMailGroup newsletterMailGroup = newsletterMailGroupDAO.get(id);
        model.addAttribute("newsletterMailGroup", newsletterMailGroup);
        model.addAttribute("newsletterMailGroupLanguageCode", cmsLanguagesDAO.getById(newsletterMailGroup.getLanguageId()).getCode());
        return "cms/newsletterMailGroups/SingleNewsletterMailGroup";
    }

    @RequestMapping(value = "/newsletterMailGroups/newsletterMailAddressesList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String newsletterMailAddressesList(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho,
            @RequestParam(required = false) String searchMail,
            @RequestParam(required = false) Long mailGroupId
    ) {
        List<NewsletterMailAddress> dbList = newsletterMailAddressDAO.searchByAjax(iDisplayStart, iDisplayLength, searchMail, mailGroupId);
        Integer dbListDisplayRecordsSize = newsletterMailAddressDAO.searchByAjaxCount(searchMail, mailGroupId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final NewsletterMailAddress objects = dbList.get(i);
            formDetailsJson.put("email", objects.getEmail());
            formDetailsJson.put("status", objects.getStatus());
            formDetailsJson.put("id", objects.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/updateNewsletterMailGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String updateNewsletterMailGroup(
            @ModelAttribute(value = "newsletterMailGroup") NewsletterMailGroup newsletterMailGroup,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "name", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailGroupNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailGroupDAO.update(newsletterMailGroup);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/addNewsletterMailAddresses", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addNewsletterMailAddresses(
            @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "mailGroupId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailAddressGroupMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailAddressMustBeSet"));
        if (!EmailValidator.isValidEmailAddress(newsletterMailAddress.getEmail())) {
            result.rejectValue("email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailIsInvalid"));
        } else {
            NewsletterMailAddress existingMailAddress = newsletterMailAddressDAO.getByEmailAndMailGroup(newsletterMailAddress.getEmail(), newsletterMailAddress.getMailGroupId());
            if (existingMailAddress != null) {
                result.rejectValue("email", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("EmailAlreadyAdded"));
            }
        }
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailAddress.setConfirmString(UUIDGenerator.getRandomUniqueID());
            while (newsletterMailAddressDAO.getByConfirmString(newsletterMailAddress.getConfirmString()) != null) {
                newsletterMailAddress.setConfirmString(UUIDGenerator.getRandomUniqueID());
            }
            newsletterMailAddress.setUnSubscribeString(UUIDGenerator.getRandomUniqueID());
            while (newsletterMailAddressDAO.getByUnSubscribeString(newsletterMailAddress.getUnSubscribeString()) != null) {
                newsletterMailAddress.setUnSubscribeString(UUIDGenerator.getRandomUniqueID());
            }
            newsletterMailAddressDAO.add(newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/deleteNewsletterMailAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deleteNewsletterMailAddress(
            @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailAddressDAO.delete(newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/activateNewsletterMailAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String activateNewsletterMailAddress(
            @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailAddressDAO.activate(newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/newsletterMailGroups/deactivateNewsletterMailAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deactivateNewsletterMailAddress(
            @ModelAttribute(value = "newsletterMailAddress") NewsletterMailAddress newsletterMailAddress,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsletterMailAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            newsletterMailAddressDAO.deactivate(newsletterMailAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
