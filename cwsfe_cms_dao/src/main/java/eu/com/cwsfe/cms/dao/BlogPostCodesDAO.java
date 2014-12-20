package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPostCode;
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
public class BlogPostCodesDAO {

    private static final Logger LOGGER = LogManager.getLogger(BlogPostCodesDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalNumberNotDeleted() {
        String query = "SELECT count(*) FROM BLOG_POST_CODE WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int countForAjax() {
        String query = "SELECT count(*) FROM BLOG_POST_CODE WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<BlogPostCode> searchByAjax(int iDisplayStart, int iDisplayLength, Long postId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = postId;
        dbParams[1] = iDisplayLength;
        dbParams[2] = iDisplayStart;
        String query =
                "SELECT " +
                        " code_id, blog_post_id, code, status" +
                        " FROM BLOG_POST_CODE" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY code_id DESC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapBlogPostCode(resultSet));
    }

    private BlogPostCode mapBlogPostCode(ResultSet resultSet) throws SQLException {
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(resultSet.getString("code_id"));
        blogPostCode.setBlogPostId(resultSet.getLong("blog_post_id"));
        blogPostCode.setCode(resultSet.getString("code"));
        blogPostCode.setStatus(resultSet.getString("status"));
        return blogPostCode;
    }

    public int searchByAjaxCount(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        String query =
                "SELECT count(*) FROM (" +
                        "SELECT " +
                        " code_id, blog_post_id, code, status" +
                        " FROM BLOG_POST_CODE" +
                        " WHERE status <> 'D' AND blog_post_id = ?" +
                        " ORDER BY code_id DESC" +
                        ") AS results";
        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
    }

    @Cacheable(value="blogPostCodeByPostIdAndCodeId")
    public BlogPostCode getCodeForPost(Long postId, String codeId) {
        Object[] dbParams = new Object[2];
        dbParams[0] = postId;
        dbParams[1] = codeId;
        String query =
                "SELECT " +
                        " code_id, blog_post_id, code, status" +
                        " FROM BLOG_POST_CODE " +
                        "WHERE blog_post_id = ? AND code_id = ?";
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPostCode(resultSet));
    }

    @Cacheable(value="blogPostCodeByPostIdAndCodeId")
    public BlogPostCode getCodeForPostByCodeId(Long postId, String codeId) {
        Object[] dbParams = new Object[2];
        dbParams[0] = postId;
        dbParams[1] = codeId;
        String query =
                "SELECT " +
                        " code_id, blog_post_id, code, status" +
                        " FROM BLOG_POST_CODE " +
                        "WHERE blog_post_id = ? AND code_id = ?";
        BlogPostCode blogPostCode = null;
        try {
            blogPostCode = jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPostCode(resultSet));
        } catch (DataAccessException e) {
            LOGGER.error("Problem query: [" + query + "] with params: " + Arrays.toString(dbParams), e);
        }
        return blogPostCode;
    }

    public String add(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPostCode.getCodeId();
        dbParams[1] = blogPostCode.getBlogPostId();
        dbParams[2] = blogPostCode.getCode();
        jdbcTemplate.update("INSERT INTO BLOG_POST_CODE(code_id, blog_post_id, code, status) VALUES (?, ?, ?, 'N')", dbParams);
        return blogPostCode.getCodeId();
    }

    @CacheEvict(value = {"blogPostCodeByPostIdAndCodeId"})
    public void update(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPostCode.getBlogPostId();
        dbParams[1] = blogPostCode.getCode();
        dbParams[2] = blogPostCode.getCodeId();
        jdbcTemplate.update("UPDATE BLOG_POST_CODE SET blog_post_id = ?, code = ? WHERE code_id = ?", dbParams);
    }

    @CacheEvict(value = {"blogPostCodeByPostIdAndCodeId"})
    public void delete(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[2];
        dbParams[0] = blogPostCode.getBlogPostId();
        dbParams[1] = blogPostCode.getCodeId();
        jdbcTemplate.update("DELETE FROM BLOG_POST_CODE WHERE blog_post_id = ? AND code_id = ?", dbParams);
    }

}
