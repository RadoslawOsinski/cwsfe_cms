package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsLanguagesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(*) from CMS_LANGUAGES";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private static final String COLUMNS = "id, code, name, status ";

    public List<Language> listAll() {
        String query =
                "SELECT " +
                        COLUMNS +
                        "FROM CMS_LANGUAGES " +
                        " where status = 'N'" +
                        "order by code";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapLang(resultSet));
    }

    private Language mapLang(ResultSet resultSet) throws SQLException {
        Language lang = new Language();
        lang.setId(resultSet.getLong("ID"));
        lang.setCode(resultSet.getString("CODE"));
        lang.setName(resultSet.getString("NAME"));
        lang.setStatus(resultSet.getString("STATUS"));
        return lang;
    }

    public List<Language> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        COLUMNS +
                        "FROM CMS_LANGUAGES " +
                        " where status = 'N'" +
                        " order by code" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapLang(resultSet));
    }

    public List<Language> listForDropList(String term, int limit) {
        Object[] dbParams = new Object[3];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = '%' + term + '%';
        dbParams[2] = limit;
        String query =
                "SELECT " +
                        COLUMNS +
                        " FROM CMS_LANGUAGES " +
                        " where status = 'N' and (lower(code) like lower(?) or lower(name) like lower(?)) " +
                        " order by code, name" +
                        " limit ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapLang(resultSet));
    }

    @Cacheable(value = "cmsLanguagesById")
    public Language getById(Long id) {
        String query =
                "SELECT " + COLUMNS + "FROM CMS_LANGUAGES WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapLang(resultSet));
    }

    @Cacheable(value = "cmsLanguagesByCode")
    public Language getByCode(String code) {
        String query =
                "SELECT " + COLUMNS + "FROM CMS_LANGUAGES WHERE code = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = code;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapLang(resultSet));
    }

    @Cacheable(value = "cmsLanguagesByCodeIgnoreCase")
    public Language getByCodeIgnoreCase(String code) {
        String query =
                "SELECT " + COLUMNS + "FROM CMS_LANGUAGES WHERE lower(code) = lower(?)";
        Object[] dbParams = new Object[1];
        dbParams[0] = code;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapLang(resultSet));
    }

    public Long add(Language lang) {
        Object[] dbParams = new Object[3];
        Long id = jdbcTemplate.queryForObject("select nextval('CMS_LANGUAGES_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = lang.getCode();
        dbParams[2] = lang.getName();
        jdbcTemplate.update(
                "INSERT INTO CMS_LANGUAGES(id, code, name, status) VALUES (?, ?, ?, 'N')",
                dbParams
        );
        return id;
    }

    @CacheEvict(value = {"cmsLanguagesById", "cmsLanguagesByCode", "cmsLanguagesByCodeIgnoreCase"})
    public void update(Language lang) {
        Object[] dbParams = new Object[3];
        dbParams[0] = lang.getCode();
        dbParams[1] = lang.getName();
        dbParams[2] = lang.getId();
        jdbcTemplate.update(
                "UPDATE CMS_LANGUAGES SET code = ?, name = ? WHERE id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsLanguagesById", "cmsLanguagesByCode", "cmsLanguagesByCodeIgnoreCase"})
    public void delete(Language lang) {
        Object[] dbParams = new Object[1];
        dbParams[0] = lang.getId();
        jdbcTemplate.update(
                "update CMS_LANGUAGES set status = 'D' where id = ?",
                dbParams
        );
    }

    @CacheEvict(value = {"cmsLanguagesById", "cmsLanguagesByCode", "cmsLanguagesByCodeIgnoreCase"})
    public void undelete(Language lang) {
        Object[] dbParams = new Object[1];
        dbParams[0] = lang.getId();
        jdbcTemplate.update(
                "update CMS_LANGUAGES set status = 'N' where id = ?",
                dbParams
        );
    }

}
