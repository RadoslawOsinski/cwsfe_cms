package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;

/**
 * @author Radoslaw Osinski
 */
public interface ImageStorageService {

    String storeNewsImage(CmsNewsImagesEntity cmsNewsImage);

    boolean isNewsImagesStorageInitialized();

    void initializeNewsImagesStorage();
}
