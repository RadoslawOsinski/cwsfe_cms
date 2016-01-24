package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsNewsImage;

/**
 * @author Radoslaw Osinski
 */
public interface ImageStorageService {

    void storeNewsImage(CmsNewsImage cmsNewsImage);

    void storeBlogImage(BlogPostImage cmsNewsImage);

}
