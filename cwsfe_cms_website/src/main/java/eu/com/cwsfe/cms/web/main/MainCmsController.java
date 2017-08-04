package eu.com.cwsfe.cms.web.main;

import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author Radoslaw Osinski
 */
@Controller
class MainCmsController extends JsonController {

    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").
        toFormatter().withZone(ZoneId.systemDefault());

    @RequestMapping(value = "/Main", method = RequestMethod.GET)
    public String printDashboard(ModelMap model, Principal principal, HttpServletRequest httpServletRequest) {
        model.addAttribute("userName", principal == null ? "?" : principal.getName());
        model.addAttribute("additionalCssCode", "");
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest));
        return "cms/main/Main";
    }

    private String getPageJS(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getContextPath() + "/resources-cwsfe-cms/js/cms/main/Dashboard";
    }

}
