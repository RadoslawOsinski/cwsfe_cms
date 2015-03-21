package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.web.mvc.JsonController;
import eu.com.cwsfe.cms.dao.CmsFoldersDAO;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.CmsFolder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
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
public class FoldersController extends JsonController {

    @Autowired
    private CmsFoldersDAO cmsFoldersDAO;

    @RequestMapping(value = "/folders", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/folders/Folders";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/folders/Folders.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/folders").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FoldersManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/foldersList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listFolders(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho
    ) {
        final List<CmsFolder> cmsFolders = cmsFoldersDAO.listAjax(iDisplayStart, iDisplayLength);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < cmsFolders.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            formDetailsJson.put("folderName", cmsFolders.get(i).getFolderName());
            formDetailsJson.put("orderNumber", cmsFolders.get(i).getOrderNumber());
            formDetailsJson.put("id", cmsFolders.get(i).getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        final int numberOfFolders = cmsFoldersDAO.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfFolders);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfFolders);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/foldersDropList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String listFoldersForDropList(
            @RequestParam String term,
            @RequestParam Integer limit
    ) {
        final List<CmsFolder> results = cmsFoldersDAO.listFoldersForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsFolder cmsFolder : results) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", cmsFolder.getId());
            formDetailsJson.put("folderName", cmsFolder.getFolderName());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addFolder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addFolder(
            @ModelAttribute(value = "cmsFolder") CmsFolder cmsFolder,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "folderName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsFoldersDAO.add(cmsFolder);
                addJsonSuccess(responseDetailsJson);
            } catch (DuplicateKeyException e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameAlreadyExist"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteFolder(
            @ModelAttribute(value = "cmsFolder") CmsFolder cmsFolder,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsFoldersDAO.delete(cmsFolder);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
