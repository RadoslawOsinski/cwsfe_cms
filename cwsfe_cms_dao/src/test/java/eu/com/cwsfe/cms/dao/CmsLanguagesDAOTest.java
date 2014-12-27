package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=true)
@ContextConfiguration(locations={"classpath:cwsfe-cms-dao-test.xml", "classpath:cwsfe-cms-cache-test.xml"})
public class CmsLanguagesDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CmsLanguagesDAO cmsLanguagesDAO;
    @Autowired
    private EhCacheCacheManager cacheManager;

    @Test
    public void testGetByIdOnExistingLang() throws Exception {
        //given
        Language lang = new Language();
        lang.setCode("pl");
        lang.setName("PL");
        cmsLanguagesDAO.add(lang);

        //when
        lang = cmsLanguagesDAO.getById(1l);

        //then
        assertNotNull(lang);
        assertNotNull(lang.getCode());
    }

//    @Test
//    public void testGetByIdWithCache() throws Exception {
//        long elementId = 1l;
//        Lang lang = cmsLanguagesDAO.getById(elementId);
//        assertNotNull(lang);
//        assertNotNull(lang.getCode());
//        Cache cache = cacheManager.getCache("cmsLanguagesById");
//        Cache.ValueWrapper valueWrapper = cache.get(elementId);
//        assertNotNull(valueWrapper);
//        assertNotNull(valueWrapper.get());
//        assertEquals(lang, valueWrapper.get());
//    }
//
//    @Test
//    public void testGetByCode() throws Exception {
//        Lang lang = cmsLanguagesDAO.getByCode("pl");
//        assertNotNull(lang);
//        assertEquals(lang.getCode(), "pl");
//    }
//
//    @Test
//    public void testGetByCodeWithCache() throws Exception {
//        String code = "pl";
//        Lang lang = cmsLanguagesDAO.getByCode(code);
//        assertNotNull(lang);
//        assertNotNull(lang.getId());
//        Cache cache = cacheManager.getCache("cmsLanguagesByCode");
//        Cache.ValueWrapper valueWrapper = cache.get(code);
//        assertNotNull(valueWrapper);
//        assertNotNull(valueWrapper.get());
//        assertEquals(lang, valueWrapper.get());
//    }
//
//    @Test
//    public void testGetByCodeIgnoreCase() throws Exception {
//        Lang lang = cmsLanguagesDAO.getByCode("pl");
//        assertNotNull(lang);
//        assertEquals(lang.getCode(), "pl");
//    }
//
//    @Test
//    public void testGetByCodeIgnoreCaseWithCache() throws Exception {
//        String code = "pl";
//        Lang lang = cmsLanguagesDAO.getByCodeIgnoreCase(code);
//        assertNotNull(lang);
//        assertNotNull(lang.getId());
//        Cache cache = cacheManager.getCache("cmsLanguagesByCodeIgnoreCase");
//        Cache.ValueWrapper valueWrapper = cache.get(code);
//        assertNotNull(valueWrapper);
//        assertNotNull(valueWrapper.get());
//        assertEquals(lang, valueWrapper.get());
//    }
//
//    @Test
//    public void testAdd() throws Exception {
//        String code = "xx";
//        Lang lang = new Lang();
//        lang.setCode(code);
//        lang.setName("XXXX");
//        lang.setId(cmsLanguagesDAO.add(lang));
//        assertNotNull(lang);
//        assertNotNull(lang.getId());
//        assertEquals(code, lang.getCode());
//    }
//
//    @Test
//    public void testAddWithCache() throws Exception {
//        String code = "xx";
//        Lang lang = new Lang();
//        lang.setCode(code);
//        lang.setName("XXXX");
//        lang.setId(cmsLanguagesDAO.add(lang));
//        assertNotNull(lang);
//        assertNotNull(lang.getId());
//        assertEquals(code, lang.getCode());
//
//        Lang langAfterAdd = cmsLanguagesDAO.getByCode(code);
//        assertNotNull(langAfterAdd);
//        assertNotNull(langAfterAdd.getId());
//
//        Cache cache = cacheManager.getCache("cmsLanguagesByCode");
//        Cache.ValueWrapper valueWrapper = cache.get(code);
//        assertNotNull(valueWrapper);
//        assertNotNull(valueWrapper.get());
//        assertEquals(langAfterAdd, valueWrapper.get());
//        assertEquals(code, langAfterAdd.getCode());
//    }
//
//    @Test
//    public void testDeleteWithCache() throws Exception {
//        String code = "yy";
//        Lang lang = new Lang();
//        lang.setCode(code);
//        lang.setName("YYYY");
//        lang.setId(cmsLanguagesDAO.add(lang));
//
//        cmsLanguagesDAO.delete(lang);
//
//        Lang langAfterDelete = cmsLanguagesDAO.getByCode(lang.getCode());
//        assertNotNull(langAfterDelete);
//        assertEquals("D", langAfterDelete.getStatus());
//        Cache cache = cacheManager.getCache("cmsLanguagesByCode");
//        Cache.ValueWrapper valueWrapper = cache.get(code);
//        assertNotNull(valueWrapper);
//        assertEquals(langAfterDelete, valueWrapper.get());
//    }

}
