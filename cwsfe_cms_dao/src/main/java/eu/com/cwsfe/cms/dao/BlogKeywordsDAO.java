package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogKeywordsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countForAjax() {
        String query = "SELECT count(id) FROM blog_keywords WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<BlogKeyword> list() {
        String query =
                "SELECT" +
                        "  id, keyword_name, status" +
                        "  FROM BLOG_KEYWORDS" +
                        "  WHERE" +
                        "  status = 'N'" +
                        " ORDER BY keyword_name";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapBlogKeyword(resultSet));
    }

    private BlogKeyword mapBlogKeyword(ResultSet resultSet) throws SQLException {
        BlogKeyword blogKeyword = new BlogKeyword();
        blogKeyword.setId(resultSet.getLong("id"));
        blogKeyword.setKeywordName(resultSet.getString("keyword_name"));
        blogKeyword.setStatus(resultSet.getString("status"));
        return blogKeyword;
    }

    public List<BlogKeyword> listAjax(int offset, int limit) {
        Object[] dbParams = new Object[2];
        dbParams[0] = limit;
        dbParams[1] = offset;
        String query =
                "SELECT" +
                        "  id, keyword_name, status" +
                        "  FROM BLOG_KEYWORDS" +
                        "  WHERE" +
                        "  status = 'N'" +
                        " ORDER BY keyword_name" +
                        " LIMIT ? OFFSET ? ";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapBlogKeyword(resultSet));
    }

    @Cacheable(value="blogKeywordById")
    public BlogKeyword get(Long id) {
        String query =
                "SELECT " +
                        "id, keyword_name, status " +
                        "FROM BLOG_KEYWORDS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogKeyword(resultSet));
    }

    public Long add(BlogKeyword blogKeyword) {
        Object[] dbParams = new Object[2];
        Long id = jdbcTemplate.queryForObject("SELECT nextval('BLOG_KEYWORDS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = blogKeyword.getKeywordName();
        jdbcTemplate.update("INSERT INTO BLOG_KEYWORDS(id, keyword_name, status) VALUES (?, ?, 'N')", dbParams);
        return id;
    }

    @CacheEvict(value = {"blogKeywordById"})
    public void update(BlogKeyword blogKeyword) {
        Object[] dbParams = new Object[2];
        dbParams[0] = blogKeyword.getKeywordName();
        dbParams[1] = blogKeyword.getId();
        jdbcTemplate.update("UPDATE BLOG_KEYWORDS SET keyword_name = ? WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"blogKeywordById"})
    public void delete(BlogKeyword blogKeyword) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogKeyword.getId();
        jdbcTemplate.update("UPDATE BLOG_KEYWORDS SET status = 'D' WHERE id = ?", dbParams);
    }

    @CacheEvict(value = {"blogKeywordById"})
    public void undelete(BlogKeyword blogKeyword) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogKeyword.getId();
        jdbcTemplate.update("UPDATE BLOG_KEYWORDS SET status = 'N' WHERE id = ?", dbParams);
    }

}
