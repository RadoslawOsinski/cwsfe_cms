package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.db.blog.BlogPostImagesEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;

/**
 * @author Radoslaw Osinski
 */
public interface ImageStorageService {

    String storeNewsImage(CmsNewsImagesEntity cmsNewsImage);

    String storeBlogImage(BlogPostImagesEntity cmsNewsImage);

    boolean isBlogImagesStorageInitialized();

    boolean isNewsImagesStorageInitialized();

    void initializeBlogImagesStorage();

    void initializeNewsImagesStorage();
}
