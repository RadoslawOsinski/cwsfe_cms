package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsNewsI18nContent;
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
public class CmsNewsI18nContentsDAO {

    private static final Logger LOGGER = LogManager.getLogger(CmsNewsI18nContentsDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CmsNewsI18nContent> list() {
        String query =
                "SELECT " +
                        "id, news_id, language_id, news_title, news_shortcut, news_description, status " +
                        "FROM CMS_NEWS_I18N_CONTENTS " +
                        "WHERE status <> 'D' " +
                        "ORDER BY news_id";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsNewsI18nContent(resultSet));
    }

    private CmsNewsI18nContent mapCmsNewsI18nContent(ResultSet resultSet) throws SQLException {
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setId(resultSet.getLong("ID"));
        cmsNewsI18nContent.setNewsId(resultSet.getLong("NEWS_ID"));
        cmsNewsI18nContent.setLanguageId(resultSet.getLong("LANGUAGE_ID"));
        cmsNewsI18nContent.setNewsTitle(resultSet.getString("NEWS_TITLE"));
        cmsNewsI18nContent.setNewsShortcut(resultSet.getString("NEWS_SHORTCUT"));
        cmsNewsI18nContent.setNewsDescription(resultSet.getString("NEWS_DESCRIPTION"));
        cmsNewsI18nContent.setStatus(resultSet.getString("STATUS"));
        return cmsNewsI18nContent;
    }

    public List<CmsNewsI18nContent> listForNews(Long newsId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsId;
        String query =
                "SELECT " +
                        "id, news_id, language_id, news_title, news_shortcut, news_description, status " +
                        "FROM CMS_NEWS_I18N_CONTENTS " +
                        "WHERE status <> 'D' AND news_id = ?" +
                        "ORDER BY language_id, news_title";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsI18nContent(resultSet));
    }

    private Object rowSetToObject(ResultSet resultSet) throws SQLException {
        CmsNewsI18nContent cmsNewsI18nContent = new CmsNewsI18nContent();
        cmsNewsI18nContent.setId(resultSet.getLong("ID"));
        cmsNewsI18nContent.setNewsId(resultSet.getLong("NEWS_ID"));
        cmsNewsI18nContent.setLanguageId(resultSet.getLong("LANGUAGE_ID"));
        cmsNewsI18nContent.setNewsTitle(resultSet.getString("NEWS_TITLE"));
        cmsNewsI18nContent.setNewsShortcut(resultSet.getString("NEWS_SHORTCUT"));
        cmsNewsI18nContent.setNewsDescription(resultSet.getString("NEWS_DESCRIPTION"));
        cmsNewsI18nContent.setStatus(resultSet.getString("STATUS"));
        return cmsNewsI18nContent;
    }

    @Cacheable(value="cmsNewsI18nContentById")
    public CmsNewsI18nContent get(Long id) {
        String query =
                "SELECT " +
                        "id, news_id, language_id, news_title, news_shortcut, news_description, status " +
                        "FROM CMS_NEWS_I18N_CONTENTS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsI18nContent(resultSet));
    }

    @Cacheable(value="cmsNewsI18nContentByLanguageForNews")
    public CmsNewsI18nContent getByLanguageForNews(Long newsId, Long languageId) {
        String query =
                "SELECT " +
                        "id, news_id, language_id, news_title, news_shortcut, news_description, status " +
                        "FROM CMS_NEWS_I18N_CONTENTS " +
                        "WHERE news_id = ? AND language_id = ?";
        Object[] dbParams = new Object[2];
        dbParams[0] = newsId;
        dbParams[1] = languageId;
        CmsNewsI18nContent cmsNewsI18nContent = null;
        try {
            cmsNewsI18nContent = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNewsI18nContent(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return cmsNewsI18nContent;
    }

    public Long add(CmsNewsI18nContent cmsNewsI18nContent) {
        Object[] dbParams = new Object[3];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_NEWS_I18N_CONTENTS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsNewsI18nContent.getNewsId();
        dbParams[2] = cmsNewsI18nContent.getLanguageId();
        jdbcTemplate.update("INSERT INTO CMS_NEWS_I18N_CONTENTS(id, news_id, language_id, news_title, news_shortcut, news_description, status)" +
                " VALUES (?, ?, ?, '', '', '', 'H')", dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsNewsI18nContentById", "cmsNewsI18nContentByLanguageForNews"}, allEntries = true)
    public void updateContentWithStatus(CmsNewsI18nContent cmsNewsI18nContent) {
        Object[] dbParams = new Object[5];
        dbParams[0] = cmsNewsI18nContent.getNewsTitle();
        dbParams[1] = cmsNewsI18nContent.getNewsShortcut();
        dbParams[2] = cmsNewsI18nContent.getNewsDescription();
        dbParams[3] = cmsNewsI18nContent.getStatus();
        dbParams[4] = cmsNewsI18nContent.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_I18N_CONTENTS SET news_title = ?, news_shortcut = ?, news_description = ?, status = ? WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsI18nContentById", "cmsNewsI18nContentByLanguageForNews"}, allEntries = true)
    public void delete(CmsNewsI18nContent cmsNewsI18nContent) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsNewsI18nContent.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_I18N_CONTENTS SET status = 'D' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsI18nContentById", "cmsNewsI18nContentByLanguageForNews"}, allEntries = true)
    public void undelete(CmsNewsI18nContent cmsNewsI18nContent) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsNewsI18nContent.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_I18N_CONTENTS SET status = 'H' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsI18nContentById", "cmsNewsI18nContentByLanguageForNews"}, allEntries = true)
    public void publish(CmsNewsI18nContent cmsNewsI18nContent) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsNewsI18nContent.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_I18N_CONTENTS SET status = 'P' WHERE id = ?", dbParams);
    }

}
