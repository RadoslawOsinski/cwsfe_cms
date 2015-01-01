package eu.com.cwsfe.cms.rest;

import eu.com.cwsfe.cms.dao.CmsNewsDAO;
import eu.com.cwsfe.cms.model.CmsNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* Created by Radosław Osiński
*/
@RestController
public class NewsRestController {

    @Autowired
    private CmsNewsDAO cmsNewsDAO;

    @RequestMapping(value = "/rest/news", method = RequestMethod.GET)
    public CmsNews getNewsByNewsTypeFolderAndNewsCode(
            @RequestParam(value="newsTypeId") long newsTypeId,
            @RequestParam(value="folderId") long folderId,
            @RequestParam(value="newsCode") String newsCode
            ) {
        return cmsNewsDAO.getByNewsTypeFolderAndNewsCode(newsTypeId, folderId, newsCode);
    }
}
