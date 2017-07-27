package eu.com.cwsfe.cms.web.images;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
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

import java.io.IOException;

/**
 * @author Radoslaw Osinski
 */
@Profile("tomcat-aws")
@Service
public class AWSBucketImageStorageService implements ImageStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesController.class);

    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    private final AmazonS3 amazonS3;

    @Autowired
    public AWSBucketImageStorageService(CmsGlobalParamsRepository cmsGlobalParamsRepository, AmazonS3 amazonS3) {
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
        this.amazonS3 = amazonS3;
    }

    public String storeNewsImage(CmsNewsImagesEntity cmsNewsImage) {
        CmsGlobalParamsEntity newsImagesBucket = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_S3_NEWS_IMAGES_PATH");
//        storeImage(cmsNewsImage.getFile(), newsImagesBucket.getValue());
        CmsGlobalParamsEntity rootBucketName = cmsGlobalParamsRepository.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        return amazonS3.getUrl(rootBucketName.getValue(), newsImagesBucket.getValue() + "/" + cmsNewsImage.getFileName()).toString();
    }

    public String storeBlogImage(BlogPostImagesEntity blogPostImage) {
        CmsGlobalParamsEntity blogImagesBucket = cmsGlobalParamsRepository.getByCode("CWSFE_CMS_S3_BLOG_IMAGES_PATH");
//        storeImage(blogPostImage.getFile(), blogImagesBucket.getValue());
        CmsGlobalParamsEntity rootBucketName = cmsGlobalParamsRepository.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        return amazonS3.getUrl(rootBucketName.getValue(), blogImagesBucket.getValue() + "/" + blogPostImage.getFileName()).toString();
    }

    @Override
    public boolean isBlogImagesStorageInitialized() {
        //AWS makes folders automatically
        return true;
    }

    @Override
    public boolean isNewsImagesStorageInitialized() {
        //AWS makes folders automatically
        return true;
    }

    @Override
    public void initializeBlogImagesStorage() {
        //AWS makes folders automatically
    }

    @Override
    public void initializeNewsImagesStorage() {
        //AWS makes folders automatically
    }

    private void storeImage(MultipartFile image, String imagePath) {
        CmsGlobalParamsEntity rootBucketName = cmsGlobalParamsRepository.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(rootBucketName.getValue(), imagePath + "/" + image.getOriginalFilename(),
                image.getInputStream(), new ObjectMetadata());
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (AmazonServiceException | IOException e) {
            LOGGER.error("Cannot save image to " + imagePath + "/" + image.getOriginalFilename(), e);
        } catch (AmazonClientException e) {
            LOGGER.error("Problem with AWS S3: " + imagePath + "/" + image.getOriginalFilename(), e);
        }
    }

}
