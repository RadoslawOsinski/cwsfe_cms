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
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * @author Radoslaw Osinski
 */
@Profile("tomcat-aws")
@Configuration
public class AWSClientInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSClientInitializer.class);

    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    @Autowired
    public AWSClientInitializer(CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        AmazonS3 s3client = new AmazonS3Client(new SystemPropertiesCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.valueOf(cmsGlobalParamsRepository.getByCode("AWS_IMAGES_BUCKET_REGION").getValue())));
        return s3client;
    }

    @PostConstruct
    public void initializeRootBucket() {
        CmsGlobalParamsEntity rootBucketName = cmsGlobalParamsRepository.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        try {
            if (!getAmazonS3().doesBucketExist(rootBucketName.getValue())) {
                getAmazonS3().createBucket(new CreateBucketRequest(rootBucketName.getValue()));
            }
        } catch (AmazonServiceException ase) {
            LOGGER.error("Problem with creating root cwsfe cms bucket", ase);
        } catch (AmazonClientException ace) {
            LOGGER.error("Problem with connecting to AWS S3", ace);
        }
    }
}
