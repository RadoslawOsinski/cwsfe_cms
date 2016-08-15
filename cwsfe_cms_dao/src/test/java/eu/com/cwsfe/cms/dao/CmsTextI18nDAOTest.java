package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.DaoTestsConfiguration;
import eu.com.cwsfe.cms.model.CmsTextI18n;
import eu.com.cwsfe.cms.model.CmsTextI18nCategory;
import eu.com.cwsfe.cms.model.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@ContextConfiguration(classes = {DaoTestsConfiguration.class, CmsTextI18nDAO.class, CmsLanguagesDAO.class, CmsTextI18nCategoryDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration-tests-local"})
public class CmsTextI18nDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsTextI18nDAO dao;

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;

    @Autowired
    private CmsTextI18nCategoryDAO cmsTextI18nCategoryDAO;

    private static final Language LANGUAGE_EN = new Language();
    private static final CmsTextI18nCategory CATEGORY = new CmsTextI18nCategory();
    private static final CmsTextI18nCategory CATEGORY2 = new CmsTextI18nCategory();

    @Before
    public void setUp() throws Exception {
        CATEGORY.setCategory("category");
        CATEGORY.setId(cmsTextI18nCategoryDAO.add(CATEGORY));
        CATEGORY2.setCategory("category2");
        CATEGORY2.setId(cmsTextI18nCategoryDAO.add(CATEGORY2));

        Language en = cmsLanguagesDAO.getByCode("en");
        LANGUAGE_EN.setId(en.getId());
        LANGUAGE_EN.setCode(en.getCode());
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
    public void testList() throws Exception {
        //given
        //when
        List<CmsTextI18n> list = dao.list();

        //then
        assertNotNull("Empty results should not return null", list);
    }

    @Test
    public void testListAjax() throws Exception {
        //given
        String key = "key";
        String text = "text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());
        dao.add(cmsTextI18n);

        //when
        List<CmsTextI18n> list = dao.listAjax(0, 1);

        //then
        assertNotNull("Empty results should not return null", list);
        assertEquals("Page limit was set to 1", 1, list.size());
    }

    @Test
    public void testGet() throws Exception {
        //given
        String key = "key";
        String text = "text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());
        cmsTextI18n.setId(dao.add(cmsTextI18n));

        //when
        CmsTextI18n cmsTextI18nResult = dao.get(cmsTextI18n.getId());

        //then
        assertNotNull(cmsTextI18nResult);
        assertEquals((long) cmsTextI18n.getId(), (long) cmsTextI18nResult.getId());
        assertEquals(key, cmsTextI18nResult.getI18nKey());
        assertEquals(text, cmsTextI18nResult.getI18nText());
        assertEquals((long) CATEGORY.getId(), (long) cmsTextI18nResult.getI18nCategory());
        assertEquals((long) LANGUAGE_EN.getId(), (long) cmsTextI18nResult.getLangId());
    }

    @Test
    public void testFindTranslation() throws Exception {
        //given
        String key = "key";
        String text = "text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());
        cmsTextI18n.setId(dao.add(cmsTextI18n));

        //when
        String translation = dao.findTranslation(LANGUAGE_EN.getCode(), CATEGORY.getCategory(), key);

        //then
        assertNotNull("Translation should be found", translation);
    }

    @Test
    public void testAdd() throws Exception {
        //given
        String key = "key";
        String text = "text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());

        //when
        cmsTextI18n.setId(dao.add(cmsTextI18n));

        //then
        CmsTextI18n cmsTextI18nResult = dao.get(cmsTextI18n.getId());
        assertNotNull(cmsTextI18nResult);
        assertEquals((long) cmsTextI18n.getId(), (long) cmsTextI18nResult.getId());
        assertEquals(key, cmsTextI18nResult.getI18nKey());
        assertEquals(text, cmsTextI18nResult.getI18nText());
        assertEquals((long) CATEGORY.getId(), (long) cmsTextI18nResult.getI18nCategory());
        assertEquals((long) LANGUAGE_EN.getId(), (long) cmsTextI18nResult.getLangId());
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        String key = "key";
        String newKey = "new key";
        String text = "text";
        String newText = "new text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());
        cmsTextI18n.setId(dao.add(cmsTextI18n));
        cmsTextI18n.setI18nKey(newKey);
        cmsTextI18n.setI18nText(newText);
        cmsTextI18n.setI18nCategory(CATEGORY2.getId());

        //when
        dao.update(cmsTextI18n);

        //then
        CmsTextI18n cmsTextI18nResult = dao.get(cmsTextI18n.getId());
        assertNotNull(cmsTextI18nResult);
        assertEquals((long) cmsTextI18n.getId(), (long) cmsTextI18nResult.getId());
        assertEquals(newKey, cmsTextI18nResult.getI18nKey());
        assertEquals(newText, cmsTextI18nResult.getI18nText());
        assertEquals((long) CATEGORY2.getId(), (long) cmsTextI18nResult.getI18nCategory());
        assertEquals((long) LANGUAGE_EN.getId(), (long) cmsTextI18nResult.getLangId());
    }

    @Test
    public void testDelete() throws Exception {
        //given
        String key = "key";
        String text = "text";
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setI18nKey(key);
        cmsTextI18n.setI18nText(text);
        cmsTextI18n.setI18nCategory(CATEGORY.getId());
        cmsTextI18n.setLangId(LANGUAGE_EN.getId());
        cmsTextI18n.setId(dao.add(cmsTextI18n));

        //when
        dao.delete(cmsTextI18n);

        //then
        CmsTextI18n cmsTextI18nResult = null;
        try {
            cmsTextI18nResult = dao.get(cmsTextI18n.getId());
        } catch (EmptyResultDataAccessException e) {
            assertNotNull("I18n should not exist", e);
        }
        assertNull("I18n should not exist", cmsTextI18nResult);
    }

}