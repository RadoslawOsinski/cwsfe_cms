package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.CmsNewsImage;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CmsNewsImagesDAO {

    private static final Logger LOGGER = LogManager.getLogger(CmsNewsImagesDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalNumberNotDeleted() {
        String query = "SELECT count(*) FROM CMS_NEWS_IMAGES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int countForAjax() {
        String query = "SELECT count(*) FROM CMS_NEWS_IMAGES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<CmsNewsImage> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long postId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = postId;
        dbParams[1] = iDisplayLength;
        dbParams[2] = iDisplayStart;
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM CMS_NEWS_IMAGES" +
                        " WHERE status <> 'D' AND news_id = ?" +
                        " ORDER BY created DESC" +
                        " LIMIT ? OFFSET ?";
        List<CmsNewsImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, false));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    private CmsNewsImage mapCmsNewsImage(ResultSet resultSet, boolean withContent) throws SQLException {
        CmsNewsImage cmsNewsImage = new CmsNewsImage();
        cmsNewsImage.setId(resultSet.getLong("ID"));
        cmsNewsImage.setNewsId(resultSet.getLong("NEWS_ID"));
        cmsNewsImage.setTitle(resultSet.getString("TITLE"));
        cmsNewsImage.setFileName(resultSet.getString("FILE_NAME"));
        cmsNewsImage.setFileSize(resultSet.getLong("FILE_SIZE"));
        cmsNewsImage.setWidth(resultSet.getInt("WIDTH"));
        cmsNewsImage.setHeight(resultSet.getInt("HEIGHT"));
        cmsNewsImage.setMimeType(resultSet.getString("MIME_TYPE"));
        cmsNewsImage.setCreated(resultSet.getDate("CREATED"));
        cmsNewsImage.setStatus(resultSet.getString("STATUS"));
        if (withContent) {
            cmsNewsImage.setContent(resultSet.getBytes("CONTENT"));
        }
        return cmsNewsImage;
    }

    public int searchByAjaxCountWithoutContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT count(*) FROM (" +
                        "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM CMS_NEWS_IMAGES" +
                        " WHERE status <> 'D' AND news_id = ?" +
                        " ORDER BY created DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<CmsNewsImage> listForPostWithoutContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM CMS_NEWS_IMAGES" +
                        " WHERE status <> 'D' AND news_id = ?" +
                        " ORDER BY created DESC";
        List<CmsNewsImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, false));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    public List<CmsNewsImage> listForPostWithContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM CMS_NEWS_IMAGES" +
                        " WHERE status <> 'D' AND news_id = ?" +
                        " ORDER BY created DESC";
        List<CmsNewsImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    public List<CmsNewsImage> listWithContent() {
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM CMS_NEWS_IMAGES" +
                        " WHERE status <> 'D'" +
                        " ORDER BY created DESC";
        List<CmsNewsImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "]", e);
        }
        return blogPostImages;
    }

    @Cacheable(value="cmsNewsImageWithContentById")
    public CmsNewsImage getWithContent(Long id) {
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM CMS_NEWS_IMAGES " +
                        " WHERE id = ? ";
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsImage(resultSet, true));
    }

    public Long add(CmsNewsImage cmsNewsImage) {
        Object[] dbParams = new Object[10];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_NEWS_IMAGES_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsNewsImage.getNewsId();
        dbParams[2] = cmsNewsImage.getTitle();
        dbParams[3] = cmsNewsImage.getFileName();
        dbParams[4] = cmsNewsImage.getFileSize();
        dbParams[5] = cmsNewsImage.getWidth();
        dbParams[6] = cmsNewsImage.getHeight();
        dbParams[7] = cmsNewsImage.getMimeType();
        dbParams[8] = cmsNewsImage.getContent();
        dbParams[9] = cmsNewsImage.getCreated();
        jdbcTemplate.update("INSERT INTO CMS_NEWS_IMAGES(id, news_id, title, file_name, file_size, width, height," +
                "mime_type, content, created, status)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N')", dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void update(CmsNewsImage cmsNewsImage) {
        Object[] dbParams = new Object[10];
        dbParams[0] = cmsNewsImage.getNewsId();
        dbParams[1] = cmsNewsImage.getTitle();
        dbParams[2] = cmsNewsImage.getFileName();
        dbParams[3] = cmsNewsImage.getFileSize();
        dbParams[4] = cmsNewsImage.getWidth();
        dbParams[5] = cmsNewsImage.getHeight();
        dbParams[6] = cmsNewsImage.getMimeType();
        dbParams[7] = cmsNewsImage.getContent();
        dbParams[8] = cmsNewsImage.getCreated();
        dbParams[9] = cmsNewsImage.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET" +
                " news_id = ?, title = ?, file_name = ?, file_size = ?, width = ?, height = ?," +
                " mime_type = ?, content = ?, created = ?" +
                " WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void delete(CmsNewsImage cmsNewsImage) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsNewsImage.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET status = 'D' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void undelete(CmsNewsImage cmsNewsImage) {
        Object[] dbParams = new Object[1];
        dbParams[0] = cmsNewsImage.getId();
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET status = 'N' WHERE id = ?", dbParams);
    }


    /////////////////////////// SPECIAL METHODS:
    public List<CmsNewsImage> listImagesForNewsWithoutThumbnails(Long newsId) {
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height," +
                        " mime_type, content, created, status" +
                        " FROM CMS_NEWS_IMAGES " +
                        "WHERE news_id = ? AND status = 'N' AND title NOT LIKE 'thumbnail_%'";
        Object[] dbParams = new Object[1];
        dbParams[0] = newsId;
        List<CmsNewsImage> cmsNewsImages = new ArrayList<>(0);
        try {
            cmsNewsImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return cmsNewsImages;
    }

    public CmsNewsImage getThumbnailForNews(Long newsId) {
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height," +
                        " mime_type, content, created, status" +
                        " FROM CMS_NEWS_IMAGES " +
                        "WHERE news_id = ? AND status = 'N' AND title LIKE 'thumbnail_%'";
        Object[] dbParams = new Object[1];
        dbParams[0] = newsId;

        CmsNewsImage cmsNewsImage = null;
        try {
            cmsNewsImage = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return cmsNewsImage;
    }

    @Cacheable(value="cmsNewsImageById")
    public CmsNewsImage get(Long id) {
        String query =
                "SELECT " +
                        " id, news_id, title, file_name, file_size, width, height," +
                        " mime_type, content, created, status" +
                        " FROM CMS_NEWS_IMAGES " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        CmsNewsImage cmsNewsImage = null;
        try {
            cmsNewsImage = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                    mapCmsNewsImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return cmsNewsImage;
    }

}
