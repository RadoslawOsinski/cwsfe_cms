package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.users.CmsRolesService;
import eu.com.cwsfe.cms.services.users.CmsUserRolesService;
import eu.com.cwsfe.cms.services.users.CmsUsersService;
import eu.com.cwsfe.cms.web.mvc.BasicResponse;
import eu.com.cwsfe.cms.web.mvc.JsonController;
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
    public UsersDTO listUsers(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsUsersEntity> cmsUsers = cmsUsersService.listAjax(iDisplayStart, iDisplayLength);
        UsersDTO usersDTO = new UsersDTO();
        for (int i = 0; i < cmsUsers.size(); i++) {
            UserDTO userDTO = new UserDTO();
            userDTO.setOrderNumber(iDisplayStart + i + 1);
            userDTO.setUserName(cmsUsers.get(i).getUserName());
            userDTO.setStatus(cmsUsers.get(i).getStatus());
            userDTO.setId(cmsUsers.get(i).getId());
            usersDTO.getAaData().add(userDTO);
        }
        usersDTO.setsEcho(sEcho);
        final Long numberOfUsers = cmsUsersService.countForAjax();
        usersDTO.setiTotalRecords(numberOfUsers);
        usersDTO.setiTotalDisplayRecords(numberOfUsers);
        return usersDTO;
    }

    @RequestMapping(value = "/usersDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UsersDTO listUsersForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsUsersEntity> cmsUsers = cmsUsersService.listUsersForDropList(term, limit);
        UsersDTO usersDTO = new UsersDTO();
        for (CmsUsersEntity cmsUser : cmsUsers) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(cmsUser.getId());
            userDTO.setUserName(cmsUser.getUserName());
            usersDTO.getAaData().add(userDTO);
        }
        return usersDTO;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "passwordHash", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PasswordMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUser.setPasswordHash(BCrypt.hashpw(cmsUser.getPasswordHash(), BCrypt.gensalt(13)));
            Optional<CmsUsersEntity> userByUserName = cmsUsersService.getByUsername(cmsUser.getUserName());
            if (userByUserName.isPresent()) {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserAlreadyExists"));
            } else {
                cmsUsersService.add(cmsUser);
                basicResponse = getSuccess();
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUsersService.delete(cmsUser);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/lockUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse lockUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUsersService.lock(cmsUser);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/unlockUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse unlockUser(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUsersService.unlock(cmsUser);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String browseUser(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleUserAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleUserBreadcrumbs(locale, id));
        final Optional<CmsUsersEntity> cmsUser = cmsUsersService.get(id);
        if (cmsUser.isPresent()) {
            model.addAttribute("cmsUser", cmsUser.get());
            List<Long> userSelectedRoles = new ArrayList<>(5);
            userSelectedRoles.addAll(cmsUserRolesService.listUserRoles(
                cmsUser.get().getId()).stream().map(CmsRolesEntity::getId).collect(Collectors.toList())
            );
            model.addAttribute("userSelectedRoles", userSelectedRoles);
        }
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
    public BasicResponse updateUserBasicInfo(
        @ModelAttribute(value = "cmsUser") CmsUsersEntity cmsUser,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UserMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "userName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUsersService.updatePostBasicInfo(cmsUser);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
