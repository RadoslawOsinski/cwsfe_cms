package eu.com.cwsfe.cms.web.configuration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

/**
 * @author Radoslaw Osinski
 */
@Profile("tomcat-aws")
@Configuration
public class AWSClientInitializer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSClientInitializer.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    @Autowired
    public AWSClientInitializer(CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        AmazonS3 s3client = new AmazonS3Client(new SystemPropertiesCredentialsProvider());
        Optional<CmsGlobalParamsEntity> awsImagesBucketRegion = cmsGlobalParamsService.getByCode("AWS_IMAGES_BUCKET_REGION");
        if (awsImagesBucketRegion.isPresent()) {
            s3client.setRegion(Region.getRegion(Regions.valueOf(awsImagesBucketRegion.get().getValue())));
        } else {
            LOGGER.error("Missing configuration for AWS_IMAGES_BUCKET_REGION");
        }
        return s3client;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeRootBucket();
    }

    private void initializeRootBucket() {
        Optional<CmsGlobalParamsEntity> rootBucketName = cmsGlobalParamsService.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        try {
            if (rootBucketName.isPresent()) {
                if (!getAmazonS3().doesBucketExist(rootBucketName.get().getValue())) {
                    getAmazonS3().createBucket(new CreateBucketRequest(rootBucketName.get().getValue()));
                }
            } else {
                LOGGER.error("Missing configuration for AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME parameter");
            }
        } catch (AmazonServiceException ase) {
            LOGGER.error("Problem with creating root cwsfe cms bucket", ase);
        } catch (AmazonClientException ace) {
            LOGGER.error("Problem with connecting to AWS S3", ace);
        }
    }

}
