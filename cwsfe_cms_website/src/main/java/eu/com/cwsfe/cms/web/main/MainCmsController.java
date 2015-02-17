package eu.com.cwsfe.cms.web.main;

import eu.com.cwsfe.cms.dao.BlogPostCommentsDAO;
import eu.com.cwsfe.cms.model.BlogPostComment;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

/**
 * @author Radoslaw Osinski
 */
@Controller
class MainCmsController extends JsonController {

    @Autowired
    private BlogPostCommentsDAO blogPostCommentsDAO;

    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").
            toFormatter().withZone(ZoneId.systemDefault());

    @RequestMapping(value = "/Main", method = RequestMethod.GET)
    public String printDashboard(ModelMap model, Principal principal, HttpServletRequest httpServletRequest) {
        String name = principal.getName();
        model.addAttribute("userName", name);
        model.addAttribute("additionalCssCode", "");
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest));
        return "cms/main/Main";
    }

    private String getPageJS(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getContextPath() + "/resources-cwsfe-cms/js/cms/main/Dashboard";
    }

    @RequestMapping(value = "/blogPostCommentsList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listBlogPostComments(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho
    ) {
        List<BlogPostComment> dbList = blogPostCommentsDAO.searchByAjax(iDisplayStart, iDisplayLength);
        Integer dbListDisplayRecordsSize = blogPostCommentsDAO.searchByAjaxCount();
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final BlogPostComment objects = dbList.get(i);
            formDetailsJson.put("userName", objects.getUserName() + "[" + objects.getEmail() + "]");
            formDetailsJson.put("comment", objects.getComment());
            formDetailsJson.put("created", DATE_FORMAT.format(objects.getCreated().toInstant()));
            formDetailsJson.put("status", objects.getStatus());
            formDetailsJson.put("id", objects.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", blogPostCommentsDAO.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

}
