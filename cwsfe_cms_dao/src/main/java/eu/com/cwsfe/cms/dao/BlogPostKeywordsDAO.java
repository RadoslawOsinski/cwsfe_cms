package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogKeyword;
import eu.com.cwsfe.cms.model.BlogPostKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogPostKeywordsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private BlogKeyword mapBlogKeyword(ResultSet resultSet) throws SQLException {
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setId(resultSet.getLong("id"));
        blogKeyword.setKeywordName(resultSet.getString("keyword_name"));
        blogKeyword.setStatus(resultSet.getString("status"));
        return blogKeyword;
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

}
