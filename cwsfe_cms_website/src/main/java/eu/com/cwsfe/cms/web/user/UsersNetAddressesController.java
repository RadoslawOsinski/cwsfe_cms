package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.db.users.CmsUserAllowedNetAddressEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.users.CmsUserAllowedNetAddressService;
import eu.com.cwsfe.cms.services.users.CmsUsersService;
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
    public NetAddressesDTO listUsers(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        List<CmsUserAllowedNetAddressEntity> cmsUserAllowedNetAddresses = cmsUserAllowedNetAddressService.listAjax(iDisplayStart, iDisplayLength);
        NetAddressesDTO netAddressesDTO = new NetAddressesDTO();
        for (int i = 0; i < cmsUserAllowedNetAddresses.size(); i++) {
            NetAddressDTO netAddressDTO = new NetAddressDTO();
            netAddressDTO.setOrderNumber(iDisplayStart + i + 1);
            Optional<CmsUsersEntity> cmsUser = cmsUsersService.get(cmsUserAllowedNetAddresses.get(i).getUserId());
            netAddressDTO.setUserName(!cmsUser.isPresent() ? "---" : cmsUser.get().getUserName());
            netAddressDTO.setInetAddress(cmsUserAllowedNetAddresses.get(i).getInetAddress());
            netAddressDTO.setId(cmsUserAllowedNetAddresses.get(i).getId());
            netAddressesDTO.getAaData().add(netAddressDTO);
        }
        netAddressesDTO.setsEcho(sEcho);
        final Long numberOfAllowedNetAddresses = cmsUserAllowedNetAddressService.countForAjax();
        netAddressesDTO.setiTotalRecords(numberOfAllowedNetAddresses);
        netAddressesDTO.setiTotalDisplayRecords(numberOfAllowedNetAddresses);
        return netAddressesDTO;
    }

    @RequestMapping(value = "/addNetAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addNetAddress(
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("UsernameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "inetAddress", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            try {
                cmsUserAllowedNetAddressService.add(cmsUserAllowedNetAddress);
                basicResponse = getSuccess();
            } catch (Exception e) {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressIsIncorrect"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteNetAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteUser(
        @ModelAttribute(value = "cmsUserAllowedNetAddress") CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NetAddressMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsUserAllowedNetAddressService.delete(cmsUserAllowedNetAddress);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
