package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.domains.BlogKeywordStatus;
import eu.com.cwsfe.cms.model.BlogKeyword;
import eu.com.cwsfe.cms.model.BlogKeywordAssignment;
import eu.com.cwsfe.cms.model.BlogPostKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogPostKeywordsDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogPostKeywordsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BlogKeyword> listForPost(Long postId) {
        String query =
                "select" +
                        "  bk.id, bk.keyword_name, bk.status" +
                        "  from BLOG_KEYWORDS bk, BLOG_POST_KEYWORDS bkp" +
                        "  WHERE " +
                        "  bkp.blog_keyword_id = bk.id and bkp.blog_post_id = ? and" +
                        "  bk.status = 'N'" +
                        " ORDER BY bk.keyword_name";
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapBlogKeyword(resultSet));
    }

    public List<BlogKeywordAssignment> listValuesForPost(long postId) {
        String query =
                "select" +
                        "  bk.id, bk.keyword_name, bk.status, (bkp.blog_post_id = ?) as assignedKeyword " +
                        "  from BLOG_KEYWORDS bk LEFT JOIN BLOG_POST_KEYWORDS bkp" +
                        " on bk.id = bkp.blog_keyword_id " +
                        "  WHERE " +
                        "  bk.status = 'N'" +
                        " ORDER BY bk.keyword_name";
        Object[] dbParams = new Object[1];
        dbParams[0] = postId;
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) ->
                mapBlogKeywordAssignment(resultSet));
    }

    private BlogKeyword mapBlogKeyword(ResultSet resultSet) throws SQLException {
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setId(resultSet.getLong("id"));
        blogKeyword.setKeywordName(resultSet.getString("keyword_name"));
        blogKeyword.setStatus(BlogKeywordStatus.fromCode(resultSet.getString("status")));
        return blogKeyword;
    }

    private BlogKeywordAssignment mapBlogKeywordAssignment(ResultSet resultSet) throws SQLException {
        BlogKeywordAssignment blogKeywordAssignment = new BlogKeywordAssignment();
        blogKeywordAssignment.setId(resultSet.getLong("id"));
        blogKeywordAssignment.setKeywordName(resultSet.getString("keyword_name"));
        blogKeywordAssignment.setStatus(BlogKeywordStatus.fromCode(resultSet.getString("status")));
        blogKeywordAssignment.setAssigned(resultSet.getBoolean("assignedKeyword"));
        return blogKeywordAssignment;
    }

    public BlogPostKeyword get(Long blogPostId, Long blogKeywordId) {
        String query = "select blog_post_id, blog_keyword_id from BLOG_POST_KEYWORDS WHERE blog_post_id = ? and blog_keyword_id = ?";
        Object[] dbParams = new Object[2];
        dbParams[0] = blogPostId;
        dbParams[1] = blogKeywordId;
        return jdbcTemplate.queryForObject(query, dbParams,
                (resultSet, rowNum) -> new BlogPostKeyword(resultSet.getLong("blog_post_id"), resultSet.getLong("blog_keyword_id"))
        );
    }

    public void add(BlogPostKeyword blogPostKeyword) {
        Object[] dbParams = new Object[2];
        dbParams[0] = blogPostKeyword.getBlogPostId();
        dbParams[1] = blogPostKeyword.getBlogKeywordId();
        jdbcTemplate.update("INSERT INTO BLOG_POST_KEYWORDS(blog_post_id, blog_keyword_id) VALUES(?, ?)", dbParams);
    }

    public void deleteForPost(BlogPostKeyword blogPostKeyword) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostKeyword.getBlogPostId();
        jdbcTemplate.update("delete from BLOG_POST_KEYWORDS where blog_post_id = ?", dbParams);
    }

    public void deleteForPost(Long id) {
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        jdbcTemplate.update("delete from BLOG_POST_KEYWORDS where blog_post_id = ?", dbParams);
    }

    public void delete(long postId, Long blogKeywordId) {
        Object[] dbParams = new Object[2];
        dbParams[0] = postId;
        dbParams[1] = blogKeywordId;
        jdbcTemplate.update("delete from BLOG_POST_KEYWORDS where blog_post_id = ? and blog_keyword_id = ?", dbParams);
    }
}
