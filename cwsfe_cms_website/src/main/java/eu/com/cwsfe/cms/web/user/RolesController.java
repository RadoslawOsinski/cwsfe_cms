package eu.com.cwsfe.cms.web.user;

import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.users.CmsRolesService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
public class RolesController extends JsonController {

    private final CmsRolesService cmsRolesService;

    @Autowired
    public RolesController(CmsRolesService cmsRolesService) {
        this.cmsRolesService = cmsRolesService;
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/roles/Roles";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/roles/Roles.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/roles").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("RolesManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/rolesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public RolesDTO listRoles(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsRolesEntity> cmsRoles = cmsRolesService.listAjax(iDisplayStart, iDisplayLength);
        RolesDTO rolesDTO = new RolesDTO();
        for (int i = 0; i < cmsRoles.size(); i++) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setOrderNumber(iDisplayStart + i + 1);
            roleDTO.setRoleCode(cmsRoles.get(i).getRoleCode());
            roleDTO.setRoleName(cmsRoles.get(i).getRoleName());
            rolesDTO.getAaData().add(roleDTO);
        }
        rolesDTO.setsEcho(sEcho);
        final Long numberOfRoles = cmsRolesService.countForAjax();
        rolesDTO.setiTotalRecords(numberOfRoles);
        rolesDTO.setiTotalDisplayRecords(numberOfRoles);
        return rolesDTO;
    }

    @RequestMapping(value = "/rolesDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public RolesDTO listRolesForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsRolesEntity> cmsRoles = cmsRolesService.listRolesForDropList(term, limit);
        RolesDTO rolesDTO = new RolesDTO();
        for (CmsRolesEntity cmsRole : cmsRoles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setOrderNumber(Long.valueOf(cmsRole.getId()).intValue());
            roleDTO.setRoleName(cmsRole.getRoleName());
            rolesDTO.getAaData().add(roleDTO);
        }
        return rolesDTO;
    }

}
