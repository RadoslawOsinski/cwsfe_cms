package eu.com.cwsfe.cms.web.images;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsNewsImage;
import eu.com.cwsfe.cms.web.configuration.AWSClientInitializer;
import eu.com.cwsfe.cms.web.configuration.IntegrationTestsDataSource;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Radoslaw Osinski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AWSClientInitializer.class, IntegrationTestsDataSource.class, AWSBucketImageStorageService.class, CmsGlobalParamsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration_tests_local"})
public class AWSBucketImageStorageServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemImageStorageServiceTest.class);

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    private AWSBucketImageStorageService awsBucketImageStorageService;

    @After
    public void cleanImagesFolders() throws IOException {
        CmsGlobalParam rootBucket = cmsGlobalParamsDAO.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        ObjectListing objectListing = amazonS3.listObjects(rootBucket.getValue());
        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                amazonS3.deleteObject(rootBucket.getValue(), objectSummary.getKey());
            }
            if (objectListing.isTruncated()) {
                objectListing = amazonS3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        VersionListing list = amazonS3.listVersions(new ListVersionsRequest().withBucketName(rootBucket.getValue()));
        for (S3VersionSummary s : list.getVersionSummaries()) {
            amazonS3.deleteVersion(rootBucket.getValue(), s.getKey(), s.getVersionId());
        }
    }

    @Test
    public void storeNewsImage() throws Exception {
        //given
        File file = new File("src/main/webapp/resources-cwsfe-cms/CWSFE_logo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/png", IOUtils.toByteArray(input));
        CmsNewsImage cmsNewsImage = new CmsNewsImage();
        cmsNewsImage.setFileName("CWSFE_logo.png");
        cmsNewsImage.setFile(multipartFile);

        //when
        String imageUrl = awsBucketImageStorageService.storeNewsImage(cmsNewsImage);

        //then
        CmsGlobalParam rootBucket = cmsGlobalParamsDAO.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        CmsGlobalParam newsImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_S3_NEWS_IMAGES_PATH");
        boolean fileUploaded = true;
        try {
            amazonS3.getObjectMetadata(new GetObjectMetadataRequest(rootBucket.getValue(), newsImagesPath.getValue() + "/CWSFE_logo.png"));
        } catch (AmazonServiceException e) {
            fileUploaded = false;
            LOGGER.error("File upload error: ", e);
        }
        assertTrue("File should be persisted in S3", fileUploaded);
        assertNotNull("File should have a url for download", imageUrl);
    }

    @Test
    public void storeBlogImage() throws Exception {
        //given
        File file = new File("src/main/webapp/resources-cwsfe-cms/CWSFE_logo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/png", IOUtils.toByteArray(input));
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setFileName("CWSFE_logo.png");
        blogPostImage.setFile(multipartFile);

        //when
        String imageUrl = awsBucketImageStorageService.storeBlogImage(blogPostImage);

        //then
        CmsGlobalParam rootBucket = cmsGlobalParamsDAO.getByCode("AWS_CWSFE_CMS_S3_ROOT_BUCKET_NAME");
        CmsGlobalParam blogImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_S3_BLOG_IMAGES_PATH");
        boolean fileUploaded = true;
        try {
            amazonS3.getObjectMetadata(new GetObjectMetadataRequest(rootBucket.getValue(), blogImagesPath.getValue() + "/CWSFE_logo.png"));
        } catch (AmazonServiceException e) {
            fileUploaded = false;
            LOGGER.error("File upload error: ", e);
        }
        assertTrue("File should be persisted in S3", fileUploaded);
        assertNotNull("File should have a url for download", imageUrl);
    }
}