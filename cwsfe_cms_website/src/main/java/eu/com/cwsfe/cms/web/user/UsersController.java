package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.users.CmsRolesService;
import eu.com.cwsfe.cms.services.users.CmsUserRolesService;
import eu.com.cwsfe.cms.services.users.CmsUsersService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Radoslaw Osinski
 */
@Controller
class UsersController extends JsonController {

    private final CmsUsersService cmsUsersService;
    private final CmsRolesService cmsRolesService;
    private final CmsUserRolesService cmsUserRolesService;

    @Autowired
    public UsersController(CmsUsersService cmsUsersService, CmsRolesService cmsRolesService, CmsUserRolesService cmsUserRolesService) {
        this.cmsUsersService = cmsUsersService;
        this.cmsRolesService = cmsRolesService;
        this.cmsUserRolesService = cmsUserRolesService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/users/Users";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/users/Users.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        String breadCrumbUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").build().toUriString();
        String breadCrumbText = ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsersManagement");
        breadcrumbs.add(new BreadcrumbDTO(breadCrumbUrl, breadCrumbText));
        return breadcrumbs;
    }

    private String setSingleUserAdditionalJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/users/SingleUser.js";
    }

    private List<BreadcrumbDTO> getSingleUserBreadcrumbs(Locale locale, Long id) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").build().toUriString(), ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsersManagement")));
        breadcrumbs.add(new BreadcrumbDTO(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/" + id).build().toUriString(), ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SelectedUser")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/usersList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listUsers(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsUsersEntity> cmsUsers = cmsUsersService.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsUsers.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("userName", cmsUsers.get(i).getUserName());
            formDetailsJson.put("status", cmsUsers.get(i).getStatus());
            formDetailsJson.put("id", cmsUsers.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final Long numberOfUsers = cmsUsersService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfUsers);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfUsers);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/usersDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listUsersForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsUsersEntity> cmsUsers = cmsUsersService.listUsersForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsUsersEntity cmsUser : cmsUsers) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", cmsUser.getId());
            formDetailsJson.put("userName", cmsUser.getUserName());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "passwordHash", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PasswordMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUser.setPasswordHash(BCrypt.hashpw(cmsUser.getPasswordHash(), BCrypt.gensalt(13)));
            Optional<CmsUsersEntity> userByUserName = cmsUsersService.getByUsername(cmsUser.getUserName());
            if (userByUserName.isPresent()) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserAlreadyExists"));
            } else {
                cmsUsersService.add(cmsUser);
                addJsonSuccess(responseDetailsJson);
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUsersService.delete(cmsUser);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/lockUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String lockUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUsersService.lock(cmsUser);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/unlockUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String unlockUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUsersService.unlock(cmsUser);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String browseUser(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleUserAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleUserBreadcrumbs(locale, id));
        final CmsUsersEntity cmsUser = cmsUsersService.get(id);
        model.addAttribute("cmsUser", cmsUser);
        List<Long> userSelectedRoles = new ArrayList<>(5);
        userSelectedRoles.addAll(cmsUserRolesService.listUserRoles(cmsUser.getId()).stream().map(CmsRolesEntity::getId).collect(Collectors.toList()));
        model.addAttribute("userSelectedRoles", userSelectedRoles);
        model.addAttribute("cmsRoles", cmsRolesService.list());
        return "cms/users/SingleUser";
    }

    @RequestMapping(value = "/userRolesUpdate", method = RequestMethod.POST)
    public ModelAndView userRolesUpdate(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        ModelMap model, Locale locale,
        WebRequest webRequest, HttpServletRequest httpServletRequest
    ) {
        String[] userRolesStrings = webRequest.getParameterValues("cmsUserRoles");
        cmsUserRolesService.updateUserRoles(cmsUser.getId(), userRolesStrings);
        browseUser(model, locale, cmsUser.getId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/users/" + cmsUser.getId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/users/updateUserBasicInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateUserBasicInfo(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "userName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsUsersService.updatePostBasicInfo(cmsUser);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
