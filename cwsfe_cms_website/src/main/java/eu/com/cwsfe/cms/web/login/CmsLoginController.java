package eu.com.cwsfe.cms.web.login;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Radoslaw Osinski
 */
@Controller
class CmsLoginController {

    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    @Autowired
    public CmsLoginController(CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginPage(ModelMap model) {
        addMainSiteUrl(model);
        return "cms/login/Login";
    }

    @RequestMapping(value = "/loginPage")
    public String login(ModelMap model) {
        addMainSiteUrl(model);
        return "cms/login/Login";
    }

    @RequestMapping(value = "/loginFailed", method = RequestMethod.GET)
    public String loginError(ModelMap model) {
        model.addAttribute("error", "true");
        addMainSiteUrl(model);
        return "cms/login/Login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap model) {
        addMainSiteUrl(model);
        return "cms/login/Login";
    }

    private void addMainSiteUrl(ModelMap model) {
        model.addAttribute("mainSiteUrl", cmsGlobalParamsRepository.getByCode("MAIN_SITE").getValue());
    }

}
