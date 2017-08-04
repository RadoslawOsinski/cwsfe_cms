package eu.com.cwsfe.cms.rest.news;

import eu.com.cwsfe.cms.services.news.CmsNewsImageDTO;
import eu.com.cwsfe.cms.services.news.CmsNewsImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        @RequestParam(value = "newsId") long newsId
    ) {
        CmsNewsImageDTO thumbnailForNews = cmsNewsImagesService.getThumbnailForNews(newsId);
        List<CmsNewsImageDTO> cmsNewsImages = cmsNewsImagesService.listImagesForNewsWithoutThumbnails(newsId);
        Map<String, Object> result = new HashMap<>(1);
        result.put("thumbnailImage", thumbnailForNews);
        result.put("newsImages", cmsNewsImages);
        return result;
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleEmptyResult() {
    }
}
