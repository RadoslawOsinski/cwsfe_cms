package eu.com.cwsfe.cms.web.configuration;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUserRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import eu.com.cwsfe.cms.services.users.CmsRolesService;
import eu.com.cwsfe.cms.services.users.CmsUserRolesService;
import eu.com.cwsfe.cms.services.users.CmsUsersService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Service
class InitialConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialConfigurationService.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    private final CmsUsersService cmsUsersService;

    private final CmsRolesService cmsRolesService;

    private final CmsUserRolesService cmsUserRolesService;

    @Autowired
    public InitialConfigurationService(CmsUsersService cmsUsersService, CmsUserRolesService cmsUserRolesService, CmsRolesService cmsRolesService, CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsUsersService = cmsUsersService;
        this.cmsUserRolesService = cmsUserRolesService;
        this.cmsRolesService = cmsRolesService;
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    boolean isCwsfeCmsConfigured(Optional<CmsGlobalParamsEntity> cwsfeCmsIsConfigured) {
        return cwsfeCmsIsConfigured.isPresent() && "N".equals(cwsfeCmsIsConfigured.get().getValue());
    }

    void configureCwsfeCms(CmsUsersEntity cmsUser, Optional<CmsGlobalParamsEntity> cwsfeCmsIsConfigured) {
        cmsUser.setPasswordHash(BCrypt.hashpw(cmsUser.getPasswordHash(), BCrypt.gensalt()));
        cmsUser.setId(cmsUsersService.add(cmsUser));
        Optional<CmsRolesEntity> cwsfeCmsAdminRole = cmsRolesService.getByCode("ROLE_CWSFE_CMS_ADMIN");
        CmsUserRolesEntity cmsUserRole = new CmsUserRolesEntity();
        cmsUserRole.setCmsUserId(cmsUser.getId());
        cmsUserRole.setRoleId(cwsfeCmsAdminRole.get().getId());
        cmsUserRolesService.add(cmsUserRole);
        cwsfeCmsIsConfigured.get().setValue("Y");
        cmsGlobalParamsService.update(cwsfeCmsIsConfigured.get());
    }

}
