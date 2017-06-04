//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.BlogPostCodesDAO;
//import eu.com.cwsfe.cms.model.BlogPostCodeDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Created by Radosław Osiński
// */
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class BlogPostCodeRestController {
//
//    private final BlogPostCodesRepository blogPostCodesDAO;
//
//    @Autowired
//    public BlogPostCodeRestController(BlogPostCodesRepository blogPostCodesDAO) {
//        this.blogPostCodesRepository = blogPostCodesDAO;
//    }
//
//    /**
//     * @param postId blog post id
//     * @param codeId code id
//     * @return blogPostCodesDAO
//     */
//    @RequestMapping(value = "/rest/blogPostCode/{postId}/{codeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public BlogPostCodeDTO getPostCode(
//        @PathVariable(value = "postId") long postId,
//        @PathVariable(value = "codeId") String codeId
//    ) {
//        return blogPostCodesDAO.getCodeForPostByCodeId(postId, codeId);
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//}
