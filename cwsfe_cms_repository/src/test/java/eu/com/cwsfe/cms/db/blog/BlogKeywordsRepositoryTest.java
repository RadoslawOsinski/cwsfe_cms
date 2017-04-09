package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.RepositoriesTestsConfiguration;
import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
@ContextConfiguration(classes = {RepositoriesTestsConfiguration.class, BlogKeywordsRepository.class, BlogKeywordsEntity.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
class BlogKeywordsRepositoryTest {

    @Autowired
    private BlogKeywordsRepository repository;

    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testCountForAjax() {
        //given
        //when
        Long result = repository.countForAjax();

        //then
        assertNotNull("query should return non negative value", result);
        assertTrue("query should return non negative value", result >= 0);
    }

    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testEmptyList() {
        //given
        //when
        List<BlogKeywordsEntity> list = repository.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertTrue(list.isEmpty());
    }

    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testNotEmptyList() {
        //given
        //when
        List<BlogKeywordsEntity> list = repository.list();

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
    }

    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testListAjax() {
        //given
        //when
        List<BlogKeywordsEntity> list = repository.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertFalse("List should not be empty", list.isEmpty());
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-before-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/eu/com/cwsfe/cms/db/blog/blog-keyword-after-test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testGet() {
        //given
        String expectedKeywordName = "test1";
        Long addedId = 100000L;

        //when
        BlogKeywordsEntity blogKeywordResult = repository.get(addedId);

        //then
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(expectedKeywordName, blogKeywordResult.getKeywordName());
        assertEquals(NewDeletedStatus.NEW, blogKeywordResult.getStatus());
    }

    @Test
    void testAdd() {
        //given
        String keywordName = "test";
        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
        blogKeyword.setKeywordName(keywordName);

        //when
        Long addedId = repository.add(blogKeyword);

        //then
        BlogKeywordsEntity blogKeywordResult = repository.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName, blogKeywordResult.getKeywordName());
        assertEquals(NewDeletedStatus.NEW, blogKeywordResult.getStatus());
    }

    @Test
    void testUpdate() {
        //given
        String keywordName1 = "test";
        String keywordName2 = "test2";
        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        Long addedId = repository.add(blogKeyword);
        blogKeyword.setId(addedId);
        blogKeyword.setKeywordName(keywordName2);

        //when
        repository.update(blogKeyword);

        //then
        BlogKeywordsEntity blogKeywordResult = repository.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName2, blogKeywordResult.getKeywordName());
    }

    @Test
    void testDelete() {
        //given
        String keywordName1 = "test";
        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        Long addedId = repository.add(blogKeyword);
        blogKeyword.setId(addedId);

        //when
        repository.delete(blogKeyword);

        //then
        BlogKeywordsEntity blogKeywordResult = repository.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName1, blogKeywordResult.getKeywordName());
        assertEquals("Unexpected status value for deleted object", NewDeletedStatus.DELETED, blogKeywordResult.getStatus());
    }

    @Test
    void testUndelete() {
        //given
        String keywordName1 = "test";
        BlogKeywordsEntity blogKeyword = new BlogKeywordsEntity();
        blogKeyword.setKeywordName(keywordName1);
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        Long addedId = repository.add(blogKeyword);
        blogKeyword.setId(addedId);
        repository.delete(blogKeyword);

        //when
        repository.undelete(blogKeyword);

        //then
        BlogKeywordsEntity blogKeywordResult = repository.get(addedId);
        assertNotNull(blogKeywordResult);
        assertEquals((long) addedId, (long) blogKeywordResult.getId());
        assertEquals(keywordName1, blogKeywordResult.getKeywordName());
        assertEquals("Unexpected status for undeleted object", NewDeletedStatus.NEW, blogKeywordResult.getStatus());
    }
}
