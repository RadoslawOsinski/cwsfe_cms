package eu.com.cwsfe.cms.web.images;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsNewsImage;
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

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @Autowired
    private AmazonS3 amazonS3;

    public void storeNewsImage(CmsNewsImage cmsNewsImage) {
        CmsGlobalParam newsImagesBucket = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_S3_NEWS_IMAGES_PATH");
        storeImage(cmsNewsImage.getFile(), newsImagesBucket.getValue());
    }

    public void storeBlogImage(BlogPostImage blogPostImage) {
        CmsGlobalParam blogImagesBucket = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_S3_BLOG_IMAGES_PATH");
        storeImage(blogPostImage.getFile(), blogImagesBucket.getValue());
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
        CmsGlobalParam rootBucketName = cmsGlobalParamsDAO.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
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
