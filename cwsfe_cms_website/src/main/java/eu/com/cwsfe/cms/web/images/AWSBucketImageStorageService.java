package eu.com.cwsfe.cms.web.images;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import eu.com.cwsfe.cms.web.news.CmsNewsImagesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Radoslaw Osinski
 */
@Profile("tomcat-aws")
@Service
public class AWSBucketImageStorageService implements ImageStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesController.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    private final AmazonS3 amazonS3;

    @Autowired
    public AWSBucketImageStorageService(CmsGlobalParamsService cmsGlobalParamsService, AmazonS3 amazonS3) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
        this.amazonS3 = amazonS3;
    }

    public String storeNewsImage(CmsNewsImagesEntity cmsNewsImage) {
        Optional<CmsGlobalParamsEntity> newsImagesBucket = cmsGlobalParamsService.getByCode("CWSFE_CMS_S3_NEWS_IMAGES_PATH");
//        storeImage(cmsNewsImage.getFile(), newsImagesBucket.getValue());
        Optional<CmsGlobalParamsEntity> rootBucketName = cmsGlobalParamsService.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        return amazonS3.getUrl(rootBucketName.get().getValue(), newsImagesBucket.get().getValue() + "/" + cmsNewsImage.getFileName()).toString();
    }

    @Override
    public boolean isNewsImagesStorageInitialized() {
        //AWS makes folders automatically
        return true;
    }

    @Override
    public void initializeNewsImagesStorage() {
        //AWS makes folders automatically
    }

    private void storeImage(MultipartFile image, String imagePath) {
        Optional<CmsGlobalParamsEntity> rootBucketName = cmsGlobalParamsService.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        if (!rootBucketName.isPresent()) {
            LOGGER.error("Missing configuration AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        } else {
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(rootBucketName.get().getValue(), imagePath + "/" + image.getOriginalFilename(),
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

}
