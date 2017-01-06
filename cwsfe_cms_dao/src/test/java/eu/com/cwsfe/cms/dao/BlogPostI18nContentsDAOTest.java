package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.domains.BlogPostI18nContentStatus;
import eu.com.cwsfe.cms.domains.BlogPostStatus;
import eu.com.cwsfe.cms.model.BlogPost;
import eu.com.cwsfe.cms.model.BlogPostI18nContent;
import eu.com.cwsfe.cms.model.CmsAuthor;
import eu.com.cwsfe.cms.model.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class,
    BlogPostI18nContentsDAO.class, BlogPostsDAO.class, CmsAuthorsDAO.class, CmsLanguagesDAO.class
})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class BlogPostI18nContentsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogPostI18nContentsDAO dao;

    @Autowired
    private BlogPostsDAO postsDao;

    @Autowired
    private CmsAuthorsDAO authorsDao;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    private static final CmsAuthor CMS_AUTHOR = new CmsAuthor();
    private static final BlogPost BLOG_POST = new BlogPost();
    private static final Language LANGUAGE_EN = new Language();

    @Before
    public void setUp() throws Exception {
        CMS_AUTHOR.setFirstName("firstName");
        CMS_AUTHOR.setLastName("lastName");
        CMS_AUTHOR.setId(authorsDao.add(CMS_AUTHOR));

        BLOG_POST.setPostAuthorId(CMS_AUTHOR.getId());
        BLOG_POST.setPostTextCode("post text code");
        BLOG_POST.setStatus(BlogPostStatus.NEW);
        BLOG_POST.setId(postsDao.add(BLOG_POST));

        LANGUAGE_EN.setId(cmsLanguagesDAO.getByCode("en").getId());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        Long addedId = dao.add(blogPostI18nContent);

        //when
        BlogPostI18nContent result = dao.get(addedId);

        //then
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getPostId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getPostTitle());
        assertEquals(shortcut, result.getPostShortcut());
        assertEquals(description, result.getPostDescription());
    }

    @Test
    public void testGetByLanguageForPost() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        dao.add(blogPostI18nContent);

        //when
        BlogPostI18nContent result = dao.getByLanguageForPost(BLOG_POST.getId(), LANGUAGE_EN.getId());

        //then
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getPostId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getPostTitle());
        assertEquals(shortcut, result.getPostShortcut());
        assertEquals(description, result.getPostDescription());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);

        //when
        Long addedId = dao.add(blogPostI18nContent);

        //then
        BlogPostI18nContent result = dao.get(addedId);
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getPostId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(title, result.getPostTitle());
        assertEquals(shortcut, result.getPostShortcut());
        assertEquals(description, result.getPostDescription());
    }

    @Test
    public void testUpdateContentWithStatus() throws Exception {
        //given
        String title = "title";
        String newTitle = "new title";
        String shortcut = "shortcut";
        String newShortcut = "new shortcut";
        String description = "description";
        String newDescription = "new description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setId(dao.add(blogPostI18nContent));
        blogPostI18nContent.setPostTitle(newTitle);
        blogPostI18nContent.setPostShortcut(newShortcut);
        blogPostI18nContent.setPostDescription(newDescription);
        blogPostI18nContent.setStatus(BlogPostI18nContentStatus.DELETED);

        //when
        dao.updateContentWithStatus(blogPostI18nContent);

        //then
        BlogPostI18nContent result = dao.get(blogPostI18nContent.getId());
        assertNotNull(result);
        assertEquals(BLOG_POST.getId(), result.getPostId());
        assertEquals(LANGUAGE_EN.getId(), result.getLanguageId());
        assertEquals(newTitle, result.getPostTitle());
        assertEquals(newShortcut, result.getPostShortcut());
        assertEquals(newDescription, result.getPostDescription());
        assertEquals(BlogPostI18nContentStatus.DELETED, result.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setId(dao.add(blogPostI18nContent));

        //when
        dao.delete(blogPostI18nContent);

        //then
        BlogPostI18nContent result = dao.get(blogPostI18nContent.getId());
        assertNotNull("Post i18n should exists", result);
        assertEquals("Post i18n should be deleted", BlogPostI18nContentStatus.DELETED, result.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setId(dao.add(blogPostI18nContent));

        //when
        dao.undelete(blogPostI18nContent);

        //then
        BlogPostI18nContent result = dao.get(blogPostI18nContent.getId());
        assertNotNull("Post i18n should exists", result);
        assertEquals("Post i18n should be hidden", BlogPostI18nContentStatus.HIDDEN, result.getStatus());
    }

    @Test
    public void testPublish() throws Exception {
        //given
        String title = "title";
        String shortcut = "shortcut";
        String description = "description";
        BlogPostI18nContent blogPostI18nContent = new BlogPostI18nContent();
        blogPostI18nContent.setPostId(BLOG_POST.getId());
        blogPostI18nContent.setLanguageId(LANGUAGE_EN.getId());
        blogPostI18nContent.setPostTitle(title);
        blogPostI18nContent.setPostShortcut(shortcut);
        blogPostI18nContent.setPostDescription(description);
        blogPostI18nContent.setId(dao.add(blogPostI18nContent));

        //when
        dao.publish(blogPostI18nContent);

        //then
        BlogPostI18nContent result = dao.get(blogPostI18nContent.getId());
        assertNotNull("Post i18n should exists", result);
        assertEquals("Post i18n should be published", BlogPostI18nContentStatus.PUBLISHED, result.getStatus());
    }
}
