package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.BlogKeywordStatus;
import eu.com.cwsfe.cms.model.BlogKeyword;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=true)
@ContextConfiguration(locations={"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class BlogKeywordsDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BlogKeywordsDAO dao;

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
    public void testEmptyList() throws Exception {
        //given
        //when
        List<BlogKeyword> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testNotEmptyList() throws Exception {
        //given
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName("test1");
        blogKeyword.setStatus(BlogKeywordStatus.NEW);
        dao.add(blogKeyword);

        //when
        List<BlogKeyword> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName("test1");
        dao.add(blogKeyword);
        BlogKeyword blogKeyword2 = new BlogKeyword();
        blogKeyword2.setKeywordName("test2");
        dao.add(blogKeyword2);

        //when
        List<BlogKeyword> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String keywordName = "test";
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName(keywordName);
        blogKeyword.setStatus(BlogKeywordStatus.NEW);
        Long addedId = dao.add(blogKeyword);

        //when
        BlogKeyword blogKeywordResult = dao.get(addedId);

        //then
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName, blogKeywordResult.getKeywordName());
        assertEquals(BlogKeywordStatus.NEW, blogKeywordResult.getStatus());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String keywordName = "test";
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName(keywordName);

        //when
        Long addedId = dao.add(blogKeyword);

        //then
        BlogKeyword blogKeywordResult = dao.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName, blogKeywordResult.getKeywordName());
        assertEquals(BlogKeywordStatus.NEW, blogKeywordResult.getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String keywordName1 = "test";
        String keywordName2 = "test2";
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(BlogKeywordStatus.NEW);
        Long addedId = dao.add(blogKeyword);
        blogKeyword.setId(addedId);
        blogKeyword.setKeywordName(keywordName2);

        //when
        dao.update(blogKeyword);

        //then
        BlogKeyword blogKeywordResult = dao.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName2, blogKeywordResult.getKeywordName());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String keywordName1 = "test";
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(BlogKeywordStatus.NEW);
        Long addedId = dao.add(blogKeyword);
        blogKeyword.setId(addedId);

        //when
        dao.delete(blogKeyword);

        //then
        BlogKeyword blogKeywordResult = dao.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName1, blogKeywordResult.getKeywordName());
        assertEquals("Unexpected status value for deleted object", BlogKeywordStatus.DELETED, blogKeywordResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        String keywordName1 = "test";
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(BlogKeywordStatus.NEW);
        Long addedId = dao.add(blogKeyword);
        blogKeyword.setId(addedId);
        dao.delete(blogKeyword);

        //when
        dao.undelete(blogKeyword);

        //then
        BlogKeyword blogKeywordResult = dao.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName1, blogKeywordResult.getKeywordName());
        assertEquals("Unexpected status for undeleted object", BlogKeywordStatus.NEW, blogKeywordResult.getStatus());
    }
}