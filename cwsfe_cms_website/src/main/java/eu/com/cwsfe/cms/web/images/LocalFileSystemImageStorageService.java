package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.app.configuration.SecurityConfig;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import eu.com.cwsfe.cms.web.news.CmsNewsImagesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author Radoslaw Osinski
 */
@Profile({"wildfly", "tomcat", "localFileSystemImageStorage"})
@Service
public class LocalFileSystemImageStorageService implements ImageStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesController.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    @Autowired
    public LocalFileSystemImageStorageService(CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    public String storeNewsImage(MultipartFile file) {
        Optional<CmsGlobalParamsEntity> newsImagesPath = cmsGlobalParamsService.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        if (!newsImagesPath.isPresent()) {
            LOGGER.error("Missing configuration CWSFE_CMS_NEWS_IMAGES_PATH");
            return "Missing configuration CWSFE_CMS_NEWS_IMAGES_PATH";
        } else {
            storeImage(file, newsImagesPath.get().getValue());
            Optional<CmsGlobalParamsEntity> cmsMainUrl = cmsGlobalParamsService.getByCode("CWSFE_CMS_MAIN_URL");
            if (!cmsMainUrl.isPresent()) {
                LOGGER.error("Missing configuration CWSFE_CMS_MAIN_URL");
                return "Missing configuration CWSFE_CMS_MAIN_URL";
            } else {
                return cmsMainUrl.get().getValue() + SecurityConfig.NEWS_IMAGES + file.getOriginalFilename();
            }
        }
    }

    @Override
    public boolean isNewsImagesStorageInitialized() {
        Optional<CmsGlobalParamsEntity> newsImagesPath = cmsGlobalParamsService.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        if (!newsImagesPath.isPresent()) {
            LOGGER.error("Missing configuration CWSFE_CMS_NEWS_IMAGES_PATH");
            return false;
        } else {
            File newsImagesDirectory = new File(newsImagesPath.get().getValue());
            return newsImagesDirectory.exists() && newsImagesDirectory.isDirectory();
        }
    }

    @Override
    public void initializeNewsImagesStorage() {
        Optional<CmsGlobalParamsEntity> newsImagesPath = cmsGlobalParamsService.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        if (newsImagesPath.isPresent()) {
            File newsImagesDirectory = new File(newsImagesPath.get().getValue());
            if (newsImagesDirectory.mkdir()) {
                LOGGER.error("Failed to create folder: " + newsImagesPath.get().getValue());
            }
        } else {
            LOGGER.error("Missing configuration CWSFE_CMS_NEWS_IMAGES_PATH");
        }
    }

    private void storeImage(MultipartFile image, String imagePath) {
        assert image.getOriginalFilename() != null : "File name must be present!";
        File copiedFile = new File(imagePath, image.getOriginalFilename());
        try {
            boolean executableResult = copiedFile.setExecutable(false);
            LOGGER.trace("Setting executable for file: {}, result: {}", copiedFile.getAbsolutePath(), executableResult);
            Files.copy(image.getInputStream(), copiedFile.toPath());
        } catch (IOException e) {
            LOGGER.error("Cannot save image to " + copiedFile.getAbsolutePath(), e);
        }
    }

}
