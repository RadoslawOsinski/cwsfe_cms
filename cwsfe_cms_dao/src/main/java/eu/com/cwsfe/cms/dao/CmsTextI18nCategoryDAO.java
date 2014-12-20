package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsTextI18nCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsTextI18nCategoryDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_TEXT_I18N_CATEGORIES";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsTextI18nCategory> list() {
        String query =
                "SELECT " +
                        " id, category, status" +
                        " FROM CMS_TEXT_I18N_CATEGORIES " +
                        " ORDER BY category";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsTextI18nCategory(resultSet));
    }

    private CmsTextI18nCategory mapCmsTextI18nCategory(ResultSet resultSet) throws SQLException {
        CmsTextI18nCategory cmsTextI18nCategory = new CmsTextI18nCategory();
        cmsTextI18nCategory.setId(resultSet.getLong("ID"));
        cmsTextI18nCategory.setCategory(resultSet.getString("CATEGORY"));
        cmsTextI18nCategory.setStatus(resultSet.getString("STATUS"));
        return cmsTextI18nCategory;
    }

    public List<CmsTextI18nCategory> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        " id, category, status" +
                        " FROM CMS_TEXT_I18N_CATEGORIES " +
                        " ORDER BY category" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsTextI18nCategory(resultSet));
    }

    public List<CmsTextI18nCategory> listForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        " id, category, status" +
                        " FROM CMS_TEXT_I18N_CATEGORIES " +
                        " WHERE status = 'N' AND lower(category) LIKE lower(?)" +
                        " ORDER BY category" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsTextI18nCategory(resultSet));
    }

    @Cacheable(value="cmsTextI18nCategoryById")
    public CmsTextI18nCategory get(Long id) {
        String query =
                "SELECT " +
                        " id, category, status" +
                        " FROM CMS_TEXT_I18N_CATEGORIES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsTextI18nCategory(resultSet));
    }

    public Long add(CmsTextI18nCategory cmsTextI18nCategory) {
        final long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_TEXT_I18N_CATEGORIES_S')", Long.class);
        Object[] dbParams = new Object[3];
        dbParams[0] = id;
        dbParams[1] = cmsTextI18nCategory.getCategory();
        dbParams[2] = 'N';
        jdbcTemplate.update(
                "INSERT INTO CMS_TEXT_I18N_CATEGORIES(id, category, status) VALUES (?, ?, ?)",
                dbParams
        );
        return id;
    }

    @CacheEvict(value = {"cmsTextI18nCategoryById"})
    public void update(CmsTextI18nCategory cmsTextI18nCategory) {
        Object[] dbParams = new Object[3];
        dbParams[0] = cmsTextI18nCategory.getCategory();
        dbParams[1] = cmsTextI18nCategory.getStatus();
        dbParams[2] = cmsTextI18nCategory.getId();
        jdbcTemplate.update(
                "UPDATE CMS_TEXT_I18N_CATEGORIES SET category = ?, status = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsTextI18nCategoryById"})
    public void delete(CmsTextI18nCategory cmsTextI18nCategory) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsTextI18nCategory.getId();
        jdbcTemplate.update(
                "UPDATE CMS_TEXT_I18N_CATEGORIES SET status = 'D' WHERE id = ?", dbParams
        );
    }

    @CacheEvict(value = {"cmsTextI18nCategoryById"})
    public void undelete(CmsTextI18nCategory cmsTextI18nCategory) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsTextI18nCategory.getId();
        jdbcTemplate.update(
                "UPDATE CMS_TEXT_I18N_CATEGORIES SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
