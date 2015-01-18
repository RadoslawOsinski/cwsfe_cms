package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.*;
import eu.com.cwsfe.cms.model.BlogPostComment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class BlogPostCommentsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostCommentsDAO dao;

    @Autowired
    private BlogPostsDAO postsDao;

    @Autowired
    private BlogPostI18nContentsDAO blogPostI18nContentsDAO;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final BlogPost BLOG_POST = new BlogPost();
    private static final BlogPostI18nContent BLOG_POST_I18N_CONTENT = new BlogPostI18nContent();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        BLOG_POST.setPostAuthorId(CMS_AUTHOR.getId());
        BLOG_POST.setPostTextCode("post text code");
        BLOG_POST.setStatus("N");
        BLOG_POST.setId(postsDao.add(BLOG_POST));

        BLOG_POST_I18N_CONTENT.setPostId(BLOG_POST.getId());
        BLOG_POST_I18N_CONTENT.setStatus("N");
        BLOG_POST_I18N_CONTENT.setPostDescription("description");
        BLOG_POST_I18N_CONTENT.setPostShortcut("shortcut");
        BLOG_POST_I18N_CONTENT.setPostTitle("title");
        BLOG_POST_I18N_CONTENT.setLanguageId(cmsLanguagesDAO.getByCode("en").getId());
        BLOG_POST_I18N_CONTENT.setId(blogPostI18nContentsDAO.add(BLOG_POST_I18N_CONTENT));
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
    public void testListPublishedForPostI18nContent() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));
        dao.publish(blogPostComment);

        //when
        List<BlogPostComment> blogPostComments = dao.listPublishedForPostI18nContent(BLOG_POST_I18N_CONTENT.getId());

        //then
        assertNotNull(blogPostComments);
        assertFalse("There should be only on published comment", blogPostComments.isEmpty());
        assertEquals("There should be only on published comment", 1, blogPostComments.size());
    }

    @Test
    public void testSearchByAjax() throws Exception {
        //given
        int iDisplayStart = 0;
        int iDisplayLength = 1;

        //when
        List<BlogPostComment> blogPostComments = dao.searchByAjax(iDisplayStart, iDisplayLength);

        //then
        assertNotNull(blogPostComments);
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        //when
        int result = dao.searchByAjaxCount();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGet() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        Long addedId = dao.add(blogPostComment);

        //when
        BlogPostComment result = dao.get(addedId);

        //then
        assertNotNull(result);
        assertEquals(comment, result.getComment());
        assertEquals(created, result.getCreated());
        assertEquals(email, result.getEmail());
        assertEquals(status, result.getStatus());
        assertEquals(userName, result.getUsername());
        assertEquals(BLOG_POST_I18N_CONTENT.getId(), result.getBlogPostI18nContentId());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());

        //when
        Long addedId = dao.add(blogPostComment);

        //then
        BlogPostComment result = dao.get(addedId);
        assertNotNull(result);
        assertEquals(comment, result.getComment());
        assertEquals(created, result.getCreated());
        assertEquals(email, result.getEmail());
        assertEquals(status, result.getStatus());
        assertEquals(userName, result.getUsername());
        assertEquals(BLOG_POST_I18N_CONTENT.getId(), result.getBlogPostI18nContentId());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String comment = "comment text";
        String newComment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));
        blogPostComment.setComment(newComment);
        //when
        dao.update(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull(result);
        assertEquals(newComment, result.getComment());
        assertEquals(created, result.getCreated());
        assertEquals(email, result.getEmail());
        assertEquals(status, result.getStatus());
        assertEquals(userName, result.getUsername());
        assertEquals(BLOG_POST_I18N_CONTENT.getId(), result.getBlogPostI18nContentId());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));

        //when
        dao.delete(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull("Post comment should exists", result);
        assertEquals("Post status should be deleted", "D", result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));

        //when
        dao.undelete(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull("Post comment should exists", result);
        assertEquals("Post status should be new", "N", result.getStatus());
    }

    @Test
    public void testPublish() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));

        //when
        dao.publish(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull("Post comment should exists", result);
        assertEquals("Post status should be published", "P", result.getStatus());
    }

    @Test
    public void testBlock() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));

        //when
        dao.block(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull("Post comment should exists", result);
        assertEquals("Post status should be blocked", "B", result.getStatus());
    }

    @Test
    public void testMarkAsSpam() throws Exception {
        //given
        String comment = "comment text";
        Date created = new Date();
        String email = "test@test.eu";
        String status = "N";
        String userName = "testUser";
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setComment(comment);
        blogPostComment.setCreated(created);
        blogPostComment.setEmail(email);
        blogPostComment.setStatus(status);
        blogPostComment.setUsername(userName);
        blogPostComment.setBlogPostI18nContentId(BLOG_POST_I18N_CONTENT.getId());
        blogPostComment.setId(dao.add(blogPostComment));

        //when
        dao.markAsSpam(blogPostComment);

        //then
        BlogPostComment result = dao.get(blogPostComment.getId());
        assertNotNull("Post comment should exists", result);
        assertEquals("Post status should be marked as spam", "S", result.getStatus());
    }

    @Test
    public void testCountCommentsForPostI18n() throws Exception {
        //given
        //when
        int count = dao.countCommentsForPostI18n(BLOG_POST_I18N_CONTENT.getId());

        //then
        assertNotNull(count);
    }
}