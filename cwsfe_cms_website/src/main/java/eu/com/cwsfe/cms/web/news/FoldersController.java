package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.news.CmsFoldersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.news.CmsFoldersService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
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

    private final CmsFoldersService cmsFoldersService;

    @Autowired
    public FoldersController(CmsFoldersService cmsFoldersService) {
        this.cmsFoldersService = cmsFoldersService;
    }

    @RequestMapping(value = "/folders", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/folders/Folders";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/folders/Folders.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/folders").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FoldersManagement")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/foldersList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listFolders(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsFoldersEntity> cmsFolders = cmsFoldersService.listAjax(iDisplayStart, iDisplayLength);
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
        final int numberOfFolders = cmsFoldersService.countForAjax();
        responseDetailsJson.put("iTotalRecords", numberOfFolders);
        responseDetailsJson.put("iTotalDisplayRecords", numberOfFolders);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/foldersDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listFoldersForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsFoldersEntity> results = cmsFoldersService.listFoldersForDropList(term, limit);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (CmsFoldersEntity cmsFolder : results) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("id", cmsFolder.getId());
            formDetailsJson.put("folderName", cmsFolder.getFolderName());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("data", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addFolder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addFolder(
        @ModelAttribute(value = "cmsFolder") CmsFoldersEntity cmsFolder,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "folderName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                cmsFoldersService.add(cmsFolder);
                addJsonSuccess(responseDetailsJson);
            } catch (DuplicateKeyException e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameAlreadyExist"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteFolder(
        @ModelAttribute(value = "cmsFolder") CmsFoldersEntity cmsFolder,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsFoldersService.delete(cmsFolder);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
