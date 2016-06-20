package eu.com.cwsfe.cms.web.configuration;

import eu.com.cwsfe.cms.web.images.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Radoslaw Osinski
 */
@Service
public class ImageStorageInitializer {

    final ImageStorageService imageStorageService;

    @Autowired
    public ImageStorageInitializer(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostConstruct
    public void initializeImageStorage() {
        if (!imageStorageService.isBlogImagesStorageInitialized()) {
            imageStorageService.initializeBlogImagesStorage();
        }
        if (!imageStorageService.isNewsImagesStorageInitialized()) {
            imageStorageService.initializeNewsImagesStorage();
        }
    }
}
