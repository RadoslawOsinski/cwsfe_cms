package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostCode;
import eu.com.cwsfe.cms.model.CmsAuthor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=true)
@ContextConfiguration(locations={"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class BlogPostCodesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostCodesDAO dao;

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
    public void testSearchByAjax() throws Exception {
        //given
        int iDisplayStart = 0;
        int iDisplayLength = 1;
        long postId = 1l;

        //when
        List<BlogPostCode> blogPostCodes = dao.searchByAjax(iDisplayStart, iDisplayLength, postId);

        //then
        assertNotNull(blogPostCodes);
    }

    @Test
    public void testSearchByAjaxCount() throws Exception {
        //given
        long postId = 1l;

        //when
        int result = dao.searchByAjaxCount(postId);

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Test
    public void testGetCodeForPost() throws Exception {
        //given
        String code = "some code text";
        String codeId = "testId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(BLOG_POST.getId());
        blogPostCode.setCode(code);
        dao.add(blogPostCode);

        //when
        BlogPostCode result = dao.getCodeForPostByCodeId(BLOG_POST.getId(), codeId);

        //then
        assertNotNull(result);
        assertNotNull(result.getCodeId());
        assertEquals("N", result.getStatus());
        assertEquals(code, result.getCode());
        assertEquals(BLOG_POST.getId(), result.getBlogPostId());
    }

    @Test
    public void testGetCodeForPostByCodeId() throws Exception {
        //given
        String code = "some code text";
        String codeId = "testId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(BLOG_POST.getId());
        blogPostCode.setCode(code);
        dao.add(blogPostCode);

        //when
        BlogPostCode result = dao.getCodeForPostByCodeId(BLOG_POST.getId(), codeId);

        //then
        assertNotNull(result);
        assertNotNull(result.getCodeId());
        assertEquals("N", result.getStatus());
        assertEquals(code, result.getCode());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String code = "some code text";
        String codeId = "testId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(BLOG_POST.getId());
        blogPostCode.setCode(code);

        //when
        dao.add(blogPostCode);

        //then
        BlogPostCode result = dao.getCodeForPostByCodeId(BLOG_POST.getId(), codeId);
        assertNotNull(result);
        assertNotNull(result.getCodeId());
        assertEquals("N", result.getStatus());
        assertEquals(code, result.getCode());
        assertEquals(BLOG_POST.getId(), result.getBlogPostId());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String code = "some code text";
        String newCode = "some new codeText";
        String codeId = "testId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(BLOG_POST.getId());
        blogPostCode.setCode(code);
        dao.add(blogPostCode);
        blogPostCode.setCode(newCode);

        //when
        dao.update(blogPostCode);

        //then
        BlogPostCode result = dao.getCodeForPostByCodeId(BLOG_POST.getId(), codeId);
        assertNotNull(result);
        assertNotNull(result.getCodeId());
        assertEquals("N", result.getStatus());
        assertEquals(newCode, result.getCode());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String code = "some code text";
        String codeId = "testId";
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(codeId);
        blogPostCode.setBlogPostId(BLOG_POST.getId());
        blogPostCode.setCode(code);
        dao.add(blogPostCode);

        //when
        dao.delete(blogPostCode);

        //then
        BlogPostCode codeForPost = null;
        try {
            codeForPost = dao.getCodeForPostByCodeId(BLOG_POST.getId(), codeId);
        } catch (EmptyResultDataAccessException e) {
            assertNotNull("Code for post should not exists", e);
        }
        assertNull("Code for post should not exists", codeForPost);
    }
}