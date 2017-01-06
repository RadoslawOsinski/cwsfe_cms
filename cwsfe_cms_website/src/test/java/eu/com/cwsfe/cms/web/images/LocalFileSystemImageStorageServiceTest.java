package eu.com.cwsfe.cms.web.images;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsGlobalParam;
import eu.com.cwsfe.cms.model.CmsNewsImage;
import eu.com.cwsfe.cms.web.configuration.IntegrationTestsDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
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

import static org.junit.Assert.assertTrue;

/**
 * @author Radoslaw Osinski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestsDataSource.class, LocalFileSystemImageStorageService.class, CmsGlobalParamsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration_tests_local"})
public class LocalFileSystemImageStorageServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileSystemImageStorageServiceTest.class);

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @Autowired
    private LocalFileSystemImageStorageService localFileSystemImageStorageService;

    @Before
    public void cleanOrCreateImagesFolders() throws IOException {
        CmsGlobalParam newsImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        CmsGlobalParam blogImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
        File newsImagesDir = new File(newsImagesPath.getValue());
        if (!newsImagesDir.exists()) {
            if (newsImagesDir.mkdir()) {
                LOGGER.error("Failed to create folder: " + newsImagesPath.getValue());
            }
        }
        FileUtils.cleanDirectory(newsImagesDir);
        File blogImagesDir = new File(blogImagesPath.getValue());
        if (!blogImagesDir.exists()) {
            if (blogImagesDir.mkdir()) {
                LOGGER.error("Failed to create folder: " + newsImagesPath.getValue());
            }
        }
        FileUtils.cleanDirectory(blogImagesDir);
    }

    @After
    public void cleanImagesFolders() throws IOException {
        CmsGlobalParam newsImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        CmsGlobalParam blogImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
        File newsImagesDir = new File(newsImagesPath.getValue());
        FileUtils.cleanDirectory(newsImagesDir);
        File blogImagesDir = new File(blogImagesPath.getValue());
        FileUtils.cleanDirectory(blogImagesDir);
    }

    @Test
    public void storeNewsImage() throws Exception {
        //given
        File file = new File("src/main/webapp/resources-cwsfe-cms/CWSFE_logo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
            file.getName(), "image/png", IOUtils.toByteArray(input));
        CmsNewsImage cmsNewsImage = new CmsNewsImage();
        cmsNewsImage.setFile(multipartFile);

        //when
        localFileSystemImageStorageService.storeNewsImage(cmsNewsImage);

        //then
        CmsGlobalParam newsImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_NEWS_IMAGES_PATH");
        File outputFile = new File(newsImagesPath.getValue() + "/CWSFE_logo.png");
        assertTrue("File should be persisted in file system: " + outputFile.getAbsolutePath(), outputFile.exists());
    }

    @Test
    public void storeBlogImage() throws Exception {
        //given
        File file = new File("src/main/webapp/resources-cwsfe-cms/CWSFE_logo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
            file.getName(), "image/png", IOUtils.toByteArray(input));
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setFile(multipartFile);

        //when
        localFileSystemImageStorageService.storeBlogImage(blogPostImage);

        //then
        CmsGlobalParam newsImagesPath = cmsGlobalParamsDAO.getByCode("CWSFE_CMS_BLOG_IMAGES_PATH");
        File outputFile = new File(newsImagesPath.getValue() + "/CWSFE_logo.png");
        assertTrue("File should be persisted in file system: " + outputFile.getAbsolutePath(), outputFile.exists());
    }
}
