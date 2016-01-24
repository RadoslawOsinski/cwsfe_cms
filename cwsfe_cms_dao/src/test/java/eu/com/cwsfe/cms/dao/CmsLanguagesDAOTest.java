package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.LanguageStatus;
import eu.com.cwsfe.cms.model.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(locations = {"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsLanguagesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsLanguagesDAO dao;

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
    public void testListAll() throws Exception {
        //given
        //when
        List<Language> list = dao.listAll();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        language.setCode("pl");
        language.setName("Polish");
        dao.add(language);

        //when
        List<Language> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testListForDropList() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        language.setCode("pl");
        String name = "Polish";
        language.setName(name);
        dao.add(language);

        //when
        List<Language> results = dao.listForDropList(name, 1);

        //then
        assertNotNull(results);
        assertEquals("Page limit was set to 1", 1, results.size());
    }

    @Test
    public void testGetById() throws Exception {
        //given
        Language language = new Language();
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        language.setId(dao.add(language));

        //when
        Language languageResult = dao.getById(language.getId());

        //then
        assertNotNull(languageResult);
        assertEquals((long) language.getId(), (long) languageResult.getId());
        assertEquals(code, languageResult.getCode());
        assertEquals(name, languageResult.getName());
    }

    @Test
    public void testGetByCode() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        dao.add(language);

        //when
        Language languageResult = dao.getByCode(language.getCode());

        //then
        assertNotNull(languageResult);
        assertEquals(code, languageResult.getCode());
        assertEquals(name, languageResult.getName());
    }

    @Test
    public void testGetByCodeIgnoreCase() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        dao.add(language);

        //when
        Language languageResult = dao.getByCodeIgnoreCase(language.getCode().toUpperCase());

        //then
        assertNotNull(languageResult);
        assertEquals(code, languageResult.getCode());
        assertEquals(name, languageResult.getName());
    }

    @Test
    public void testAdd() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);

        //when
        dao.add(language);

        //then
        Language languageResult = dao.getByCodeIgnoreCase(language.getCode().toUpperCase());
        assertNotNull(languageResult);
        assertEquals(code, languageResult.getCode());
        assertEquals(name, languageResult.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        language.setId(dao.add(language));
        String newCode = "fr";
        String newName = "French";
        language.setCode(newCode);
        language.setName(newName);

        //when
        dao.update(language);

        //then
        Language languageResult = dao.getById(language.getId());
        assertNotNull(languageResult);
        assertEquals((long) language.getId(), (long) languageResult.getId());
        assertEquals(newCode, languageResult.getCode());
        assertEquals(newName, languageResult.getName());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        language.setId(dao.add(language));

        //when
        dao.delete(language);

        //then
        Language languageResult = dao.getById(language.getId());
        assertNotNull(languageResult);
        assertEquals((long) language.getId(), (long) languageResult.getId());
        assertEquals("Unexpected status value for deleted object", LanguageStatus.DELETED, languageResult.getStatus());
    }

    @Test
    public void testUndelete() throws Exception {
        //given
        Language language = new Language();
        language.setId(1l);
        String code = "pl";
        String name = "Polish";
        language.setCode(code);
        language.setName(name);
        language.setId(dao.add(language));
        dao.delete(language);

        //when
        dao.undelete(language);

        //then
        Language languageResult = dao.getById(language.getId());
        assertNotNull(languageResult);
        assertEquals((long) language.getId(), (long) languageResult.getId());
        assertEquals("Unexpected status value for undeleted object", LanguageStatus.NEW, languageResult.getStatus());
    }
}
