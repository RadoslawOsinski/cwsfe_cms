package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogKeyword;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostKeyword;
import eu.com.cwsfe.cms.model.CmsAuthor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class BlogPostKeywordsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    private BlogPostKeywordsDAO dao;

    @Autowired
    private BlogPostsDAO postsDao;

    @Autowired
    private BlogKeywordsDAO blogKeywordsDAO;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final BlogPost BLOG_POST = new BlogPost();
    private static final BlogKeyword BLOG_KEYWORD = new BlogKeyword();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        BLOG_POST.setPostAuthorId(CMS_AUTHOR.getId());
        BLOG_POST.setPostTextCode("post text code");
        BLOG_POST.setStatus("N");
        BLOG_POST.setId(postsDao.add(BLOG_POST));

        BLOG_KEYWORD.setKeywordName("test keyword");
        BLOG_KEYWORD.setId(blogKeywordsDAO.add(BLOG_KEYWORD));
    }

    @Test
    public void testListForPost() throws Exception {
        //given
        //when
        List<BlogKeyword> list = dao.listForPost(1l);

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        BlogPostKeyword blogPostKeyword = new BlogPostKeyword(BLOG_POST.getId(), BLOG_KEYWORD.getId());

        //when
        dao.add(blogPostKeyword);

        //then
        List<BlogKeyword> blogKeywords = dao.listForPost(BLOG_POST.getId());
        assertNotNull(blogKeywords);
        assertTrue("Post keywords should contain added keyword for post", blogKeywords.stream().map(BlogKeyword::getKeywordName).collect(Collectors.toList()).contains(BLOG_KEYWORD.getKeywordName()));
    }

    @Test
    public void testDeleteForPost() throws Exception {
        //given
        BlogPostKeyword blogPostKeyword = new BlogPostKeyword(BLOG_POST.getId(), BLOG_KEYWORD.getId());
        dao.add(blogPostKeyword);

        //when
        dao.deleteForPost(blogPostKeyword);

        //then
        List<BlogKeyword> blogKeywords = dao.listForPost(BLOG_POST.getId());
        assertNotNull(blogKeywords);
        assertTrue(blogKeywords.isEmpty());
    }

    @Test
    public void testDeleteForPostById() throws Exception {
        //given
        BlogPostKeyword blogPostKeyword = new BlogPostKeyword(BLOG_POST.getId(), BLOG_KEYWORD.getId());
        dao.add(blogPostKeyword);

        //when
        dao.deleteForPost(BLOG_POST.getId());

        //then
        List<BlogKeyword> blogKeywords = dao.listForPost(BLOG_POST.getId());
        assertNotNull(blogKeywords);
        assertTrue(blogKeywords.isEmpty());
    }
}