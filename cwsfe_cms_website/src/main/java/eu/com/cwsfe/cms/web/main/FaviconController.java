package eu.com.cwsfe.cms.web.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Radosław Osiński
 */
@Controller
public class FaviconController {

    @RequestMapping("favicon.ico")
    public String favicon() {
        return "forward:/resources-cwsfe-cms/favicon.ico";
    }

}
