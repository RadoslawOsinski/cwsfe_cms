//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.CmsNewsImagesDAO;
//import eu.com.cwsfe.cms.services.news.CmsNewsImageDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Radosław Osiński
// */
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class NewsImagesRestController {
//
//    private final CmsNewsImagesRepository cmsNewsImagesDAO;
//
//    @Autowired
//    public NewsImagesRestController(CmsNewsImagesRepository cmsNewsImagesDAO) {
//        this.cmsNewsImagesRepository = cmsNewsImagesDAO;
//    }
//
//    @RequestMapping(value = "/rest/newsImages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public Map<String, Object> getImagesForNewsWithoutContent(
//        @RequestParam(value = "newsId") long newsId
//    ) {
//        CmsNewsImageDTO thumbnailForNews = cmsNewsImagesDAO.getThumbnailForNews(newsId);
//        List<CmsNewsImageDTO> cmsNewsImages = cmsNewsImagesDAO.listImagesForNewsWithoutThumbnails(newsId);
//        Map<String, Object> result = new HashMap<>(1);
//        result.put("thumbnailImage", thumbnailForNews);
//        result.put("newsImages", cmsNewsImages);
//        return result;
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//}
