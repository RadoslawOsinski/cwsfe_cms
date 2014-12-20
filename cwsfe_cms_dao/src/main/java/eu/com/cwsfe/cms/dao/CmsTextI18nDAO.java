package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsTextI18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class CmsTextI18nDAO {

    private static final Logger LOGGER = LogManager.getLogger(CmsTextI18nDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_TEXT_I18N";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsTextI18n> list() {
        String query =
                "SELECT " +
                        " id, lang_id, i18n_category, i18n_key, i18n_text" +
                        " FROM CMS_TEXT_I18N " +
                        " ORDER BY i18n_key, i18n_category";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsTextI18n(resultSet));
    }

    private CmsTextI18n mapCmsTextI18n(ResultSet resultSet) throws SQLException {
        CmsTextI18n cmsTextI18n = new CmsTextI18n();
        cmsTextI18n.setId(resultSet.getLong("ID"));
        cmsTextI18n.setLangId(resultSet.getLong("LANG_ID"));
        cmsTextI18n.setI18nCategory(resultSet.getLong("I18N_CATEGORY"));
        cmsTextI18n.setI18nKey(resultSet.getString("I18N_KEY"));
        cmsTextI18n.setI18nText(resultSet.getString("I18N_TEXT"));
        return cmsTextI18n;
    }

    public List<CmsTextI18n> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, lang_id, i18n_category, i18n_key, i18n_text" +
                        " FROM CMS_TEXT_I18N " +
                        " ORDER BY i18n_key, i18n_category" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsTextI18n(resultSet));
    }

    @Cacheable(value="cmsTextI18nById")
    public CmsTextI18n get(Long id) {
        String query =
                "SELECT " +
                        " id, lang_id, i18n_category, i18n_key, i18n_text" +
                        " FROM CMS_TEXT_I18N " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsTextI18n(resultSet));
    }

    public String findTranslation(String language2LetterCode, String category, String key) {
        String query =
                "SELECT " +
                        " i18n_text" +
                        " FROM CMS_TEXT_I18N " +
                        "WHERE " +
                        " lang_id IN (SELECT id FROM cms_languages WHERE code = ?) AND" +
                        " i18n_category IN (SELECT ID FROM cms_text_i18n_categories WHERE category = ?) AND i18n_key = ?";
        Object[] dbParams = new Object[3];
        dbParams[0] = language2LetterCode;
        dbParams[1] = category;
        dbParams[2] = key;
        try {
            return jdbcTemplate.queryForObject(query, dbParams, String.class);
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return null;
    }

    public Long add(CmsTextI18n cmsTextI18n) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_TEXT_I18N_S')", Long.class);
        Object[] dbParams = new Object[5];
        dbParams[0] = id;
        dbParams[1] = cmsTextI18n.getLangId();
        dbParams[2] = cmsTextI18n.getI18nCategory();
        dbParams[3] = cmsTextI18n.getI18nKey();
        dbParams[4] = cmsTextI18n.getI18nText();
        jdbcTemplate.update(
                "INSERT INTO CMS_TEXT_I18N(id, lang_id, i18n_category, i18n_key, i18n_text) VALUES (?, ?, ?, ?, ?)",
                dbParams
        );
        return id;
    }

    @CacheEvict(value = {"cmsTextI18nById"})
    public void update(CmsTextI18n cmsTextI18n) {
        Object[] dbParams = new Object[5];
        dbParams[0] = cmsTextI18n.getLangId();
        dbParams[1] = cmsTextI18n.getI18nCategory();
        dbParams[2] = cmsTextI18n.getI18nKey();
        dbParams[3] = cmsTextI18n.getI18nText();
        dbParams[4] = cmsTextI18n.getId();
        jdbcTemplate.update(
                "UPDATE CMS_TEXT_I18N SET lang_id = ?, i18n_category = ?, i18n_key = ?, i18n_text = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsTextI18nById"})
    public void delete(CmsTextI18n cmsTextI18n) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsTextI18n.getId();
        jdbcTemplate.update(
                "DELETE FROM CMS_TEXT_I18N WHERE id = ?", dbParams
        );
    }

}
