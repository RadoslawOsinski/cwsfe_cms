package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.web.mvc.JsonController;
import eu.com.cwsfe.cms.dao.CmsUserAllowedNetAddressDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserAllowedNetAddress;
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

    private final CmsUsersRepository cmsUsersDAO;

    private final CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressDAO;

    @Autowired
    public UsersNetAddressesController(CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressDAO, CmsUsersRepository cmsUsersDAO) {
        this.cmsUserAllowedNetAddressRepository = cmsUserAllowedNetAddressDAO;
        this.cmsUsersRepository = cmsUsersDAO;
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

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        String breadCrumbUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("usersNetAddresses").build().toUriString();
        String breadCrumbText = ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsersNetAddressesManagement");
        breadcrumbs.add(new Breadcrumb(breadCrumbUrl, breadCrumbText));
        return breadcrumbs;
    }

    @RequestMapping(value = "/usersNetAddressesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listUsers(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        List<CmsUserAllowedNetAddress> cmsUserAllowedNetAddresses = cmsUserAllowedNetAddressDAO.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsUserAllowedNetAddresses.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            CmsUser cmsUser = cmsUsersDAO.get(cmsUserAllowedNetAddresses.get(i).getUserId());
            formDetailsJson.put("userName", cmsUser == null ? "---" : cmsUser.getUserName());
            formDetailsJson.put("inetAddress", cmsUserAllowedNetAddresses.get(i).getInetAddress());
            formDetailsJson.put("id", cmsUserAllowedNetAddresses.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfAllowedNetAddresses = cmsUserAllowedNetAddressDAO.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfAllowedNetAddresses);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfAllowedNetAddresses);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNetAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addNetAddress(
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddress cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "inetAddress", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsUserAllowedNetAddressDAO.add(cmsUserAllowedNetAddress);
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
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddress cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUserAllowedNetAddressDAO.delete(cmsUserAllowedNetAddress);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
