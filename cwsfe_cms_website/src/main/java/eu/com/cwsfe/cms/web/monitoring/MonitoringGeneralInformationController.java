package eu.com.cwsfe.cms.web.monitoring;

import eu.com.cwsfe.cms.web.mvc.JsonController;
import eu.com.cwsfe.cms.model.Breadcrumb;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class MonitoringGeneralInformationController extends JsonController {

    @Autowired
    private ServerWatch serverWatch;

    @RequestMapping(value = "/monitoring/generalInformation", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        model.addAttribute("osName", serverWatch.getOSName());
        model.addAttribute("osVersion", serverWatch.getOSVersion());
        model.addAttribute("architecture", serverWatch.getArchitecture());
        model.addAttribute("availableCPUs", serverWatch.getAvailableCPUs());
        model.addAttribute("usedMemoryInMb", serverWatch.usedMemoryInMb());
        model.addAttribute("availableMemoryInMB", serverWatch.availableMemoryInMB());
        return "cms/monitoring/GeneralInformation";
    }

    protected String getPageJS(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getContextPath() + "/resources-cwsfe-cms/js/cms/monitoring/GeneralInformation";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/monitoring/generalInformation").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("MonitoringGeneralInformation")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/monitoring/generalMemoryInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String getGeneralMemoryInfo() {
        JSONObject responseDetailsJson = new JSONObject();
        responseDetailsJson.put("usedMemoryInMb", serverWatch.usedMemoryInMb());
        responseDetailsJson.put("availableMemoryInMB", serverWatch.availableMemoryInMB());
        return responseDetailsJson.toString();
    }

}
