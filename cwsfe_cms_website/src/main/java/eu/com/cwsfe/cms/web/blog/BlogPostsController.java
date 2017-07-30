//package eu.com.cwsfe.cms.web.blog;
//
//import eu.com.cwsfe.cms.db.author.CmsAuthorsService;
//import eu.com.cwsfe.cms.db.blog.*;
//import eu.com.cwsfe.cms.db.i18n.CmsLanguagesService;
//import eu.com.cwsfe.cms.services.breadcrumbs.BreadcrumbDTO;
//import eu.com.cwsfe.cms.web.mvc.JsonController;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BeanPropertyBindingResult;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.ResourceBundle;
//
///**
// * @author Radoslaw Osinski
// */
//@Controller
//public class BlogPostsController extends JsonController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostsController.class);
//
////    private final BlogPostKeywordsService blogPostKeywordsService;
////    private final BlogPostI18nContentsService blogPostI18nContentsService;
//    private final BlogPostsService blogPostsService;
//    private final CmsAuthorsService cmsAuthorsService;
//    private final CmsLanguagesService cmsLanguagesService;
//
//    @Autowired
//    public BlogPostsController(CmsAuthorsService cmsAuthorsService, BlogPostsService blogPostsService, CmsLanguagesService cmsLanguagesService, /*BlogPostI18nContentsService blogPostI18nContentsService, BlogPostKeywordsService blogPostKeywordsService*/) {
//        this.cmsAuthorsService = cmsAuthorsService;
//        this.blogPostsService = blogPostsService;
//        this.cmsLanguagesService = cmsLanguagesService;
////        this.blogPostI18nContentsService = blogPostI18nContentsService;
////        this.blogPostKeywordsService = blogPostKeywordsService;
//    }
//
//    @RequestMapping(value = "/blogPosts", method = RequestMethod.GET)
//    public String defaultView(ModelMap model, Locale locale, HttpServletRequest httpServletRequest) {
//        model.addAttribute("mainJavaScript", getPageJS(httpServletRequest.getContextPath()));
//        model.addAttribute("breadcrumbs", getBreadcrumbs(locale));
//        return "cms/blog/Posts";
//    }
//
//    private String getPageJS(String contextPath) {
//        return contextPath + "/resources-cwsfe-cms/js/cms/blog/Posts.js";
//    }
//
//    private List<BreadcrumbDTO> getBreadcrumbs(Locale locale) {
//        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
//        breadcrumbs.add(new BreadcrumbDTO(
//            ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts").build().toUriString(),
//            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostManagement")));
//        return breadcrumbs;
//    }
//
//    private List<BreadcrumbDTO> getSingleBlogPostsBreadcrumbs(Locale locale, Long id) {
//        List<BreadcrumbDTO> breadcrumbs = new ArrayList<>(1);
//        breadcrumbs.add(new BreadcrumbDTO(
//            ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts").build().toUriString(),
//            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostsManagement")));
//        breadcrumbs.add(new BreadcrumbDTO(
//            ServletUriComponentsBuilder.fromCurrentContextPath().path("/blogPosts/" + id).build().toUriString(),
//            ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CurrentBlogPost")));
//        return breadcrumbs;
//    }
//
//    @RequestMapping(value = "/blogPostsList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String listBlogPosts(
//        @RequestParam int iDisplayStart,
//        @RequestParam int iDisplayLength,
//        @RequestParam String sEcho,
//        @RequestParam(required = false) String searchPostTextCode,
//        WebRequest webRequest
//    ) {
//        Integer searchAuthorId = null;
//        String searchAuthorIdText = webRequest.getParameter("searchAuthorId");
//        if (searchAuthorIdText != null && !searchAuthorIdText.isEmpty()) {
//            try {
//                searchAuthorId = Integer.parseInt(searchAuthorIdText);
//            } catch (NumberFormatException e) {
//                LOGGER.error("Search author id is not a number: {}", searchAuthorIdText);
//            }
//        }
////        List<Object[]> dbList = blogPostsService.searchByAjax(iDisplayStart, iDisplayLength, searchAuthorId, searchPostTextCode);
////        Integer dbListDisplayRecordsSize = blogPostsService.searchByAjaxCount(searchAuthorId, searchPostTextCode);
//        JSONObject responseDetailsJson = new JSONObject();
////        JSONArray jsonArray = new JSONArray();
////        for (int i = 0; i < dbList.size(); i++) {
////            JSONObject formDetailsJson = new JSONObject();
////            formDetailsJson.put("#", iDisplayStart + i + 1);
////            final Object[] objects = dbList.get(i);
////            if (objects[1] == null || ((String) objects[1]).isEmpty()) {
////                formDetailsJson.put("author", "---");
////            } else {
////                formDetailsJson.put("author", objects[1]);
////            }
////            formDetailsJson.put("postTextCode", objects[2]);
////            todo reconsider time management
////            formDetailsJson.put("postCreationDate", ((java.sql.Date) objects[3]).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
////            formDetailsJson.put("id", objects[0]);
////            jsonArray.add(formDetailsJson);
////        }
//        responseDetailsJson.put("sEcho", sEcho);
////        responseDetailsJson.put("iTotalRecords", blogPostsService.getTotalNumberNotDeleted());
////        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
////        responseDetailsJson.put("aaData", jsonArray);
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/blogPostKeywordAssignment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String listBlogPostKeywordAssignment(
//        @RequestParam long blogPostId,
//        WebRequest webRequest
//    ) {
////        List<BlogKeywordAssignment> blogKeywordAssignments = blogPostKeywordsService.listValuesForPost(blogPostId);
//        JSONObject responseDetailsJson = new JSONObject();
////        JSONArray jsonArray = new JSONArray();
////        for (BlogKeywordAssignment blogKeywordAssignment : blogKeywordAssignments) {
////            JSONObject formDetailsJson = new JSONObject();
////            formDetailsJson.put("id", blogKeywordAssignment.getId());
////            formDetailsJson.put("keywordName", blogKeywordAssignment.getKeywordName());
////            formDetailsJson.put("status", blogKeywordAssignment.getStatus());
////            formDetailsJson.put("assigned", blogKeywordAssignment.isAssigned());
////            jsonArray.add(formDetailsJson);
////        }
////        responseDetailsJson.put("aaData", jsonArray);
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/addBlogPost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String addBlogPost(
//        @ModelAttribute(value = "blogPost") BlogPostsEntity blogPost,
//        BindingResult result, Locale locale
//    ) {
//        ValidationUtils.rejectIfEmpty(result, "postAuthorId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("AuthorMustBeSet"));
//        ValidationUtils.rejectIfEmpty(result, "postTextCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PostTextCodeMustBeSet"));
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
//            //todo reconsider time management
////            blogPost.setPostCreationDate(new Date());
////            blogPost.setStatus(BlogPostStatus.HIDDEN);
////            blogPostsService.add(blogPost);
//            addJsonSuccess(responseDetailsJson);
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/deleteBlogPost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String delete(
//        @ModelAttribute(value = "blogPost") BlogPostsEntity blogPost,
//        BindingResult result, Locale locale
//    ) {
//        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
////            blogPostsService.delete(blogPost);
//            addJsonSuccess(responseDetailsJson);
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/blogPosts/{id}", method = RequestMethod.GET)
//    public String browseBlogPost(ModelMap model, Locale locale, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
//        model.addAttribute("mainJavaScript", httpServletRequest.getContextPath() + "/resources-cwsfe-cms/js/cms/blog/SinglePost.js");
//        model.addAttribute("breadcrumbs", getSingleBlogPostsBreadcrumbs(locale, id));
////        BlogPostsEntity blogPost = blogPostsService.get(id);
////        model.addAttribute("cmsLanguages", cmsLanguagesService.listAll());
////        model.addAttribute("blogPost", blogPost);
////        model.addAttribute("cmsAuthor", cmsAuthorsService.get(blogPost.getPostAuthorId()));
//        return "cms/blog/SingleBlogPost";
//    }
//
//    @RequestMapping(value = "/blogPosts/{id}/{langId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String getBlogPostI18n(ModelMap model, @PathVariable("id") Long blogPostId, @PathVariable("langId") Long langId, Locale locale) {
//        BindingResult result = new BeanPropertyBindingResult(null, "getNewsI18n");
//        if (blogPostId == null) {
//            result.addError(new ObjectError("id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet")));
//        }
//        if (langId == null) {
//            result.addError(new ObjectError("langId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet")));
//        }
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
//            BlogPostI18NContentsEntity cmsBlogPostI18nContent;
////            try {
////                cmsBlogPostI18nContent = blogPostI18nContentsService.getByLanguageForPost(blogPostId, langId);
////            } catch (EmptyResultDataAccessException ignored) {
////                cmsBlogPostI18nContent = new BlogPostI18nContent();
////                cmsBlogPostI18nContent.setPostId(blogPostId);
////                cmsBlogPostI18nContent.setLanguageId(langId);
////                cmsBlogPostI18nContent.setPostTitle("");
////                cmsBlogPostI18nContent.setPostShortcut("");
////                cmsBlogPostI18nContent.setPostDescription("");
////                cmsBlogPostI18nContent.setStatus(BlogPostI18nContentStatus.HIDDEN);
////            }
//            addJsonSuccess(responseDetailsJson);
//            JSONObject data = new JSONObject();
//            data.put("postTitle", cmsBlogPostI18nContent.getPostTitle());
//            data.put("postShortcut", cmsBlogPostI18nContent.getPostShortcut());
//            data.put("postDescription", cmsBlogPostI18nContent.getPostDescription());
//            data.put("status", cmsBlogPostI18nContent.getStatus());
//            responseDetailsJson.put("data", data);
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/blogPosts/updatePostBasicInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String updatePostBasicInfo(
//        @ModelAttribute(value = "blogPost") BlogPostsEntity blogPost,
//        BindingResult result, Locale locale
//    ) {
//        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
//        ValidationUtils.rejectIfEmpty(result, "postTextCode", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("PostTextCodeMustBeSet"));
//        ValidationUtils.rejectIfEmpty(result, "status", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("StatusMustBeSet"));
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
////            blogPostsService.updatePostBasicInfo(blogPost);
//            addJsonSuccess(responseDetailsJson);
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/blogPosts/updateBlogPostI18nContent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String updateBlogPostI18nContent(
//        @ModelAttribute(value = "BlogPostI18nContent") BlogPostI18NContentsEntity blogPostI18nContent,
//        BindingResult result, ModelMap model, Locale locale, HttpServletRequest httpServletRequest
//    ) {
//        ValidationUtils.rejectIfEmpty(result, "postId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
//        ValidationUtils.rejectIfEmpty(result, "languageId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("LanguageMustBeSet"));
//        ValidationUtils.rejectIfEmpty(result, "postTitle", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TitleMustBeSet"));
//        blogPostI18nContent.setPostTitle(blogPostI18nContent.getPostTitle().trim());
//        if (blogPostI18nContent.getPostShortcut() != null) {
//            blogPostI18nContent.setPostShortcut(blogPostI18nContent.getPostShortcut().trim());
//        }
//        if (blogPostI18nContent.getPostDescription() != null) {
//            blogPostI18nContent.setPostDescription(blogPostI18nContent.getPostDescription().trim());
//        }
//        BlogPostI18NContentsEntity existingI18nContent;
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
////            try {
////                existingI18nContent = blogPostI18nContentsService.getByLanguageForPost(blogPostI18nContent.getPostId(), blogPostI18nContent.getLanguageId());
////            } catch (EmptyResultDataAccessException e) {
////                existingI18nContent = null;
////            }
////            try {
////                if (existingI18nContent == null) {
////                    blogPostI18nContentsService.add(blogPostI18nContent);
////                } else {
////                    blogPostI18nContent.setId(existingI18nContent.getId());
////                    blogPostI18nContentsService.updateContentWithStatus(blogPostI18nContent);
////                }
////                addJsonSuccess(responseDetailsJson);
////            } catch (Exception e) {
////                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SavingFailed"));
////            }
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//    @RequestMapping(value = "/postCategoryUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String postCategoriesUpdate(
////        @ModelAttribute(value = "blogKeywordAssignment") BlogKeywordAssignment blogKeywordAssignment,
//        @RequestParam Long postId,
//        ModelMap model, Locale locale, BindingResult result,
//        WebRequest webRequest, HttpServletRequest httpServletRequest
//    ) {
//        if (postId == null) {
//            result.addError(new ObjectError("postId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet")));
//        }
//        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogKeywordIsRequired"));
//        ValidationUtils.rejectIfEmpty(result, "assigned", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogKeywordAssignmentIsRequired"));
//        JSONObject responseDetailsJson = new JSONObject();
//        if (!result.hasErrors()) {
//            BlogPostKeywordsEntity blogPostKeyword;
////            try {
////                blogPostKeyword = blogPostKeywordsService.get(postId, blogKeywordAssignment.getId());
////            } catch (EmptyResultDataAccessException e) {
////                blogPostKeyword = null;
////            }
////            try {
////                if (blogPostKeyword == null && blogKeywordAssignment.isAssigned()) {
////                    blogPostKeywordsService.add(new BlogPostKeyword(postId, blogKeywordAssignment.getId()));
////                } else if (blogPostKeyword != null && !blogKeywordAssignment.isAssigned()) {
////                    blogPostKeywordsService.delete(postId, blogKeywordAssignment.getId());
////                }
////                addJsonSuccess(responseDetailsJson);
////            } catch (Exception e) {
////                addErrorMessage(responseDetailsJson, ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("SavingFailed"));
////            }
//        } else {
//            prepareErrorResponse(result, responseDetailsJson);
//        }
//        return responseDetailsJson.toString();
//    }
//
//}
