package eu.com.cwsfe.cms.controllers;

import eu.com.cwsfe.cms.dao.*;
import eu.com.cwsfe.cms.model.*;
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
import java.util.stream.Collectors;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class BlogPostsController extends JsonController {

    private static final Logger LOGGER = LogManager.getLogger(BlogPostsController.class);

    @Autowired
    private BlogKeywordsDAO blogKeywordsDAO;
    @Autowired
    private BlogPostKeywordsDAO blogPostKeywordsDAO;
    @Autowired
    private BlogPostI18nContentsDAO blogPostI18nContentsDAO;
    @Autowired
    private BlogPostsDAO blogPostsDAO;
    @Autowired
    private CmsAuthorsDAO cmsAuthorsDAO;
    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    @RequestMapping(value = "/blogPosts", method = RequestMethod.GET)
    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
        return "cms/blog/Posts";
    }

    private String getPageJS(String contextPath) {
        return contextPath + "/resources-cwsfe-cms/js/cms/blog/Posts.js";
    }

    private List<Breadcrumb> getBreadcrumbs(Locale locale) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostManagement")));
        return breadcrumbs;
    }

    private List<Breadcrumb> getSingleBlogPostsBreadcrumbs(Locale locale, Long id) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>(1);
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts").build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostsManagement")));
        breadcrumbs.add(new Breadcrumb(
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts/" + id).build().toUriString(),
                ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentBlogPost")));
        return breadcrumbs;
    }

    @RequestMapping(value = "/blogPostsList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String listBlogPosts(
            @RequestParam int iDisplayStart,
            @RequestParam int iDisplayLength,
            @RequestParam String sEcho,
            @RequestParam(required = false) String searchPostTextCode,
            WebRequest webRequest
    ) {
        Integer searchAuthorId = null;
        try {
            searchAuthorId = Integer.parseInt(webRequest.getParameter("searchAuthorId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Search author id is not a number: " + webRequest.getParameter("searchAuthorId"));
        }
        List<Object[]> dbList = blogPostsDAO.searchByAjax(iDisplayStart, iDisplayLength, searchAuthorId, searchPostTextCode);
        Integer dbListDisplayRecordsSize = blogPostsDAO.searchByAjaxCount(searchAuthorId, searchPostTextCode);
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
            formDetailsJson.put("postTextCode", objects[2]);
            //todo reconsider time management
            formDetailsJson.put("postCreationDate", ((java.sql.Date) objects[3]).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            formDetailsJson.put("id", objects[0]);
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", blogPostsDAO.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/addBlogPost", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String addBlogPost(
            @ModelAttribute(value = "blogPost") BlogPost blogPost,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "postAuthorId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "postTextCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PostTextCodeMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            //todo reconsider time management
            blogPost.setPostCreationDate(new Date());
            blogPost.setStatus("H");
            blogPostsDAO.add(blogPost);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/deleteBlogPost", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String delete(
            @ModelAttribute(value = "blogPost") BlogPost blogPost,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostsDAO.delete(blogPost);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/{id}", method = RequestMethod.GET)
    public String browseBlogPost(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        model.addAttribute("mainJavaScript", httpServletRequest.getContextPath() + "/resources-cwsfe-cms/js/cms/blog/SinglePost.js");
        model.addAttribute("breadcrumbs", getSingleBlogPostsBreadcrumbs(locale, id));
        BlogPost blogPost = blogPostsDAO.get(id);
        final List<Language> languages = cmsLanguagesDAO.listAll();
        model.addAttribute("cmsLanguages", languages);
        Map<String, BlogPostI18nContent> blogPostI18nContents = new HashMap<>(languages.size());
        for (Language lang : languages) {
            blogPostI18nContents.put(lang.getCode(), blogPostI18nContentsDAO.getByLanguageForPost(blogPost.getId(), lang.getId()));
        }
        blogPost.setBlogPostI18nContent(blogPostI18nContents);
        blogPost.setBlogKeywords(blogPostKeywordsDAO.listForPost(blogPost.getId()));
        List<Long> blogPostSelectedKeywords = new ArrayList<>(5);
        blogPostSelectedKeywords.addAll(blogPost.getBlogKeywords().stream().map(BlogKeyword::getId).collect(Collectors.toList()));
        model.addAttribute("blogPostSelectedKeywords", blogPostSelectedKeywords);
        model.addAttribute("blogKeywords", blogKeywordsDAO.list());
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("cmsAuthor", cmsAuthorsDAO.get(blogPost.getPostAuthorId()));
        return "cms/blog/SingleBlogPost";
    }

    @RequestMapping(value = "/blogPosts/updatePostBasicInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8;pageEncoding=UTF-8")
    @ResponseBody
    public String updatePostBasicInfo(
            @ModelAttribute(value = "blogPost") BlogPost blogPost,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "postTextCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PostTextCodeMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostsDAO.updatePostBasicInfo(blogPost);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "addBlogPostsI18nContent", method = RequestMethod.POST)
    public ModelAndView addBlogPostsI18nContent(
            @ModelAttribute(value = "blogPostI18nContent") BlogPostI18nContent blogPostI18nContent,
            ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        blogPostI18nContentsDAO.add(blogPostI18nContent);
        browseBlogPost(model, locale, blogPostI18nContent.getPostId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/blogPosts/" + blogPostI18nContent.getPostId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/blogPosts/updateBlogPostI18nContent", method = RequestMethod.POST)
    public ModelAndView updateBlogPostI18nContent(
            @ModelAttribute(value = "BlogPostI18nContent") BlogPostI18nContent blogPostI18nContent,
            ModelMap model, Locale locale, HttpServletRequest httpServletRequest
    ) {
        blogPostI18nContent.setPostTitle(blogPostI18nContent.getPostTitle().trim());
        blogPostI18nContent.setPostShortcut(blogPostI18nContent.getPostShortcut().trim());
        blogPostI18nContent.setPostDescription(blogPostI18nContent.getPostDescription().trim());
        blogPostI18nContentsDAO.updateContentWithStatus(blogPostI18nContent);
        browseBlogPost(model, locale, blogPostI18nContent.getPostId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/blogPosts/" + blogPostI18nContent.getPostId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/postCategoriesUpdate", method = RequestMethod.POST)
    public ModelAndView postCategoriesUpdate(
            @ModelAttribute(value = "blogPost") BlogPost blogPost,
            ModelMap model, Locale locale,
            WebRequest webRequest, HttpServletRequest httpServletRequest
    ) {
        String[] postCategoriesStrings = webRequest.getParameterValues("postCategories");
        //todo add transactions!
        blogPostKeywordsDAO.deleteForPost(blogPost.getId());
        if (postCategoriesStrings != null) {
            for (String keywordIdString : postCategoriesStrings) {
                blogPostKeywordsDAO.add(new BlogPostKeyword(
                        blogPost.getId(),
                        Long.parseLong(keywordIdString)
                ));
            }
        }
        /////////// end transaction
        browseBlogPost(model, locale, blogPost.getId(), httpServletRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/blogPosts/" + blogPost.getId(), true, false, false));
        return modelAndView;
    }

}
