package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.BlogPostStatus;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.CmsAuthor;
import eu.com.cwsfe.cms.model.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class BlogPostsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostsDAO dao;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final Language LANGUAGE_EN = new Language();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());
    }

    @Test
    public void testListArchiveStatisticsByLanguageId() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listArchiveStatistics(1l);

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListArchiveStatistics() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listArchiveStatistics();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListForPageWithPaging() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listForPageWithPaging(1l, 1, 1);

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListCountForPageWithPaging() throws Exception {
        //given
        //when
        long result = dao.listCountForPageWithPaging(1l);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testListForPageWithCategoryAndPaging() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listForPageWithCategoryAndPaging(1l, 1l, 1, 1);

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListCountForPageWithCategoryAndPaging() throws Exception {
        //given
        //when
        long result = dao.listCountForPageWithCategoryAndPaging(1l, 1l);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testListForPageWithSearchTextAndPaging() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listForPageWithSearchTextAndPaging("", 1l, 1, 1);

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListCountForPageWithSearchTextAndPaging() throws Exception {
        //given
        //when
        long result = dao.listCountForPageWithSearchTextAndPaging("", 1l);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testListForPageWithArchiveDateAndPaging() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listForPageWithArchiveDateAndPaging(new Date(), new Date(), 1l, 1, 1);

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListCountForPageWithArchiveDateAndPaging() throws Exception {
        //given
        //when
        long result = dao.listCountForPageWithArchiveDateAndPaging(new Date(), new Date(), 1l);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
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
    public void testSearchByAjax() throws Exception {
        //given
        //when
        List<Object[]> list = dao.searchByAjax(0, 1, 1, "");

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        //when
        long result = dao.searchByAjaxCount(1, "");

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGet() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));

        //when
        BlogPost blogPostResult = dao.get(blogPost.getId());

        //then
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals(CMS_AUTHOR.getId(), blogPostResult.getPostAuthorId());
        assertEquals(postTextCode, blogPostResult.getPostTextCode());
        assertEquals(BlogPostStatus.NEW, blogPostResult.getStatus());
    }

    @Test
    public void testGetByCode() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));

        //when
        BlogPost blogPostResult = dao.getByCode(postTextCode);

        //then
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals(CMS_AUTHOR.getId(), blogPostResult.getPostAuthorId());
        assertEquals(postTextCode, blogPostResult.getPostTextCode());
        assertEquals(BlogPostStatus.NEW, blogPostResult.getStatus());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);

        //when
        blogPost.setId(dao.add(blogPost));

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals(CMS_AUTHOR.getId(), blogPostResult.getPostAuthorId());
        assertEquals(postTextCode, blogPostResult.getPostTextCode());
        assertEquals(BlogPostStatus.NEW, blogPostResult.getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String postTextCode = "post text code";
        String newPostTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));
        blogPost.setPostTextCode(newPostTextCode);
        blogPost.setStatus(BlogPostStatus.PUBLISHED);

        //when
        dao.update(blogPost);

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals(CMS_AUTHOR.getId(), blogPostResult.getPostAuthorId());
        assertEquals(newPostTextCode, blogPostResult.getPostTextCode());
        assertEquals(BlogPostStatus.PUBLISHED, blogPostResult.getStatus());
    }

    @Test
    public void testUpdatePostBasicInfo() throws Exception {
        //given
        String postTextCode = "post text code";
        String newPostTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));
        blogPost.setPostTextCode(newPostTextCode);
        blogPost.setStatus(BlogPostStatus.PUBLISHED);

        //when
        dao.updatePostBasicInfo(blogPost);

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals(CMS_AUTHOR.getId(), blogPostResult.getPostAuthorId());
        assertEquals(newPostTextCode, blogPostResult.getPostTextCode());
        assertEquals(BlogPostStatus.PUBLISHED, blogPostResult.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));

        //when
        dao.delete(blogPost);

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals("Unexpected status value for deleted object", BlogPostStatus.DELETED, blogPostResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));
        dao.delete(blogPost);

        //when
        dao.undelete(blogPost);

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals("Unexpected status value for undeleted object", BlogPostStatus.HIDDEN, blogPostResult.getStatus());
    }

    @Test
    public void testPublish() throws Exception {
        //given
        String postTextCode = "post text code";
        BlogPost blogPost = new BlogPost();
        blogPost.setPostAuthorId(CMS_AUTHOR.getId());
        blogPost.setPostTextCode(postTextCode);
        blogPost.setStatus(BlogPostStatus.NEW);
        blogPost.setId(dao.add(blogPost));
        dao.delete(blogPost);

        //when
        dao.publish(blogPost);

        //then
        BlogPost blogPostResult = dao.get(blogPost.getId());
        assertNotNull(blogPostResult);
        assertEquals((long) blogPost.getId(), (long) blogPostResult.getId());
        assertEquals("Unexpected status value for undeleted object", BlogPostStatus.PUBLISHED, blogPostResult.getStatus());
    }

    @Test
    public void testListI18nPosts() throws Exception {
        //given
        //when
        List<Object[]> list = dao.listI18nPosts(1l);

        //then
        assertNotNull("Empty results should not return null", list);
    }
}