package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsNewsImage;

/**
 * @author Radoslaw Osinski
 */
public interface ImageStorageService {

    String storeNewsImage(CmsNewsImage cmsNewsImage);

    String storeBlogImage(BlogPostImage cmsNewsImage);

    boolean isBlogImagesStorageInitialized();

    boolean isNewsImagesStorageInitialized();

    void initializeBlogImagesStorage();

    void initializeNewsImagesStorage();
}
