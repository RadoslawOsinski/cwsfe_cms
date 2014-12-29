package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsAuthor;
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
public class BlogPostImagesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostImagesDAO dao;

    @Autowired
    private BlogPostsDAO postsDao;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final BlogPost BLOG_POST = new BlogPost();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        BLOG_POST.setPostAuthorId(CMS_AUTHOR.getId());
        BLOG_POST.setPostTextCode("post text code");
        BLOG_POST.setStatus("N");
        BLOG_POST.setId(postsDao.add(BLOG_POST));
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
        List<BlogPostImage> blogPostImages = dao.searchByAjaxWithoutContent(0, 1, 1l);

        //then
        assertNotNull(blogPostImages);
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
    public void testListForPostWithoutContent() throws Exception {
        //given
        //when
        List<BlogPostImage> blogPostImages = dao.listForPostWithoutContent(1l);

        //then
        assertNotNull(blogPostImages);
    }

    @Test
    public void testListForPostWithContent() throws Exception {
        //given
        //when
        List<BlogPostImage> blogPostImages = dao.listForPostWithContent(1l);

        //then
        assertNotNull(blogPostImages);
    }

    @Test
    public void testListWithContent() throws Exception {
        //given
        //when
        List<BlogPostImage> blogPostImages = dao.listWithContent();

        //then
        assertNotNull(blogPostImages);
    }

    @Test
    public void testGetWithContent() throws Exception {
        //given
        String title = "title";
        String status = "N";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(status);
        blogPostImage.setContent(content);
        blogPostImage.setCreated(created);
        blogPostImage.setFileName(fileName);
        blogPostImage.setFileSize(fileSize);
        blogPostImage.setWidth(width);
        blogPostImage.setHeight(height);
        blogPostImage.setMimeType(mimeType);
        Long addedId = dao.add(blogPostImage);

        //when
        BlogPostImage result = dao.getWithContent(addedId);

        //then
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getBlogPostId());
        assertEquals(title, result.getTitle());
        assertEquals(status, result.getStatus());
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
        String status = "N";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(status);
        blogPostImage.setContent(content);
        blogPostImage.setCreated(created);
        blogPostImage.setFileName(fileName);
        blogPostImage.setFileSize(fileSize);
        blogPostImage.setWidth(width);
        blogPostImage.setHeight(height);
        blogPostImage.setMimeType(mimeType);

        //when
        Long addedId = dao.add(blogPostImage);

        //then
        BlogPostImage result = dao.getWithContent(addedId);
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getBlogPostId());
        assertEquals(title, result.getTitle());
        assertEquals(status, result.getStatus());
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
        String status = "N";
        byte[] content = new BigInteger("1111000011110001", 2).toByteArray();
        Date created = new Date(1);
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        String newTitle = "title2";
        String newStatus = "P";
        byte[] newContent = new BigInteger("1000011110001", 2).toByteArray();
        Date newCreated = new Date(2);
        String newFileName = "fileName2";
        long newFileSize = 12l;
        int newWidth = 22;
        int newHeight = 32;
        String newMimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(status);
        blogPostImage.setContent(content);
        blogPostImage.setCreated(created);
        blogPostImage.setFileName(fileName);
        blogPostImage.setFileSize(fileSize);
        blogPostImage.setWidth(width);
        blogPostImage.setHeight(height);
        blogPostImage.setMimeType(mimeType);
        blogPostImage.setId(dao.add(blogPostImage));
        blogPostImage.setTitle(newTitle);
        blogPostImage.setStatus(newStatus);
        blogPostImage.setContent(newContent);
        blogPostImage.setCreated(newCreated);
        blogPostImage.setFileName(newFileName);
        blogPostImage.setFileSize(newFileSize);
        blogPostImage.setWidth(newWidth);
        blogPostImage.setHeight(newHeight);
        blogPostImage.setMimeType(newMimeType);

        //when
        dao.update(blogPostImage);

        //then
        BlogPostImage result = dao.getWithContent(blogPostImage.getId());
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getBlogPostId());
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
        String status = "N";
        byte[] content = new byte[100];
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(status);
        blogPostImage.setContent(content);
        blogPostImage.setCreated(created);
        blogPostImage.setFileName(fileName);
        blogPostImage.setFileSize(fileSize);
        blogPostImage.setWidth(width);
        blogPostImage.setHeight(height);
        blogPostImage.setMimeType(mimeType);
        blogPostImage.setId(dao.add(blogPostImage));

        //when
        dao.delete(blogPostImage);

        //then
        BlogPostImage result = dao.getWithContent(blogPostImage.getId());
        assertNotNull("Post image should exists", result);
        assertEquals("Post image should be deleted", "D", result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String title = "title";
        String status = "N";
        byte[] content = new byte[100];
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1l;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(status);
        blogPostImage.setContent(content);
        blogPostImage.setCreated(created);
        blogPostImage.setFileName(fileName);
        blogPostImage.setFileSize(fileSize);
        blogPostImage.setWidth(width);
        blogPostImage.setHeight(height);
        blogPostImage.setMimeType(mimeType);
        blogPostImage.setId(dao.add(blogPostImage));

        //when
        dao.undelete(blogPostImage);

        //then
        BlogPostImage result = dao.getWithContent(blogPostImage.getId());
        assertNotNull("Post image should exists", result);
        assertEquals("Post image should undeleted", "N", result.getStatus());
    }
}