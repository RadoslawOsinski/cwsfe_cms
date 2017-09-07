package eu.com.cwsfe.cms.rest.news;

import eu.com.cwsfe.cms.services.news.CmsNewsImageDTO;
import eu.com.cwsfe.cms.services.news.CmsNewsImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@RestController
public class NewsImagesRestController {

    private final CmsNewsImagesService cmsNewsImagesService;

    @Autowired
    public NewsImagesRestController(CmsNewsImagesService cmsNewsImagesService) {
        this.cmsNewsImagesService = cmsNewsImagesService;
    }

    @RequestMapping(value = "/rest/newsImages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Map<String, Object> getImagesForNewsWithoutContent(
        @RequestParam(value = "newsId") long newsId,
        HttpServletResponse response
    ) {
        Optional<CmsNewsImageDTO> thumbnailForNews = cmsNewsImagesService.getThumbnailForNews(newsId);
        if (thumbnailForNews.isPresent()) {
            List<CmsNewsImageDTO> cmsNewsImages = cmsNewsImagesService.listImagesForNewsWithoutThumbnails(newsId);
            Map<String, Object> result = new HashMap<>(1);
            result.put("thumbnailImage", thumbnailForNews.get());
            result.put("newsImages", cmsNewsImages);
            return result;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

}
