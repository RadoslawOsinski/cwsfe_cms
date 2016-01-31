package eu.com.cwsfe.cms.web.blog;

import eu.com.cwsfe.cms.dao.BlogPostImagesDAO;
import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.web.images.ImageStorageService;
import eu.com.cwsfe.cms.web.mvc.JsonController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @Autowired
    private BlogPostImagesDAO blogPostImagesDAO;

    @Autowired
    ImageStorageService imageStorageService;

    @RequestMapping(value = "/blogPosts/blogPostImagesList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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
        List<BlogPostImage> dbList = blogPostImagesDAO.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, blogPostId);
        Integer dbListDisplayRecordsSize = blogPostImagesDAO.searchByAjaxCountWithoutContent(blogPostId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final BlogPostImage object = dbList.get(i);
            formDetailsJson.put("image", object.getId());
            formDetailsJson.put("title", object.getTitle());
            formDetailsJson.put("fileName", object.getFileName());
            formDetailsJson.put("url", object.getUrl());
            formDetailsJson.put("id", object.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", blogPostImagesDAO.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/blogPosts/addBlogPostImage", method = RequestMethod.POST)
    public ModelAndView addBlogPostImage(
            @ModelAttribute(value = "blogPostImage") BlogPostImage blogPostImage,
            BindingResult result, Locale locale
    ) {
        BufferedImage image;
        try {
            image = ImageIO.read(blogPostImage.getFile().getInputStream());
            blogPostImage.setWidth(image.getWidth());
            blogPostImage.setHeight(image.getHeight());
        } catch (IOException e) {
            LOGGER.error("Problem with reading image", e);
        }
        blogPostImage.setFileName(blogPostImage.getFile().getOriginalFilename());
        blogPostImage.setFileSize(blogPostImage.getFile().getSize());
        blogPostImage.setMimeType(blogPostImage.getFile().getContentType());
        blogPostImage.setCreated(new Date());
        blogPostImage.setLastModified(blogPostImage.getCreated());
        ValidationUtils.rejectIfEmpty(result, "title", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TitleMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "blogPostId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("BlogPostMustBeSet"));
        if (!result.hasErrors()) {
            blogPostImage.setId(blogPostImagesDAO.add(blogPostImage));
            blogPostImage.setUrl(imageStorageService.storeBlogImage(blogPostImage));
            blogPostImagesDAO.updateUrl(blogPostImage);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/blogPosts/" + blogPostImage.getBlogPostId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/blogPosts/deleteBlogPostImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteBlogPostImage(
            @ModelAttribute(value = "blogPostImage") BlogPostImage blogPostImage,
            BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("ImageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            blogPostImagesDAO.delete(blogPostImage);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
