package eu.com.cwsfe.cms.web.login;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Radoslaw Osinski
 */
@Controller
class CmsLoginController {

    private static final Logger LOG = LoggerFactory.getLogger(CmsLoginController.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    @Autowired
    public CmsLoginController(CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginPage(ModelMap model, HttpServletRequest req) {
        addMainSiteUrl(model);
//        pushJsAndCssForLoginPage(req);
        return "cms/login/Login";
    }

//    private void pushJsAndCssForLoginPage(HttpServletRequest req) {
//        PushBuilder pushBuilder = req.newPushBuilder();
//        if (pushBuilder != null) {
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/css/foundation/normalize.css").addHeader("content-type", "text/css").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/css/foundation/foundation.css").addHeader("content-type", "text/css").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/css/cms/shared.css").addHeader("content-type", "text/css").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/css/cms/colors.css").addHeader("content-type", "text/css").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/css/login/login.css").addHeader("content-type", "text/css").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/js/jquery/jquery-2.1.1.min.js").addHeader("content-type", "application/js").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/js/foundation/foundation.min.js").addHeader("content-type", "application/js").push();
//            pushBuilder.path(req.getContextPath() + "/resources-cwsfe-cms/CWSFE_logo.png").addHeader("content-type", "application/js").push();
//        }
//    }

    @RequestMapping(value = "/loginPage")
    public String login(ModelMap model, HttpServletRequest req) {
        addMainSiteUrl(model);
//        pushJsAndCssForLoginPage(req);
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
        Optional<CmsGlobalParamsEntity> mainSite = cmsGlobalParamsService.getByCode("MAIN_SITE");
        if (mainSite.isPresent()) {
            model.addAttribute("mainSiteUrl", mainSite.get().getValue());
        } else {
            LOG.error("Missing configuration MAIN_SITE");
            model.addAttribute("mainSiteUrl", "");
        }
    }

}
