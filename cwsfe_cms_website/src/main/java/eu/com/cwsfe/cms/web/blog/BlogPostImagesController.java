package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostImagesEntity;
import eu.com.cwsfe.cms.db.blog.BlogPostImagesRepository;
import eu.com.cwsfe.cms.web.images.ImageStorageService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class BlogPostImagesController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostImagesController.class);

    private final BlogPostImagesRepository blogPostImagesRepository;

    private final ImageStorageService imageStorageService;

    @Autowired
    public BlogPostImagesController(ImageStorageService imageStorageService, BlogPostImagesRepository blogPostImagesRepository) {
        this.imageStorageService = imageStorageService;
        this.blogPostImagesRepository = blogPostImagesRepository;
    }

    @RequestMapping(value = "/blogPosts/blogPostImagesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listBlogPostImages(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        WebRequest webRequest
    ) {
        Long blogPostId = null;
        try {
            blogPostId = Long.parseLong(webRequest.getParameter("blogPostId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Blog post id is not a number {}", webRequest.getParameter("blogPostId"));
        }
        List<BlogPostImagesEntity> dbList = blogPostImagesRepository.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, blogPostId);
        Integer dbListDisplayRecordsSize = blogPostImagesRepository.searchByAjaxCountWithoutContent(blogPostId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final BlogPostImagesEntity object = dbList.get(i);
            formDetailsJson.put("image", object.getId());
            formDetailsJson.put("title", object.getTitle());
            formDetailsJson.put("fileName", object.getFileName());
            formDetailsJson.put("url", object.getUrl());
            formDetailsJson.put("id", object.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", blogPostImagesRepository.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/addBlogPostImage", method = RequestMethod.POST)
    public ModelAndView addBlogPostImage(
        @ModelAttribute(value = "blogPostImage") BlogPostImagesEntity blogPostImage,
        BindingResult result, Locale locale
    ) {
        BufferedImage image;
//        try {
//            image = ImageIO.read(blogPostImage.getFile().getInputStream());
//            blogPostImage.setWidth(image.getWidth());
//            blogPostImage.setHeight(image.getHeight());
//        } catch (IOException e) {
//            LOGGER.error("Problem with reading image", e);
//        }
//        blogPostImage.setFileName(blogPostImage.getFile().getOriginalFilename());
//        blogPostImage.setFileSize(blogPostImage.getFile().getSize());
//        blogPostImage.setMimeType(blogPostImage.getFile().getContentType());
        blogPostImage.setCreated(Timestamp.from(new Date().toInstant()));
        blogPostImage.setLastModified(blogPostImage.getCreated());
        ValidationUtils.rejectIfEmpty(result, "title", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TitleMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        if (!result.hasErrors()) {
            blogPostImage.setId(blogPostImagesRepository.add(blogPostImage));
            blogPostImage.setUrl(imageStorageService.storeBlogImage(blogPostImage));
            blogPostImagesRepository.updateUrl(blogPostImage);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/blogPosts/" + blogPostImage.getBlogPostId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/blogPosts/deleteBlogPostImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteBlogPostImage(
        @ModelAttribute(value = "blogPostImage") BlogPostImagesEntity blogPostImage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("ImageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostImagesRepository.delete(blogPostImage);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
