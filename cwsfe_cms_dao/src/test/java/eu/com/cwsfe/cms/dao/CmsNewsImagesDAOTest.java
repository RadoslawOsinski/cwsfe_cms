package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.CmsNewsImageStatus;
import eu.com.cwsfe.cms.domains.CmsNewsStatus;
import eu.com.cwsfe.cms.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class CmsNewsImagesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsNewsImagesDAO dao;

    @Autowired
    private CmsNewsDAO newsDao;

    @Autowired
    private NewsTypesDAO newsTypesDAO;

    @Autowired
    private CmsFoldersDAO cmsFoldersDAO;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final NewsType NEWS_TYPE = new NewsType();
    private static final CmsFolder FOLDER = new CmsFolder();
    private static final CmsNews NEWS = new CmsNews();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        NEWS_TYPE.setType("type");
        NEWS_TYPE.setId(newsTypesDAO.add(NEWS_TYPE));

        FOLDER.setFolderName("folder");
        FOLDER.setOrderNumber(1l);
        FOLDER.setId(cmsFoldersDAO.add(FOLDER));

        NEWS.setAuthorId(CMS_AUTHOR.getId());
        NEWS.setNewsCode("news text code");
        NEWS.setStatus(CmsNewsStatus.NEW);
        NEWS.setNewsTypeId(NEWS_TYPE.getId());
        NEWS.setNewsFolderId(FOLDER.getId());
        NEWS.setCreationDate(new Date(4));
        NEWS.setId(newsDao.add(NEWS));
    }

    @Test
    public void testGetTotalNumberNotDeleted() throws Exception {
        //given
        //when
        int result = dao.getTotalNumberNotDeleted();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testCountForAjax() throws Exception {
        //given
        //when
        int result = dao.countForAjax();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testSearchByAjaxWithoutContent() throws Exception {
        //given
        //when
        List<CmsNewsImage> cmsNewsImages = dao.searchByAjaxWithoutContent(0, 1, 1l);

        //then
        assertNotNull(cmsNewsImages);
    }

    @Test
    public void testSearchByAjaxCountWithoutContent() throws Exception {
        //given
        //when
        int results = dao.searchByAjaxCountWithoutContent(1l);

        //then
        assertNotNull(results);
        assertTrue(results >= 0);
    }

    @Test
    public void testGetWithContent() throws Exception {
        //given
        String title = "title";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        Long addedId = dao.add(newsImage);

        //when
        CmsNewsImage result = dao.getWithContent(addedId);

        //then
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(title, result.getTitle());
        assertEquals(CmsNewsImageStatus.NEW, result.getStatus());
        assertTrue(Arrays.equals(content, result.getContent()));
        assertEquals(created, result.getCreated());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileSize, (long) result.getFileSize());
        assertEquals(width, (int) result.getWidth());
        assertEquals(height, (int) result.getHeight());
        assertEquals(mimeType, result.getMimeType());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String title = "title";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);

        //when
        Long addedId = dao.add(newsImage);

        //then
        CmsNewsImage result = dao.getWithContent(addedId);
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(title, result.getTitle());
        assertEquals(CmsNewsImageStatus.NEW, result.getStatus());
        assertTrue(Arrays.equals(content, result.getContent()));
        assertEquals(created, result.getCreated());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileSize, (long) result.getFileSize());
        assertEquals(width, (int) result.getWidth());
        assertEquals(height, (int) result.getHeight());
        assertEquals(mimeType, result.getMimeType());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String title = "title";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date(1);
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        String newTitle = "title2";
        byte[] newContent = new BigInteger("1000011110001", 2).toByteArray();
        Date newCreated = new Date(2);
        String newFileName = "fileName2";
        long newFileSize = 12l;
        int newWidth = 22;
        int newHeight = 32;
        String newMimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        newsImage.setId(dao.add(newsImage));
        newsImage.setTitle(newTitle);
        newsImage.setStatus(CmsNewsImageStatus.PUBLISHED);
        newsImage.setContent(newContent);
        newsImage.setCreated(newCreated);
        newsImage.setFileName(newFileName);
        newsImage.setFileSize(newFileSize);
        newsImage.setWidth(newWidth);
        newsImage.setHeight(newHeight);
        newsImage.setMimeType(newMimeType);

        //when
        dao.update(newsImage);

        //then
        CmsNewsImage result = dao.getWithContent(newsImage.getId());
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(newTitle, result.getTitle());
        assertTrue(Arrays.equals(newContent, result.getContent()));
        assertEquals(newCreated, result.getCreated());
        assertEquals(newFileName, result.getFileName());
        assertEquals(newFileSize, (long) result.getFileSize());
        assertEquals(newWidth, (int) result.getWidth());
        assertEquals(newHeight, (int) result.getHeight());
        assertEquals(newMimeType, result.getMimeType());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String title = "title";
        byte[] content = new byte[100];
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        newsImage.setId(dao.add(newsImage));

        //when
        dao.delete(newsImage);

        //then
        CmsNewsImage result = dao.getWithContent(newsImage.getId());
        assertNotNull("News image should exists", result);
        assertEquals("News image should be deleted", CmsNewsImageStatus.DELETED, result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String title = "title";
        byte[] content = new byte[100];
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        newsImage.setId(dao.add(newsImage));

        //when
        dao.undelete(newsImage);

        //then
        CmsNewsImage result = dao.getWithContent(newsImage.getId());
        assertNotNull("News image should exists", result);
        assertEquals("News image should undeleted", CmsNewsImageStatus.NEW, result.getStatus());
    }

    @Test
    public void testListImagesForNewsWithoutThumbnails() throws Exception {
        //given
        //when
        List<CmsNewsImage> cmsNewsImagesWithoutThumbnails = dao.listImagesForNewsWithoutThumbnails(1l);

        //then
        assertNotNull(cmsNewsImagesWithoutThumbnails);
        for (CmsNewsImage cmsNewsImage : cmsNewsImagesWithoutThumbnails) {
            assertFalse(cmsNewsImage.getTitle().startsWith("thumbnail_"));
        }
    }

    @Test
    public void testGetThumbnailForNews() throws Exception {
        //given
        String title = "thumbnail_title";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        dao.add(newsImage);

        //when
        CmsNewsImage result = dao.getThumbnailForNews(NEWS.getId());

        //then
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(title, result.getTitle());
        assertTrue("title is not starting from thumbnail_ text", result.getTitle().startsWith("thumbnail_"));
        assertEquals(CmsNewsImageStatus.NEW, result.getStatus());
        assertTrue(Arrays.equals(content, result.getContent()));
        assertEquals(created, result.getCreated());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileSize, (long) result.getFileSize());
        assertEquals(width, (int) result.getWidth());
        assertEquals(height, (int) result.getHeight());
        assertEquals(mimeType, result.getMimeType());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String title = "title";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        CmsNewsImage newsImage = new CmsNewsImage();
        newsImage.setNewsId(NEWS.getId());
        newsImage.setTitle(title);
        newsImage.setStatus(CmsNewsImageStatus.NEW);
        newsImage.setContent(content);
        newsImage.setCreated(created);
        newsImage.setFileName(fileName);
        newsImage.setFileSize(fileSize);
        newsImage.setWidth(width);
        newsImage.setHeight(height);
        newsImage.setMimeType(mimeType);
        Long addedId = dao.add(newsImage);

        //when
        CmsNewsImage result = dao.getWithContent(addedId);

        //then
        assertNotNull(result);
        assertEquals(NEWS.getId(), result.getNewsId());
        assertEquals(title, result.getTitle());
        assertEquals(CmsNewsImageStatus.NEW, result.getStatus());
        assertTrue(Arrays.equals(content, result.getContent()));
        assertEquals(created, result.getCreated());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileSize, (long) result.getFileSize());
        assertEquals(width, (int) result.getWidth());
        assertEquals(height, (int) result.getHeight());
        assertEquals(mimeType, result.getMimeType());
    }
}