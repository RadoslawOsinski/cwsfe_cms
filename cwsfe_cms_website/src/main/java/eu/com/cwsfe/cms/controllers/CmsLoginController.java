package eu.com.cwsfe.cms.controllers;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
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

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

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
        model.addAttribute("mainSiteUrl", cmsGlobalParamsDAO.getByCode("MAIN_SITE").getValue());
    }

}
