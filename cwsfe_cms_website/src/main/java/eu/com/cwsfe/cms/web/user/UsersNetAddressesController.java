package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.db.users.CmsUserAllowedNetAddressEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.users.CmsUserAllowedNetAddressService;
import eu.com.cwsfe.cms.services.users.CmsUsersService;
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
class UsersNetAddressesController extends JsonController {

    private final CmsUsersService cmsUsersService;

    private final CmsUserAllowedNetAddressService cmsUserAllowedNetAddressService;

    @Autowired
    public UsersNetAddressesController(CmsUserAllowedNetAddressService cmsUserAllowedNetAddressService, CmsUsersService cmsUsersService) {
        this.cmsUserAllowedNetAddressService = cmsUserAllowedNetAddressService;
        this.cmsUsersService = cmsUsersService;
    }

    @RequestMapping(value = "/usersNetAddresses", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/usersNetAddresses/UsersNetAddresses";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/usersNetAddresses/UsersNetAddresses.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        String breadCrumbUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("usersNetAddresses").build().toUriString();
        String breadCrumbText = ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsersNetAddressesManagement");
        breadcrumbs.add(new BreadcrumbDTO(breadCrumbUrl, breadCrumbText));
        return breadcrumbs;
    }

    @RequestMapping(value = "/usersNetAddressesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listUsers(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        List<CmsUserAllowedNetAddressEntity> cmsUserAllowedNetAddresses = cmsUserAllowedNetAddressService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsUserAllowedNetAddresses.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            CmsUsersEntity cmsUser = cmsUsersService.get(cmsUserAllowedNetAddresses.get(i).getUserId());
            formDetailsJson.put("userName", cmsUser == null ? "---" : cmsUser.getUsername());
            formDetailsJson.put("inetAddress", cmsUserAllowedNetAddresses.get(i).getInetAddress());
            formDetailsJson.put("id", cmsUserAllowedNetAddresses.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfAllowedNetAddresses = cmsUserAllowedNetAddressService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfAllowedNetAddresses);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfAllowedNetAddresses);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNetAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addNetAddress(
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "inetAddress", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsUserAllowedNetAddressService.add(cmsUserAllowedNetAddress);
                addJsonSuccess(responseDetailsJson);
            } catch (Exception e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressIsIncorrect"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNetAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteUser(
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUserAllowedNetAddressService.delete(cmsUserAllowedNetAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
