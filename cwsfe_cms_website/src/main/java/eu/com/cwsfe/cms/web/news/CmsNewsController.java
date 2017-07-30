package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.news.CmsNewsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsI18NContentsEntity;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
import eu.com.cwsfe.cms.services.news.CmsFoldersService;
import eu.com.cwsfe.cms.services.news.CmsNewsI18nContentsService;
import eu.com.cwsfe.cms.services.news.CmsNewsService;
import eu.com.cwsfe.cms.services.news.NewsTypesService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class CmsNewsController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsController.class);

    private final CmsNewsService cmsNewsService;
    private final CmsNewsI18nContentsService cmsNewsI18nContentsService;
    private final CmsAuthorsService cmsAuthorsService;
    private final CmsFoldersService cmsFoldersService;
    private final NewsTypesService newsTypesService;
    private final CmsLanguagesService cmsLanguagesService;

    @Autowired
    public CmsNewsController(CmsLanguagesService cmsLanguagesService, CmsNewsService cmsNewsService, CmsAuthorsService cmsAuthorsService, CmsFoldersService cmsFoldersService, NewsTypesService newsTypesDAO, CmsNewsI18nContentsService cmsNewsI18nContentsService) {
        this.cmsLanguagesService = cmsLanguagesService;
        this.cmsNewsService = cmsNewsService;
        this.cmsAuthorsService = cmsAuthorsService;
        this.cmsFoldersService = cmsFoldersService;
        this.newsTypesService = newsTypesDAO;
        this.cmsNewsI18nContentsService = cmsNewsI18nContentsService;
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/news/News";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/news/News.js";
    }

    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        addCmsNewsManagementBreadCrumb(locale, breadcrumbs);
        return breadcrumbs;
    }

    private String setSingleNewsAdditionalJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/news/SingleNews.js";
    }

    private List<BreadcrumbDTO> getSingleNewsBreadcrumbs(Locale locale, Long id) {
        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
        addCmsNewsManagementBreadCrumb(locale, breadcrumbs);
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/news/" + id).build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentNews")));
        return breadcrumbs;
    }

    private void addCmsNewsManagementBreadCrumb(Locale locale, List<BreadcrumbDTO> breadcrumbs) {
        breadcrumbs.add(new BreadcrumbDTO(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/news").build().toUriString(),
            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CmsNewsManagement")));
    }

    @RequestMapping(value = "/newsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String list(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        @RequestParam(required = false) String searchNewsCode,
        WebRequest webRequest
    ) {
        Integer searchAuthorId = null;
        String searchAuthorIdText = webRequest.getParameter("searchAuthorId");
        if (searchAuthorIdText != null && !searchAuthorIdText.isEmpty()) {
            try {
                searchAuthorId = Integer.parseInt(searchAuthorIdText);
            } catch (NumberFormatException e) {
                LOGGER.error("Search author id is not a number: {}", searchAuthorIdText);
            }
        }
//        List<Object[]> dbList = cmsNewsService.searchByAjax(iDisplayStart, iDisplayLength, searchAuthorId, searchNewsCode);
//        Integer dbListDisplayRecordsSize = cmsNewsService.searchByAjaxCount(searchAuthorId, searchNewsCode);
        JSONObject responseDetailsJson = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < dbList.size(); i++) {
//            JSONObject formDetailsJson = new JSONObject();
//            formDetailsJson.put("#", iDisplayStart + i + 1);
//            final Object[] objects = dbList.get(i);
//            if (objects[1] == null || ((String) objects[1]).isEmpty()) {
//                formDetailsJson.put("author", "---");
//            } else {
//                formDetailsJson.put("author", objects[1]);
//            }
//            formDetailsJson.put("newsCode", objects[5]);
//            //todo reconsider time management
//            formDetailsJson.put("creationDate", ((java.sql.Date) objects[4]).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
//            formDetailsJson.put("id", objects[0]);
//            jsonArray.add(formDetailsJson);
//        }
//        responseDetailsJson.put("sEcho", sEcho);
//        responseDetailsJson.put("iTotalRecords", cmsNewsService.getTotalNumberNotDeleted());
//        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
//        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNews", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addNews(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "authorId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            //todo reconsider time management
//            cmsNews.setCreationDate(new Date().toInstant());
            cmsNewsService.add(cmsNews);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/updateNewsBasicInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNewsBasicInfo(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNewsService.updatePostBasicInfo(cmsNews);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNews", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteFolder(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNewsService.delete(cmsNews);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
    public String browseNews(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleNewsAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleNewsBreadcrumbs(locale, id));
        final CmsNewsEntity cmsNews = cmsNewsService.get(id);
        model.addAttribute("cmsLanguages", cmsLanguagesService.listAll());
        model.addAttribute("cmsNews", cmsNews);
        model.addAttribute("cmsAuthor", cmsAuthorsService.get(cmsNews.getAuthorId()));
        model.addAttribute("newsType", newsTypesService.get(cmsNews.getNewsTypeId()));
//        model.addAttribute("newsFolder", cmsFoldersService.get(cmsNews.getNewsFolderId()));
        return "cms/news/SingleNews";
    }

    @RequestMapping(value = "/news/{id}/{langId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getNewsI18n(ModelMap model, @PathVariable("id") Long newsId, @PathVariable("langId") Long langId, Locale locale) {
        BindingResult result = new BeanPropertyBindingResult(null, "getNewsI18n");
        if (newsId == null) {
            result.addError(new ObjectError("id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet")));
        }
        if (langId == null) {
            result.addError(new ObjectError("langId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet")));
        }
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            CmsNewsI18NContentsEntity cmsNewsI18nContent = cmsNewsI18nContentsService.getByLanguageForNews(newsId, langId);
            if (cmsNewsI18nContent == null) {
                cmsNewsI18nContent = new CmsNewsI18NContentsEntity();
                cmsNewsI18nContent.setNewsId(newsId);
                cmsNewsI18nContent.setLanguageId(langId);
                cmsNewsI18nContent.setNewsTitle("");
                cmsNewsI18nContent.setNewsShortcut("");
                cmsNewsI18nContent.setNewsDescription("");
//                cmsNewsI18nContent.setStatus(CmsNewsI18nContentStatus.HIDDEN);
            }
            addJsonSuccess(responseDetailsJson);
            JSONObject data = new JSONObject();
            data.put("newsTitle", cmsNewsI18nContent.getNewsTitle());
            data.put("newsShortcut", cmsNewsI18nContent.getNewsShortcut());
            data.put("newsDescription", cmsNewsI18nContent.getNewsDescription());
            data.put("status", cmsNewsI18nContent.getStatus());
            responseDetailsJson.put("data", data);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/addNewsI18nContent", method = RequestMethod.POST)
    public ModelAndView addNewsI18nContent(
        @ModelAttribute(value = "cmsNewsI18nContent") CmsNewsI18NContentsEntity cmsNewsI18nContent,
        ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        cmsNewsI18nContentsService.add(cmsNewsI18nContent);
        browseNews(model, locale, cmsNewsI18nContent.getNewsId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/news/" + cmsNewsI18nContent.getNewsId(), true, false, false));
        return modelAndView;
    }


    @RequestMapping(value = "/news/updateNewsI18nContent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNewsI18nContent(
        @ModelAttribute(value = "cmsNewsI18nContent") CmsNewsI18NContentsEntity cmsNewsI18nContent,
        BindingResult result, ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        ValidationUtils.rejectIfEmpty(result, "newsId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsTitle", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TitleMustBeSet"));
        cmsNewsI18nContent.setNewsTitle(cmsNewsI18nContent.getNewsTitle().trim());
        if (cmsNewsI18nContent.getNewsShortcut() != null) {
            cmsNewsI18nContent.setNewsShortcut(cmsNewsI18nContent.getNewsShortcut().trim());
        }
        if (cmsNewsI18nContent.getNewsDescription() != null) {
            cmsNewsI18nContent.setNewsDescription(cmsNewsI18nContent.getNewsDescription().trim());
        }
        CmsNewsI18NContentsEntity existingI18nContent;
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            try {
                existingI18nContent = cmsNewsI18nContentsService.getByLanguageForNews(cmsNewsI18nContent.getNewsId(), cmsNewsI18nContent.getLanguageId());
            } catch (EmptyResultDataAccessException e) {
                existingI18nContent = null;
            }
            try {
                if (existingI18nContent == null) {
                    cmsNewsI18nContentsService.add(cmsNewsI18nContent);
                } else {
                    cmsNewsI18nContent.setId(existingI18nContent.getId());
                    cmsNewsI18nContentsService.updateContentWithStatus(cmsNewsI18nContent);
                }
                addJsonSuccess(responseDetailsJson);
            } catch (Exception e) {
                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SavingFailed"));
            }
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
