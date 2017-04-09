package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.db.domains.CmsFolderStatus;
import eu.com.cwsfe.cms.model.CmsFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CmsFoldersDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CmsFoldersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countForAjax() {
        String query = "SELECT count(id) FROM cms_folders WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsFolder> list() {
        String query =
            "SELECT " +
                "id, parent_id, folder_name, order_number, status " +
                "FROM CMS_FOLDERS " +
                "WHERE status = 'N' " +
                "ORDER BY order_number";
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
            mapCmsFolder(resultSet));
    }

    private CmsFolder mapCmsFolder(ResultSet resultSet) throws SQLException {
        CmsFolder cmsFolder = new CmsFolder();
        cmsFolder.setId(resultSet.getLong("ID"));
        cmsFolder.setParentId(resultSet.getLong("PARENT_ID"));
        cmsFolder.setFolderName(resultSet.getString("FOLDER_NAME"));
        cmsFolder.setOrderNumber(resultSet.getLong("ORDER_NUMBER"));
        cmsFolder.setStatus(CmsFolderStatus.fromCode(resultSet.getString("STATUS")));
        return cmsFolder;
    }

    public List<CmsFolder> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
            "SELECT " +
                "id, parent_id, folder_name, order_number, status " +
                "FROM CMS_FOLDERS " +
                "WHERE status = 'N' " +
                "ORDER BY order_number" +
                " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
            mapCmsFolder(resultSet));
    }

    public List<CmsFolder> listFoldersForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
            "SELECT " +
                "id, parent_id, folder_name, order_number, status " +
                " FROM CMS_FOLDERS " +
                " WHERE status = 'N' AND lower(folder_name) LIKE lower(?) " +
                " ORDER BY order_number" +
                " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
            mapCmsFolder(resultSet));
    }

    @Cacheable(value = "cmsFolderById")
    public CmsFolder get(Long id) {
        String query =
            "SELECT " +
                "id, parent_id, folder_name, order_number, status " +
                "FROM CMS_FOLDERS " +
                "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
            mapCmsFolder(resultSet));
    }

    @Cacheable(value = "cmsFolderByName")
    public CmsFolder getByFolderName(String folderName) {
        String query =
            "SELECT " +
                "id, parent_id, folder_name, order_number, status " +
                "FROM CMS_FOLDERS " +
                "WHERE folder_name = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = folderName;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
            mapCmsFolder(resultSet));
    }

    public Long add(CmsFolder cmsFolder) {
        Object[] dbParams = new Object[4];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_FOLDERS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsFolder.getParentId();
        dbParams[2] = cmsFolder.getFolderName();
        dbParams[3] = cmsFolder.getOrderNumber();
        jdbcTemplate.update(
            "INSERT INTO CMS_FOLDERS(id, parent_id, folder_name, order_number, status) VALUES (?, ?, ?, ?, 'N')",
            dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsFolderById", "cmsFolderByName"})
    public void update(CmsFolder cmsFolder) {
        Object[] dbParams = new Object[4];
        dbParams[0] = cmsFolder.getParentId();
        dbParams[1] = cmsFolder.getFolderName();
        dbParams[2] = cmsFolder.getOrderNumber();
        dbParams[3] = cmsFolder.getId();
        jdbcTemplate.update("UPDATE CMS_FOLDERS SET parent_id = ?, folder_name = ?, order_number = ? WHERE id = ?"
            , dbParams);
    }

    @CacheEvict(value = {"cmsFolderById", "cmsFolderByName"})
    public void delete(CmsFolder cmsFolder) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsFolder.getId();
        jdbcTemplate.update("UPDATE CMS_FOLDERS SET status = 'D' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsFolderById", "cmsFolderByName"})
    public void undelete(CmsFolder cmsFolder) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsFolder.getId();
        jdbcTemplate.update("UPDATE CMS_FOLDERS SET status = 'N' WHERE id = ?", dbParams);
    }

}
