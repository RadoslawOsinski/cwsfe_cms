//package eu.com.cwsfe.cms.rest;
//
//import eu.com.cwsfe.cms.dao.*;
//import eu.com.cwsfe.cms.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Created by Radosław Osiński
// */
////todo mappers http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
//@RestController
//public class FoldersRestController {
//
//    private final CmsFoldersRepository cmsFoldersDAO;
//
//    private final CmsTextI18nRepository cmsTextI18nDAO;
//
//    @Autowired
//    public FoldersRestController(CmsTextI18nRepository cmsTextI18nDAO, CmsFoldersRepository cmsFoldersDAO) {
//        this.cmsTextI18nRepository = cmsTextI18nDAO;
//        this.cmsFoldersRepository = cmsFoldersDAO;
//    }
//
//    /**
//     * @param languageCode language code
//     * @return internationalized folders list
//     */
//    @RequestMapping(value = "/rest/foldersList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
//    public List<CmsFolderDTO> foldersList(
//        @RequestParam(value = "languageCode") String languageCode
//    ) {
//        List<CmsFolderDTO> list = cmsFoldersDAO.list();
//        for (CmsFolderDTO cmsFolder : list) {
//            cmsFolder.setFolderName(cmsTextI18nDAO.findTranslation(languageCode, "Folders", cmsFolder.getFolderName()));
//        }
//        return list;
//    }
//
//    @ExceptionHandler(value = EmptyResultDataAccessException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public void handleEmptyResult() {
//    }
//}
