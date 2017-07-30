package eu.com.cwsfe.cms.web.login;

import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Radoslaw Osinski
 */
@Controller
class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    @Autowired
    public ErrorController(CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest req, ModelMap model) {
        try {
            LOGGER.error("Error occurred: ", req.getAttribute("javax.servlet.error.exception"));
        } catch (Exception ignored) {
            //ignored logging if exception is missing in request
        }
        addMainSiteUrl(model);
        return "cms/errors/error";
    }

    private void addMainSiteUrl(ModelMap model) {
        model.addAttribute("mainSiteUrl", cmsGlobalParamsService.getByCode("MAIN_SITE").getValue());
    }

}
