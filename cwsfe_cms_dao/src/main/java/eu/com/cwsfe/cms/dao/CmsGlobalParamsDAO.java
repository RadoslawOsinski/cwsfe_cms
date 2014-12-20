package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsGlobalParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsGlobalParamsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM cms_global_params";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsGlobalParam> list() {
        String query =
                "SELECT " +
                        "id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION " +
                        "FROM CMS_GLOBAL_PARAMS " +
                        "ORDER BY CODE";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                mapCmsGlobalParam(resultSet));
    }

    private CmsGlobalParam mapCmsGlobalParam(ResultSet resultSet) throws SQLException {
        CmsGlobalParam cmsGlobalParam = new CmsGlobalParam();
        cmsGlobalParam.setId(resultSet.getLong("ID"));
        cmsGlobalParam.setCode(resultSet.getString("CODE"));
        cmsGlobalParam.setDefaultValue(resultSet.getString("DEFAULT_VALUE"));
        cmsGlobalParam.setValue(resultSet.getString("VALUE"));
        cmsGlobalParam.setDescription(resultSet.getString("DESCRIPTION"));
        return cmsGlobalParam;
    }

    public List<CmsGlobalParam> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        "id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION " +
                        "FROM CMS_GLOBAL_PARAMS " +
                        "ORDER BY CODE" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsGlobalParam(resultSet));
    }

    public List<CmsGlobalParam> listFoldersForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        "id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION " +
                        " FROM CMS_GLOBAL_PARAMS " +
                        " WHERE lower(CODE) LIKE lower(?) " +
                        " ORDER BY CODE" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsGlobalParam(resultSet));
    }

    @Cacheable(value="cmsGlobalParamById")
    public CmsGlobalParam get(Long id) {
        String query =
                "SELECT " +
                        "id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION " +
                        "FROM CMS_GLOBAL_PARAMS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsGlobalParam(resultSet));
    }

    @Cacheable(value="cmsGlobalParamByCode")
    public CmsGlobalParam getByCode(String code) {
        String query =
                "SELECT " +
                        "id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION " +
                        "FROM CMS_GLOBAL_PARAMS " +
                        "WHERE CODE = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = code;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsGlobalParam(resultSet));
    }

    public Long add(CmsGlobalParam cmsFolder) {
        Object[] dbParams = new Object[5];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_GLOBAL_PARAMS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsFolder.getCode();
        dbParams[2] = cmsFolder.getDefaultValue();
        dbParams[3] = cmsFolder.getValue();
        dbParams[4] = cmsFolder.getDescription();
        jdbcTemplate.update(
                "INSERT INTO CMS_GLOBAL_PARAMS(id, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION ) VALUES (?, ?, ?, ?, ?)",
                dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsGlobalParamById", "cmsGlobalParamByCode"})
    public void update(CmsGlobalParam cmsFolder) {
        Object[] dbParams = new Object[5];
        dbParams[0] = cmsFolder.getCode();
        dbParams[1] = cmsFolder.getDefaultValue();
        dbParams[2] = cmsFolder.getValue();
        dbParams[3] = cmsFolder.getDescription();
        dbParams[4] = cmsFolder.getId();
        jdbcTemplate.update("UPDATE CMS_GLOBAL_PARAMS SET CODE = ?, DEFAULT_VALUE = ?, VALUE = ?, DESCRIPTION = ? WHERE id = ?"
                , dbParams);
    }

    @CacheEvict(value = {"cmsGlobalParamById", "cmsGlobalParamByCode"})
    public void delete(CmsGlobalParam cmsFolder) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsFolder.getId();
        jdbcTemplate.update("DELETE FROM CMS_GLOBAL_PARAMS WHERE id = ?", dbParams);
    }

}
