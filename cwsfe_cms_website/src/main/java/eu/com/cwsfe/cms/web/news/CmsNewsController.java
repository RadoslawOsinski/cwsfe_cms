package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import eu.com.cwsfe.cms.db.news.*;
import eu.com.cwsfe.cms.services.author.CmsAuthorDTO;
import eu.com.cwsfe.cms.services.author.CmsAuthorsService;
import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
import eu.com.cwsfe.cms.services.i18n.CmsLanguagesService;
import eu.com.cwsfe.cms.services.news.CmsFoldersService;
import eu.com.cwsfe.cms.services.news.CmsNewsI18nContentsService;
import eu.com.cwsfe.cms.services.news.CmsNewsService;
import eu.com.cwsfe.cms.services.news.NewsTypesService;
import eu.com.cwsfe.cms.web.mvc.BasicResponse;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    public NewsesDTO list(
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
        List<SearchedNewsDTO> dbList = cmsNewsService.searchByAjax(iDisplayStart, iDisplayLength, searchAuthorId, searchNewsCode);
        Long dbListDisplayRecordsSize = cmsNewsService.searchByAjaxCount(searchAuthorId, searchNewsCode);
        NewsesDTO newsesDTO = new NewsesDTO();
        for (int i = 0; i < dbList.size(); i++) {
            final SearchedNewsDTO objects = dbList.get(i);
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setOrderNumber(iDisplayStart + i + 1);
            newsDTO.setAuthor((objects.getAuthor() == null || objects.getAuthor().isEmpty()) ? "---" : objects.getAuthor());
            newsDTO.setNewsCode(objects.getNewsCode());
            newsDTO.setCreationDate(objects.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            newsDTO.setId(objects.getId());
            newsesDTO.getAaData().add(newsDTO);
        }
        newsesDTO.setsEcho(sEcho);
        newsesDTO.setiTotalRecords(cmsNewsService.getTotalNumberNotDeleted());
        newsesDTO.setiTotalDisplayRecords(dbListDisplayRecordsSize);
        return newsesDTO;
    }

    @RequestMapping(value = "/addNews", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse addNews(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "authorId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsNews.setCreationDate(ZonedDateTime.now());
            cmsNewsService.add(cmsNews);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/news/updateNewsBasicInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse updateNewsBasicInfo(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "newsTypeId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsTypeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsFolderId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("FolderMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsNewsService.updatePostBasicInfo(cmsNews);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/deleteNews", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BasicResponse deleteFolder(
        @ModelAttribute(value = "cmsNews") CmsNewsEntity cmsNews,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet"));
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            cmsNewsService.delete(cmsNews);
            basicResponse = getSuccess();
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
    public String browseNews(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", setSingleNewsAdditionalJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getSingleNewsBreadcrumbs(locale, id));
        final Optional<CmsNewsEntity> cmsNews = cmsNewsService.get(id);
        model.addAttribute("cmsLanguages", cmsLanguagesService.listAll());
        if (cmsNews.isPresent()) {
            model.addAttribute("cmsNews", cmsNews.get());
            Optional<CmsAuthorDTO> authorDTO = cmsAuthorsService.get(cmsNews.get().getAuthorId());
            authorDTO.ifPresent(cmsAuthorDTO -> model.addAttribute("cmsAuthor", cmsAuthorDTO));
            Optional<CmsNewsTypesEntity> newsType = newsTypesService.get(cmsNews.get().getNewsTypeId());
            newsType.ifPresent(cmsNewsType -> model.addAttribute("newsType", cmsNewsType));
            Optional<CmsFoldersEntity> newsFolder = cmsFoldersService.get(cmsNews.get().getNewsFolderId());
            newsFolder.ifPresent(cmsFoldersEntity -> model.addAttribute("newsFolder", cmsFoldersEntity));
        }
        return "cms/news/SingleNews";
    }

    @RequestMapping(value = "/news/{id}/{langId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public NewsI18nDTO getNewsI18n(ModelMap model, @PathVariable("id") Long newsId, @PathVariable("langId") Long langId, Locale locale) {
        BindingResult result = new BeanPropertyBindingResult(null, "getNewsI18n");
        if (newsId == null) {
            result.addError(new ObjectError("id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("NewsMustBeSet")));
        }
        if (langId == null) {
            result.addError(new ObjectError("langId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet")));
        }
        NewsI18nDTO newsI18nDTO = new NewsI18nDTO();
        if (!result.hasErrors()) {
            Optional<CmsNewsI18NContentsEntity> cmsNewsI18nContent = cmsNewsI18nContentsService.getByLanguageForNews(newsId, langId);
            if (!cmsNewsI18nContent.isPresent()) {
                CmsNewsI18NContentsEntity newCmsNewsI18nContent = new CmsNewsI18NContentsEntity();
                newCmsNewsI18nContent.setNewsId(newsId);
                newCmsNewsI18nContent.setLanguageId(langId);
                newCmsNewsI18nContent.setNewsTitle("");
                newCmsNewsI18nContent.setNewsShortcut("");
                newCmsNewsI18nContent.setNewsDescription("");
                newCmsNewsI18nContent.setStatus(PublishedHiddenStatus.HIDDEN.name());
                cmsNewsI18nContent = Optional.of(newCmsNewsI18nContent);
            }
            newsI18nDTO.setNewsTitle(cmsNewsI18nContent.get().getNewsTitle());
            newsI18nDTO.setNewsShortcut(cmsNewsI18nContent.get().getNewsShortcut());
            newsI18nDTO.setNewsDescription(cmsNewsI18nContent.get().getNewsDescription());
            newsI18nDTO.setStatus(cmsNewsI18nContent.get().getStatus());
        } else {
            newsI18nDTO.setStatus(BasicResponse.JSON_STATUS_FAIL);
            for (int i = 0; i < result.getAllErrors().size(); i++) {
                newsI18nDTO.getErrorMessages().add(result.getAllErrors().get(i).getCode());
            }
        }
        return newsI18nDTO;
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
    public BasicResponse updateNewsI18nContent(
        @ModelAttribute(value = "cmsNewsI18nContent") CmsNewsI18NContentsEntity cmsNewsI18nContent,
        BindingResult result, Locale locale
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
        Optional<CmsNewsI18NContentsEntity> existingI18nContent;
        BasicResponse basicResponse;
        if (!result.hasErrors()) {
            existingI18nContent = cmsNewsI18nContentsService.getByLanguageForNews(cmsNewsI18nContent.getNewsId(), cmsNewsI18nContent.getLanguageId());
            try {
                if (!existingI18nContent.isPresent()) {
                    cmsNewsI18nContentsService.add(cmsNewsI18nContent);
                } else {
                    cmsNewsI18nContent.setId(existingI18nContent.get().getId());
                    cmsNewsI18nContentsService.updateContentWithStatus(cmsNewsI18nContent);
                }
                basicResponse = getSuccess();
            } catch (Exception e) {
                basicResponse = getErrorMessage(ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SavingFailed"));
            }
        } else {
            basicResponse = prepareErrorResponse(result);
        }
        return basicResponse;
    }

}
