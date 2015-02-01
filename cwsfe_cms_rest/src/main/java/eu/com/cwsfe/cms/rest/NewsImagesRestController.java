package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsNewsImagesDAO;
import eu.com.cwsfe.cms.model.CmsNewsImage;
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

    @Autowired
    private CmsNewsImagesDAO cmsNewsImagesDAO;

    @RequestMapping(value = "/rest/newsImages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Map<String, Object> getImagesForNews(
            @RequestParam(value = "newsId") long newsId
    ) {
        CmsNewsImage thumbnailForNews = cmsNewsImagesDAO.getThumbnailForNews(newsId);
        List<CmsNewsImage> cmsNewsImages = cmsNewsImagesDAO.listImagesForNewsWithoutThumbnails(newsId);
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
