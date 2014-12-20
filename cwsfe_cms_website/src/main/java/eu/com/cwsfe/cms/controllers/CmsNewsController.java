package eu.com.cwsfe.cms.controllers;

import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.model.Breadcrumb;
import eu.com.cwsfe.cms.model.CmsNews;
import eu.com.cwsfe.cms.model.CmsNewsI18nContent;
import eu.com.cwsfe.cms.model.Language;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class CmsNewsController extends JsonController {

    private static final Logger LOGGER = LogManager.getLogger(CmsNewsController.class);

    @Autowired
    private CmsNewsDAO cmsNewsDAO;
    @Autowired
    private CmsNewsI18nContentsDAO cmsNewsI18nContentsDAO;
    @Autowired
    private CmsAuthorsDAO cmsAuthorsDAO;
    @Autowired
    private CmsFoldersDAO cmsFoldersDAO;
    @Autowired
    private NewsTypesDAO newsTypesDAO;
    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/news/News";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/news/News.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        addCmsNewsManagementBreadCrumb(locale, breadcrumbs);
        return breadcrumbs;
    }

    private String setSingleNewsAdditionalJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/news/SingleNews.js";
    }

    private List<Breadcrumb> getSingleNewsBreadcrumbs(Locale locale, Long id) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        addCmsNewsManagementBreadCrumb(locale, breadcrumbs);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/news/" + id).build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentNews")));
        return breadcrumbs;
    }

    private void addCmsNewsManagementBreadCrumb(Locale locale, List<Breadcrumb> breadcrumbs) {
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/news").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CmsNewsManagement")));
    }

    @RequestMapping(value = "/newsList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listBlogKeywords(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho,
            @RequestParam(required = false) String searchNewsCode,
            WebRequest webRequest
    ) {
        Integer searchAuthorId = null;
        try {
            searchAuthorId = Integer.parseInt(webRequest.getParameter("searchAuthorId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Search author id is not a number: " + webRequest.getParameter("searchAuthorId"), e);
        }
        List<Object[]> dbList = cmsNewsDAO.searchByAjax(iDisplayStart, iDisplayLength, searchAuthorId, searchNewsCode);
        Integer dbListDisplayRecordsSize = cmsNewsDAO.searchByAjaxCount(searchAuthorId, searchNewsCode);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final Object[] objects = dbList.get(i);
            if (objects[1] == null || ((String) objects[1]).isEmpty()) {
                formDetailsJson.put("author", "---");
            } else {
                formDetailsJson.put("author", objects[1]);
            }
            formDetailsJson.put("newsCode", objects[5]);
            //todo reconsider time management
            formDetailsJson.put("creationDate", ((java.sql.Date) objects[4]).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            formDetailsJson.put("id", objects[0]);
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", cmsNewsDAO.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addNews", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addNews(
            @ModelAttribute(value = "cmsNews") CmsNews cmsNews,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "authorId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNews.setCreationDate(new Date());
            cmsNewsDAO.add(cmsNews);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/updateNewsBasicInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String updateNewsBasicInfo(
            @ModelAttribute(value = "cmsNews") CmsNews cmsNews,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNewsDAO.updatePostBasicInfo(cmsNews);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteNews", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String deleteFolder(
            @ModelAttribute(value = "cmsNews") CmsNews cmsNews,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNewsDAO.delete(cmsNews);
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
        final CmsNews cmsNews = cmsNewsDAO.get(id);
        final List<Language> languages = cmsLanguagesDAO.listAll();
        model.addAttribute("cmsLanguages", languages);
        Map<String, CmsNewsI18nContent> cmsNewsI18nContents = new HashMap<>(languages.size());
        for (Language lang : languages) {
            cmsNewsI18nContents.put(lang.getCode(), cmsNewsI18nContentsDAO.getByLanguageForNews(cmsNews.getId(), lang.getId()));
        }
        cmsNews.setCmsNewsI18nContents(cmsNewsI18nContents);
        model.addAttribute("cmsNews", cmsNews);
        model.addAttribute("cmsAuthor", cmsAuthorsDAO.get(cmsNews.getAuthorId()));
        model.addAttribute("newsType", newsTypesDAO.get(cmsNews.getNewsTypeId()));
        model.addAttribute("newsFolder", cmsFoldersDAO.get(cmsNews.getNewsFolderId()));
        return "cms/news/SingleNews";
    }

    @RequestMapping(value = "/news/addNewsI18nContent", method = RequestMethod.POST)
    public ModelAndView addNewsI18nContent(
            @ModelAttribute(value = "cmsNewsI18nContent") CmsNewsI18nContent cmsNewsI18nContent,
            ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        cmsNewsI18nContentsDAO.add(cmsNewsI18nContent);
        browseNews(model, locale, cmsNewsI18nContent.getNewsId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/news/" + cmsNewsI18nContent.getNewsId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/news/updateNewsI18nContent", method = RequestMethod.POST)
    public ModelAndView updateNewsI18nContent(
            @ModelAttribute(value = "cmsNewsI18nContent") CmsNewsI18nContent cmsNewsI18nContent,
            ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        cmsNewsI18nContent.setNewsTitle(cmsNewsI18nContent.getNewsTitle().trim());
        cmsNewsI18nContent.setNewsShortcut(cmsNewsI18nContent.getNewsShortcut().trim());
        cmsNewsI18nContent.setNewsDescription(cmsNewsI18nContent.getNewsDescription().trim());
        cmsNewsI18nContentsDAO.updateContentWithStatus(cmsNewsI18nContent);
        browseNews(model, locale, cmsNewsI18nContent.getNewsId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/news/" + cmsNewsI18nContent.getNewsId(), true, false, false));
        return modelAndView;
    }

}