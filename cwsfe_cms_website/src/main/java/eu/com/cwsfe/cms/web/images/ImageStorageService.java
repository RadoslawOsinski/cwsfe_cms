package eu.com.cwsfe.cms.web.images;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Radoslaw Osinski
 */
public interface ImageStorageService {

    String storeNewsImage(MultipartFile file);

    boolean isNewsImagesStorageInitialized();

    void initializeNewsImagesStorage();
}
