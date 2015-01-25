package eu.com.cwsfe.cms.configuration;

import eu.com.cwsfe.cms.mvc.JsonController;
import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.dao.CmsRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUserRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsRole;
import eu.com.cwsfe.cms.model.CmsUser;
import eu.com.cwsfe.cms.model.CmsUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
class InitialConfigurationController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialConfigurationController.class);

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @Autowired
    private CmsUsersDAO cmsUsersDAO;

    @Autowired
    private CmsRolesDAO cmsRolesDAO;

    @Autowired
    private CmsUserRolesDAO cmsUserRolesDAO;

    @RequestMapping(value = "/configuration/initialConfiguration", method = RequestMethod.GET)
    public String showInitialConfiguration() {
        try {
            CmsGlobalParam cwsfeCmsIsConfigured = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_IS_CONFIGURED");
            if (cwsfeCmsIsConfigured == null || cwsfeCmsIsConfigured.getValue() == null || "N".equals(cwsfeCmsIsConfigured.getValue())) {
                return "cms/configuration/InitialConfiguration";
            } else {
                return "cms/login/Login";
            }
        } catch (DataAccessException e) {
            LOGGER.error("Problem with initial configuration", e);
            return "cms/configuration/InitialConfiguration";
        }
    }

    @RequestMapping(value = "/configuration/addAdminUser", method = RequestMethod.POST)
    public ModelAndView addAdminUser(
            @ModelAttribute(value = "cmsUser") CmsUser cmsUser,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "userName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FirstNameMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "password", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PasswordMustBeSet"));
        ModelAndView modelAndView = new ModelAndView();
        if (!result.hasErrors()) {
            CmsGlobalParam cwsfeCmsIsConfigured = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_IS_CONFIGURED");
            if ("N".equals(cwsfeCmsIsConfigured.getValue())) {
                cmsUser.setPasswordHash(BCrypt.hashpw(cmsUser.getPassword(), BCrypt.gensalt(13)));
                cmsUser.setId(cmsUsersDAO.add(cmsUser));
                CmsRole cwsfeCmsAdminRole = cmsRolesDAO.getByCode("ROLE_CWSFE_CMS_ADMIN");
                cmsUserRolesDAO.add(new CmsUserRole(cmsUser.getId(), cwsfeCmsAdminRole.getId()));
                cwsfeCmsIsConfigured.setValue("Y");
                cmsGlobalParamsDAO.update(cwsfeCmsIsConfigured);
            }
            modelAndView.setView(new RedirectView("/", true, false, false));
        } else {
            StringBuilder errorMessage = new StringBuilder();
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                errorMessage.append(result.getAllErrors().get(i).getCode());
                errorMessage.append("<br/>");
            }
            modelAndView.getModel().put("errors", errorMessage);
            modelAndView.setView(new RedirectView("/configuration/initialConfiguration", true, false, false));
        }
        return modelAndView;
    }

}
