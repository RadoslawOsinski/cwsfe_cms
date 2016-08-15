package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.BlogPostCodeStatus;
import eu.com.cwsfe.cms.model.BlogPostCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogPostCodesDAO {

    private static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "SELECT count(*) FROM BLOG_POST_CODE WHERE status <> 'D'";
    private static final String COUNT_FOR_AJAX_QUERY = "SELECT count(*) FROM BLOG_POST_CODE WHERE status <> 'D'";
    private static final String SEARCH_BY_AJAX_QUERY = "SELECT " +
            " code_id, blog_post_id, code, status" +
            " FROM BLOG_POST_CODE" +
            " WHERE status <> 'D' AND blog_post_id = ?" +
            " ORDER BY code_id DESC" +
            " LIMIT ? OFFSET ?";
    private static final String SEARCH_BY_AJAX_COUNT_QUERY = "SELECT count(*) FROM (" +
            "SELECT " +
            " code_id, blog_post_id, code, status" +
            " FROM BLOG_POST_CODE" +
            " WHERE status <> 'D' AND blog_post_id = ?" +
            " ORDER BY code_id DESC" +
            ") AS results";
    private static final String CODE_FOR_POST_BY_CODE_ID_QUERY = "SELECT " +
            " code_id, blog_post_id, code, status" +
            " FROM BLOG_POST_CODE " +
            "WHERE blog_post_id = ? AND code_id = ?";
    private static final String ADD_QUERY = "INSERT INTO BLOG_POST_CODE(code_id, blog_post_id, code, status) VALUES (?, ?, ?, 'N')";
    private static final String UPDATE_QUERY = "UPDATE BLOG_POST_CODE SET blog_post_id = ?, code = ? WHERE code_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM BLOG_POST_CODE WHERE blog_post_id = ? AND code_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogPostCodesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getTotalNumberNotDeleted() {
        return jdbcTemplate.queryForObject(TOTAL_NUMBER_NOT_DELETED_QUERY, Integer.class);
    }

    public int countForAjax() {
        return jdbcTemplate.queryForObject(COUNT_FOR_AJAX_QUERY, Integer.class);
    }

    public List<BlogPostCode> searchByAjax(int iDisplayStart, int iDisplayLength, Long postId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = postId;
        dbParams[1] = iDisplayLength;
        dbParams[2] = iDisplayStart;
        return jdbcTemplate.query(SEARCH_BY_AJAX_QUERY, dbParams, (resultSet, rowNum) -> mapBlogPostCode(resultSet));
    }

    private BlogPostCode mapBlogPostCode(ResultSet resultSet) throws SQLException {
        BlogPostCode blogPostCode = new BlogPostCode();
        blogPostCode.setCodeId(resultSet.getString("code_id"));
        blogPostCode.setBlogPostId(resultSet.getLong("blog_post_id"));
        blogPostCode.setCode(resultSet.getString("code"));
        blogPostCode.setStatus(BlogPostCodeStatus.fromCode(resultSet.getString("status")));
        return blogPostCode;
    }

    public int searchByAjaxCount(Long postId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        return jdbcTemplate.queryForObject(SEARCH_BY_AJAX_COUNT_QUERY, dbParams, Integer.class);
    }

    @Cacheable(value="blogPostCodeByPostIdAndCodeId")
    public BlogPostCode getCodeForPostByCodeId(Long postId, String codeId) {
        Object[] dbParams = new Object[2];
        dbParams[0] = postId;
        dbParams[1] = codeId;
        return jdbcTemplate.queryForObject(CODE_FOR_POST_BY_CODE_ID_QUERY, dbParams, (resultSet, rowNum) -> mapBlogPostCode(resultSet));
    }

    public String add(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPostCode.getCodeId();
        dbParams[1] = blogPostCode.getBlogPostId();
        dbParams[2] = blogPostCode.getCode();
        jdbcTemplate.update(ADD_QUERY, dbParams);
        return blogPostCode.getCodeId();
    }

    @CacheEvict(value = {"blogPostCodeByPostIdAndCodeId"})
    public void update(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPostCode.getBlogPostId();
        dbParams[1] = blogPostCode.getCode();
        dbParams[2] = blogPostCode.getCodeId();
        jdbcTemplate.update(UPDATE_QUERY, dbParams);
    }

    @CacheEvict(value = {"blogPostCodeByPostIdAndCodeId"})
    public void delete(BlogPostCode blogPostCode) {
        Object[] dbParams = new Object[2];
        dbParams[0] = blogPostCode.getBlogPostId();
        dbParams[1] = blogPostCode.getCodeId();
        jdbcTemplate.update(DELETE_QUERY, dbParams);
    }

}
