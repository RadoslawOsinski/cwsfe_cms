package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.db.domains.BlogPostImageStatus;
import eu.com.cwsfe.cms.db.domains.BlogPostStatus;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostImage;
import eu.com.cwsfe.cms.model.CmsAuthor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, BlogPostImagesDAO.class, BlogPostsDAO.class, CmsAuthorsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class BlogPostImagesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostImagesRepository dao;

    @Autowired
    private BlogPostsRepository postsDao;

    @Autowired
    private CmsAuthorsRepository authorsDao;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final BlogPost BLOG_POST = new BlogPost();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        BLOG_POST.setPostAuthorId(CMS_AUTHOR.getId());
        BLOG_POST.setPostTextCode("post text code");
        BLOG_POST.setStatus(BlogPostStatus.NEW);
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
        List<BlogPostImage> blogPostImages = dao.searchByAjaxWithoutContent(0, 1, 1L);

        //then
        assertNotNull(blogPostImages);
    }

    @Test
    public void testSearchByAjaxCountWithoutContent() throws Exception {
        //given
        //when
        int results = dao.searchByAjaxCountWithoutContent(1L);

        //then
        assertNotNull(results);
        assertTrue(results >= 0);
    }

    @Test
    public void testListForPost() throws Exception {
        //given
        //when
        List<BlogPostImage> blogPostImages = dao.listForPost(1L);

        //then
        assertNotNull(blogPostImages);
    }

    @Test
    public void testGetWithContent() throws Exception {
        //given
        String title = "title";
        Date created = new Date();
        Date lastModified = new Date();
        String fileName = "fileName";
        long fileSize = 1L;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(BlogPostImageStatus.NEW);
        blogPostImage.setCreated(created);
        blogPostImage.setLastModified(lastModified);
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
        assertEquals(BlogPostImageStatus.NEW, result.getStatus());
        assertEquals(created, result.getCreated());
        assertEquals(lastModified, result.getLastModified());
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
        Date created = new Date();
        Date lastModified = new Date();
        String fileName = "fileName";
        long fileSize = 1L;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(BlogPostImageStatus.NEW);
        blogPostImage.setCreated(created);
        blogPostImage.setLastModified(lastModified);
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
        assertEquals(BlogPostImageStatus.NEW, result.getStatus());
        assertEquals(created, result.getCreated());
        assertEquals(lastModified, result.getLastModified());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileSize, (long) result.getFileSize());
        assertEquals(width, (int) result.getWidth());
        assertEquals(height, (int) result.getHeight());
        assertEquals(mimeType, result.getMimeType());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String title = "title";
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1L;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(BlogPostImageStatus.NEW);
        blogPostImage.setCreated(created);
        blogPostImage.setLastModified(created);
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
        assertEquals("Post image should be deleted", BlogPostImageStatus.DELETED, result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String title = "title";
        Date created = new Date();
        String fileName = "fileName";
        long fileSize = 1L;
        int width = 2;
        int height = 3;
        String mimeType = "img/png";
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setBlogPostId(BLOG_POST.getId());
        blogPostImage.setTitle(title);
        blogPostImage.setStatus(BlogPostImageStatus.NEW);
        blogPostImage.setCreated(created);
        blogPostImage.setLastModified(created);
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
        assertEquals("Post image should undeleted", BlogPostImageStatus.NEW, result.getStatus());
    }
}
