package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.NewsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NewsTypesDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM CMS_NEWS_TYPES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<NewsType> list() {
        String query =
                "SELECT " +
                        "id, type, status " +
                        "FROM CMS_NEWS_TYPES " +
                        "WHERE status = 'N' " +
                        "ORDER BY type";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapNewsType(resultSet));
    }

    private NewsType mapNewsType(ResultSet resultSet) throws SQLException {
        NewsType newsType = new NewsType();
        newsType.setId(resultSet.getLong("ID"));
        newsType.setType(resultSet.getString("TYPE"));
        newsType.setStatus(resultSet.getString("STATUS"));
        return newsType;
    }

    public List<NewsType> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT " +
                        "id, type, status " +
                        "FROM CMS_NEWS_TYPES " +
                        "WHERE status = 'N' " +
                        "ORDER BY type" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsType(resultSet));
    }

    public List<NewsType> listNewsTypesForDropList(String term, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = '%' + term + '%';
        dbParams[1] = limit;
        String query =
                "SELECT " +
                        " id, type, status " +
                        " FROM CMS_NEWS_TYPES " +
                        " WHERE status = 'N' AND lower(type) LIKE lower(?) " +
                        " ORDER BY type" +
                        " LIMIT ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapNewsType(resultSet));
    }

    public NewsType get(Long id) {
        String query =
                "SELECT " +
                        "id, type, status " +
                        "FROM CMS_NEWS_TYPES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsType(resultSet));
    }

    public NewsType getByFolderName(String type) {
        String query =
                "SELECT " +
                        "id, type, status " +
                        "FROM CMS_NEWS_TYPES " +
                        "WHERE type = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = type;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapNewsType(resultSet));
    }

    public Long add(NewsType newsType) {
        Object[] dbParams = new Object[2];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_NEWS_TYPES_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = newsType.getType();
        jdbcTemplate.update(
                "INSERT INTO CMS_NEWS_TYPES(id, type, status) VALUES (?, ?, 'N')", dbParams);
        return id;
    }

    public void update(NewsType newsType) {
        Object[] dbParams = new Object[2];
        dbParams[0] = newsType.getType();
        dbParams[1] = newsType.getId();
        jdbcTemplate.update(
                "UPDATE CMS_NEWS_TYPES SET type = ? WHERE id = ?"
                , dbParams);
    }

    public void delete(NewsType newsType) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsType.getId();
        jdbcTemplate.update(
                "UPDATE CMS_NEWS_TYPES SET status = 'D' WHERE id = ?",
                dbParams
        );
    }

    public void undelete(NewsType newsType) {
        Object[] dbParams = new Object[1];
        dbParams[0] = newsType.getId();
        jdbcTemplate.update(
                "UPDATE CMS_NEWS_TYPES SET status = 'N' WHERE id = ?",
                dbParams
        );
    }

}
