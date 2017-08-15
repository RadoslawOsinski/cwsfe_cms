package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.news.CmsFoldersEntity;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.news.CmsFoldersService;
import eu.com.cwsfe.cms.web.mvc.BasicResponse;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public FoldersDTO listFolders(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho
    ) {
        final List<CmsFoldersEntity> cmsFolders = cmsFoldersService.listAjax(iDisplayStart, iDisplayLength);
        FoldersDTO foldersDTO = new FoldersDTO();
        for (int i = 0; i < cmsFolders.size(); i++) {
            FolderDTO folderDTO = new FolderDTO();
            folderDTO.setOrderNumber(iDisplayStart + i + 1);
            folderDTO.setFolderName(cmsFolders.get(i).getFolderName());
            folderDTO.setFolderOrderNumber(cmsFolders.get(i).getOrderNumber());
            folderDTO.setId(cmsFolders.get(i).getId());
            foldersDTO.getAaData().add(folderDTO);
        }
        foldersDTO.setsEcho(sEcho);
        final Long numberOfFolders = cmsFoldersService.countForAjax();
        foldersDTO.setiTotalRecords(numberOfFolders);
        foldersDTO.setiTotalDisplayRecords(numberOfFolders);
        return foldersDTO;
    }

    @RequestMapping(value = "/news/foldersDropList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FoldersDTO listFoldersForDropList(
        @RequestParam String term,
        @RequestParam Integer limit
    ) {
        final List<CmsFoldersEntity> results = cmsFoldersService.listFoldersForDropList(term, limit);
        FoldersDTO foldersDTO = new FoldersDTO();
        for (CmsFoldersEntity cmsFolder : results) {
            FolderDTO folderDTO = new FolderDTO();
            folderDTO.setId(cmsFolder.getId());
            folderDTO.setFolderName(cmsFolder.getFolderName());
            foldersDTO.getAaData().add(folderDTO);
        }
        return foldersDTO;
    }

    @RequestMapping(value = "/addFolder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addFolder(
        @ModelAttribute(value = "cmsFolder") CmsFoldersEntity cmsFolder,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "folderName", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            Optional<CmsFoldersEntity> existingFolderName = cmsFoldersService.getByFolderName(cmsFolder.getFolderName());
            if (!existingFolderName.isPresent()) {
                cmsFoldersService.add(cmsFolder);
                basicResponse = getSuccess();
            } else {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderNameAlreadyExist"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteFolder(
        @ModelAttribute(value = "cmsFolder") CmsFoldersEntity cmsFolder,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsFoldersService.delete(cmsFolder);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
