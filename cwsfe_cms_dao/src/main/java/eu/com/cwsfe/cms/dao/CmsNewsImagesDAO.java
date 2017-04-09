package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.db.domains.CmsNewsImageStatus;
import eu.com.cwsfe.cms.model.CmsNewsImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNewsImagesDAO.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CmsNewsImagesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
                " mime_type, created, last_modified, status, url " +
                " FROM CMS_NEWS_IMAGES" +
                " WHERE status <> 'D' AND news_id = ?" +
                " ORDER BY created DESC" +
                " LIMIT ? OFFSET ?";
        List<CmsNewsImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsImage(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [{}] with params: {}", query, Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    private CmsNewsImage mapCmsNewsImage(ResultSet resultSet) throws SQLException {
        CmsNewsImage cmsNewsImage = new CmsNewsImage();
        cmsNewsImage.setId(resultSet.getLong("ID"));
        cmsNewsImage.setNewsId(resultSet.getLong("NEWS_ID"));
        cmsNewsImage.setTitle(resultSet.getString("TITLE"));
        cmsNewsImage.setFileName(resultSet.getString("FILE_NAME"));
        cmsNewsImage.setFileSize(resultSet.getLong("FILE_SIZE"));
        cmsNewsImage.setWidth(resultSet.getInt("WIDTH"));
        cmsNewsImage.setHeight(resultSet.getInt("HEIGHT"));
        cmsNewsImage.setMimeType(resultSet.getString("MIME_TYPE"));
        cmsNewsImage.setCreated(resultSet.getTimestamp("CREATED"));
        cmsNewsImage.setLastModified(resultSet.getTimestamp("last_modified"));
        cmsNewsImage.setStatus(CmsNewsImageStatus.fromCode(resultSet.getString("STATUS")));
        cmsNewsImage.setUrl(resultSet.getString("url"));
        return cmsNewsImage;
    }

    public int searchByAjaxCountWithoutContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
            "SELECT count(*) FROM (" +
                "SELECT " +
                " id, news_id, title, file_name, file_size, width, height, " +
                " mime_type, created, last_modified, status, url " +
                " FROM CMS_NEWS_IMAGES" +
                " WHERE status <> 'D' AND news_id = ?" +
                " ORDER BY created DESC" +
                ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    @Cacheable(value = "cmsNewsImageWithContentById")
    public CmsNewsImage getWithContent(Long id) {
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        String query =
            "SELECT " +
                " id, news_id, title, file_name, file_size, width, height, " +
                " mime_type, created, last_modified, status, url " +
                " FROM CMS_NEWS_IMAGES " +
                " WHERE id = ? ";
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNewsImage(resultSet));
    }

    public Long add(CmsNewsImage cmsNewsImage) {
        Object[] dbParams = new Object[11];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('CMS_NEWS_IMAGES_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = cmsNewsImage.getNewsId();
        dbParams[2] = cmsNewsImage.getTitle();
        dbParams[3] = cmsNewsImage.getFileName();
        dbParams[4] = cmsNewsImage.getFileSize();
        dbParams[5] = cmsNewsImage.getWidth();
        dbParams[6] = cmsNewsImage.getHeight();
        dbParams[7] = cmsNewsImage.getMimeType();
        dbParams[8] = cmsNewsImage.getCreated();
        dbParams[9] = cmsNewsImage.getLastModified();
        dbParams[10] = cmsNewsImage.getUrl();
        jdbcTemplate.update("INSERT INTO CMS_NEWS_IMAGES(id, news_id, title, file_name, file_size, width, height," +
            "mime_type, created, last_modified, status, url)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N', ?)", dbParams);
        return id;
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void delete(CmsNewsImage cmsNewsImage) {
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET status = 'D' WHERE id = ?", cmsNewsImage.getId());
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void undelete(CmsNewsImage cmsNewsImage) {
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET status = 'N' WHERE id = ?", cmsNewsImage.getId());
    }

    /////////////////////////// SPECIAL METHODS:
    public List<CmsNewsImage> listImagesForNewsWithoutThumbnails(Long newsId) {
        String query =
            "SELECT " +
                " id, news_id, title, file_name, file_size, width, height," +
                " mime_type, created, last_modified, status, url" +
                " FROM CMS_NEWS_IMAGES " +
                "WHERE news_id = ? AND status = 'N' AND title NOT LIKE 'thumbnail_%'";
        Object[] dbParams = new Object[1];
        dbParams[0] = newsId;
        List<CmsNewsImage> cmsNewsImages = new ArrayList<>(0);
        try {
            cmsNewsImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsImage(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [{}] with params: {}", query, Arrays.toString(dbParams), e);
        }
        return cmsNewsImages;
    }

    public CmsNewsImage getThumbnailForNews(Long newsId) {
        String query =
            "SELECT " +
                " id, news_id, title, file_name, file_size, width, height," +
                " mime_type, created, last_modified, status, url" +
                " FROM CMS_NEWS_IMAGES " +
                "WHERE news_id = ? AND status = 'N' AND title LIKE 'thumbnail_%'";
        Object[] dbParams = new Object[1];
        dbParams[0] = newsId;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNewsImage(resultSet));
    }

    @Cacheable(value = "cmsNewsImageById")
    public CmsNewsImage get(Long id) {
        String query =
            "SELECT " +
                " id, news_id, title, file_name, file_size, width, height," +
                " mime_type, created, last_modified, status, url" +
                " FROM CMS_NEWS_IMAGES " +
                "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        CmsNewsImage cmsNewsImage = null;
        try {
            cmsNewsImage = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapCmsNewsImage(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [{}] with params: {}", query, Arrays.toString(dbParams), e);
        }
        return cmsNewsImage;
    }

    @CacheEvict(value = {"cmsNewsImageWithContentById", "cmsNewsImageById"})
    public void updateUrl(CmsNewsImage cmsNewsImage) {
        jdbcTemplate.update("UPDATE CMS_NEWS_IMAGES SET URL = ? WHERE id = ?", cmsNewsImage.getUrl(), cmsNewsImage.getId());
    }
}
