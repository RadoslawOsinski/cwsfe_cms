package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPostImage;
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
public class BlogPostImagesDAO {

    private static final Logger LOGGER = LogManager.getLogger(BlogPostImagesDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalNumberNotDeleted() {
        String query = "SELECT count(*) FROM BLOG_POST_IMAGES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int countForAjax() {
        String query = "SELECT count(*) FROM BLOG_POST_IMAGES WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<BlogPostImage> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long postId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = postId;
        dbParams[1] = iDisplayLength;
        dbParams[2] = iDisplayStart;
        String query =
                "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM BLOG_POST_IMAGES" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY created DESC" +
                        " LIMIT ? OFFSET ?";
        List<BlogPostImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapBlogPostImage(resultSet, false));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    private BlogPostImage mapBlogPostImage(ResultSet resultSet, boolean withContent) throws SQLException {
        BlogPostImage blogPostImage = new BlogPostImage();
        blogPostImage.setId(resultSet.getLong("ID"));
        blogPostImage.setBlogPostId(resultSet.getLong("blog_post_id"));
        blogPostImage.setTitle(resultSet.getString("title"));
        blogPostImage.setFileName(resultSet.getString("file_name"));
        blogPostImage.setFileSize(resultSet.getLong("file_size"));
        blogPostImage.setWidth(resultSet.getInt("width"));
        blogPostImage.setHeight(resultSet.getInt("height"));
        blogPostImage.setMimeType(resultSet.getString("mime_type"));
        blogPostImage.setCreated(resultSet.getDate("created"));
        blogPostImage.setStatus(resultSet.getString("STATUS"));
        if (withContent) {
            blogPostImage.setContent(resultSet.getBytes("CONTENT"));
        }
        return blogPostImage;
    }

    public int searchByAjaxCountWithoutContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT count(*) FROM (" +
                        "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM BLOG_POST_IMAGES" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY created DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    public List<BlogPostImage> listForPostWithoutContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, created, status " +
                        " FROM BLOG_POST_IMAGES" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY created DESC";
        List<BlogPostImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapBlogPostImage(resultSet, false));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    public List<BlogPostImage> listForPostWithContent(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM BLOG_POST_IMAGES" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY created DESC";
        List<BlogPostImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                    mapBlogPostImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostImages;
    }

    public List<BlogPostImage> listWithContent() {
        String query =
                "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM BLOG_POST_IMAGES" +
                        " WHERE status <> 'D'" +
                        " ORDER BY created DESC";
        List<BlogPostImage> blogPostImages = new ArrayList<>(0);
        try {
            blogPostImages = jdbcTemplate.query(query, (resultSet, rowNum) ->
                    mapBlogPostImage(resultSet, true));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "]", e);
        }
        return blogPostImages;
    }

    @Cacheable(value="blogPostImageWithContentById")
    public BlogPostImage getWithContent(Long id) {
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        String query =
                "SELECT " +
                        " id, blog_post_id, title, file_name, file_size, width, height, " +
                        " mime_type, content, created, status " +
                        " FROM BLOG_POST_IMAGES " +
                        " WHERE id = ? ";
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) ->
                mapBlogPostImage(resultSet, true));
    }

    public Long add(BlogPostImage blogPostImage) {
        Object[] dbParams = new Object[10];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('BLOG_POST_IMAGES_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = blogPostImage.getBlogPostId();
        dbParams[2] = blogPostImage.getTitle();
        dbParams[3] = blogPostImage.getFileName();
        dbParams[4] = blogPostImage.getFileSize();
        dbParams[5] = blogPostImage.getWidth();
        dbParams[6] = blogPostImage.getHeight();
        dbParams[7] = blogPostImage.getMimeType();
        dbParams[8] = blogPostImage.getContent();
        dbParams[9] = blogPostImage.getCreated();
        jdbcTemplate.update("INSERT INTO BLOG_POST_IMAGES(id, blog_post_id, title, file_name, file_size, width, height," +
                " mime_type, content, created, status)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N')", dbParams);
        return id;
    }

    @CacheEvict(value = {"blogPostImageWithContentById"})
    public void update(BlogPostImage blogPostImage) {
        Object[] dbParams = new Object[10];
        dbParams[0] = blogPostImage.getBlogPostId();
        dbParams[1] = blogPostImage.getTitle();
        dbParams[2] = blogPostImage.getFileName();
        dbParams[3] = blogPostImage.getFileSize();
        dbParams[4] = blogPostImage.getWidth();
        dbParams[5] = blogPostImage.getHeight();
        dbParams[6] = blogPostImage.getMimeType();
        dbParams[7] = blogPostImage.getContent();
        dbParams[8] = blogPostImage.getCreated();
        dbParams[9] = blogPostImage.getId();
        jdbcTemplate.update("UPDATE BLOG_POST_IMAGES SET" +
                " blog_post_id = ?, title = ?, file_name = ?, file_size = ?, width = ?, height = ?, " +
                " mime_type = ?, content = ?, created = ? " +
                " WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"blogPostImageWithContentById"})
    public void delete(BlogPostImage blogPostImage) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostImage.getId();
        jdbcTemplate.update("UPDATE BLOG_POST_IMAGES SET status = 'D' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"blogPostImageWithContentById"})
    public void undelete(BlogPostImage blogPostImage) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostImage.getId();
        jdbcTemplate.update("UPDATE BLOG_POST_IMAGES SET status = 'N' WHERE id = ?", dbParams);
    }

}
