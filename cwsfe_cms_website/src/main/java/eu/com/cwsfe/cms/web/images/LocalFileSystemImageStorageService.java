package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.db.blog.BlogPostImagesEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
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

/**
 * @author Radoslaw Osinski
 */
@Profile({"wildfly", "tomcat", "localFileSystemImageStorage"})
@Service
public class LocalFileSystemImageStorageService implements ImageStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesController.class);

    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    @Autowired
    public LocalFileSystemImageStorageService(CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    public String storeNewsImage(CmsNewsImagesEntity cmsNewsImage) {
        CmsGlobalParamsEntity newsImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
//        storeImage(cmsNewsImage.getFile(), newsImagesPath.getValue());
        CmsGlobalParamsEntity cmsMainUrl = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_MAIN_URL");
        return cmsMainUrl.getValue() + "/newsImages/" + cmsNewsImage.getFileName();   //todo extract in spring security hardcoded /newsImages
    }

    public String storeBlogImage(BlogPostImagesEntity blogPostImage) {
        CmsGlobalParamsEntity blogImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
//        storeImage(blogPostImage.getFile(), blogImagesPath.getValue());
        CmsGlobalParamsEntity cmsMainUrl = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_MAIN_URL");
        return cmsMainUrl.getValue() + "/blogPostImages/" + blogPostImage.getFileName();  //todo extract in spring security hardcoded /blogPostImages
    }

    @Override
    public boolean isBlogImagesStorageInitialized() {
        CmsGlobalParamsEntity newsImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
        File newsImagesDirectory = new File(newsImagesPath.getValue());
        return newsImagesDirectory.exists() && newsImagesDirectory.isDirectory();
    }

    @Override
    public boolean isNewsImagesStorageInitialized() {
        CmsGlobalParamsEntity newsImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        File newsImagesDirectory = new File(newsImagesPath.getValue());
        return newsImagesDirectory.exists() && newsImagesDirectory.isDirectory();
    }

    @Override
    public void initializeBlogImagesStorage() {
        CmsGlobalParamsEntity newsImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
        File newsImagesDirectory = new File(newsImagesPath.getValue());
        if (newsImagesDirectory.mkdir()) {
            LOGGER.error("Failed to create folder: " + newsImagesPath.getValue());
        }
    }

    @Override
    public void initializeNewsImagesStorage() {
        CmsGlobalParamsEntity newsImagesPath = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        File newsImagesDirectory = new File(newsImagesPath.getValue());
        if (newsImagesDirectory.mkdir()) {
            LOGGER.error("Failed to create folder: " + newsImagesPath.getValue());
        }
    }

    private void storeImage(MultipartFile image, String imagePath) {
        File copiedFile = new File(imagePath, image.getOriginalFilename());
        try {
            copiedFile.setExecutable(false);
            Files.copy(image.getInputStream(), copiedFile.toPath());
        } catch (IOException e) {
            LOGGER.error("Cannot save image to " + copiedFile.getAbsolutePath(), e);
        }
    }

}
