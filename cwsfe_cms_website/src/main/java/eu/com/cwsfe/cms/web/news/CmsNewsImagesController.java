package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.news.CmsNewsImage;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.services.news.CmsNewsImagesService;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Radoslaw Osinski
 */
@Controller
public class CmsNewsImagesController extends JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesController.class);

    private final CmsNewsImagesService cmsNewsImagesService;

    private final ImageStorageService imageStorageService;

    @Autowired
    public CmsNewsImagesController(ImageStorageService imageStorageService, CmsNewsImagesService cmsNewsImagesService) {
        this.imageStorageService = imageStorageService;
        this.cmsNewsImagesService = cmsNewsImagesService;
    }

    @RequestMapping(value = "/news/cmsNewsImagesList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String list(
        @RequestParam int iDisplayStart,
        @RequestParam int iDisplayLength,
        @RequestParam String sEcho,
        WebRequest webRequest
    ) {
        Long newsId = null;
        try {
            newsId = Long.parseLong(webRequest.getParameter("cmsNewsId"));
        } catch (NumberFormatException e) {
            LOGGER.error("Cms news id is not a number: {}", webRequest.getParameter("cmsNewsId"));
        }
        List<CmsNewsImagesEntity> dbList = cmsNewsImagesService.searchByAjaxWithoutContent(iDisplayStart, iDisplayLength, newsId);
        Long dbListDisplayRecordsSize = cmsNewsImagesService.searchByAjaxCountWithoutContent(newsId);
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dbList.size(); i++) {
            JSONObject formDetailsJson = new JSONObject();
            formDetailsJson.put("#", iDisplayStart + i + 1);
            final CmsNewsImagesEntity object = dbList.get(i);
            formDetailsJson.put("image", object.getId());
            formDetailsJson.put("title", object.getTitle());
            formDetailsJson.put("fileName", object.getFileName());
            formDetailsJson.put("url", object.getUrl());
            formDetailsJson.put("id", object.getId());
            jsonArray.add(formDetailsJson);
        }
        responseDetailsJson.put("sEcho", sEcho);
        responseDetailsJson.put("iTotalRecords", cmsNewsImagesService.getTotalNumberNotDeleted());
        responseDetailsJson.put("iTotalDisplayRecords", dbListDisplayRecordsSize);
        responseDetailsJson.put("aaData", jsonArray);
        return responseDetailsJson.toString();
    }

    @RequestMapping(value = "/news/addCmsNewsImage", method = RequestMethod.POST)
    public ModelAndView addCmsNewsImage(
        @ModelAttribute(value = "cmsNewsImage") CmsNewsImage cmsNewsImage,
        BindingResult result, Locale locale
    ) {
        BufferedImage image;
        CmsNewsImagesEntity newCmsNewsImage = new CmsNewsImagesEntity();
        try {
            image = ImageIO.read(cmsNewsImage.getFile().getInputStream());
            newCmsNewsImage.setWidth(image.getWidth());
            newCmsNewsImage.setHeight(image.getHeight());
        } catch (IOException e) {
            LOGGER.error("Problem with reading image", e);
        }
        newCmsNewsImage.setFileName(cmsNewsImage.getFile().getOriginalFilename());
        newCmsNewsImage.setFileSize(cmsNewsImage.getFile().getSize());
        newCmsNewsImage.setMimeType(cmsNewsImage.getFile().getContentType());
        newCmsNewsImage.setCreated(ZonedDateTime.now());
        newCmsNewsImage.setLastModified(newCmsNewsImage.getCreated());
        ValidationUtils.rejectIfEmpty(result, "title", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("TitleMustBeSet"));
        ValidationUtils.rejectIfEmpty(result, "newsId", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("CmsNewsMustBeSet"));
        if (!result.hasErrors()) {
            cmsNewsImage.setId(cmsNewsImagesService.add(newCmsNewsImage));
            cmsNewsImage.setUrl(imageStorageService.storeNewsImage(newCmsNewsImage));
            cmsNewsImagesService.updateUrl(newCmsNewsImage);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/news/" + cmsNewsImage.getNewsId(), true, false, false));
        return modelAndView;
    }

    @RequestMapping(value = "/news/deleteCmsNewsImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteCmsNewsImage(
        @ModelAttribute(value = "cmsNewsImage") CmsNewsImagesEntity cmsNewsImage,
        BindingResult result, Locale locale
    ) {
        ValidationUtils.rejectIfEmpty(result, "id", ResourceBundle.getBundle(CWSFE_CMS_RESOURCE_BUNDLE_PATH, locale).getString("ImageMustBeSet"));
        JSONObject responseDetailsJson = new JSONObject();
        if (!result.hasErrors()) {
            cmsNewsImagesService.delete(cmsNewsImage);
            addJsonSuccess(responseDetailsJson);
        } else {
            prepareErrorResponse(result, responseDetailsJson);
        }
        return responseDetailsJson.toString();
    }

}
