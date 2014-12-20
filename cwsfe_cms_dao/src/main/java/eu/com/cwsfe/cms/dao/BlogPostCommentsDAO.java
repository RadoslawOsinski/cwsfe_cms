package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPostComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BlogPostCommentsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getTotalNumberNotDeleted() {
        String query = "SELECT count(*) FROM CMS_BLOG_POST_COMMENTS WHERE status <> 'D'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<BlogPostComment> listPublishedForPostI18nContent(Long blogPostI18nContentId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostI18nContentId;
        String query =
                "select" +
                        "  id, parent_comment_id, blog_post_i18n_content_id, comment, user_name, email, status, created" +
                        "  from CMS_BLOG_POST_COMMENTS" +
                        "  WHERE" +
                        "  status = 'P' and blog_post_i18n_content_id = ?" +
                        " ORDER BY created";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapBlogPostComment(resultSet));
    }


    private BlogPostComment mapBlogPostComment(ResultSet resultSet) throws SQLException {
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setId(resultSet.getLong("id"));
        blogPostComment.setParentCommentId(resultSet.getLong("parent_comment_id"));
        blogPostComment.setBlogPostI18nContentId(resultSet.getLong("blog_post_i18n_content_id"));
        blogPostComment.setComment(resultSet.getString("comment"));
        blogPostComment.setUsername(resultSet.getString("user_name"));
        blogPostComment.setEmail(resultSet.getString("email"));
        blogPostComment.setStatus(resultSet.getString("status"));
        blogPostComment.setCreated(resultSet.getDate("created"));
        return blogPostComment;
    }

    public List<BlogPostComment> searchByAjax(
            int iDisplayStart, int iDisplayLength
    ) {
        int numberOfSearchParams = 0;
        List<Object> additionalParams = new ArrayList<>(5);
        numberOfSearchParams++;
        additionalParams.add(iDisplayLength);
        numberOfSearchParams++;
        additionalParams.add(iDisplayStart);
        Object[] dbParams = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParams[i] = additionalParams.get(i);
        }
        String query =
                "select" +
                        "  id, parent_comment_id, blog_post_i18n_content_id, comment, user_name, email, status, created" +
                        "  from CMS_BLOG_POST_COMMENTS" +
                        " ORDER BY created desc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams, (resultSet, rowNum) -> mapBlogPostComment(resultSet));
    }

    public int searchByAjaxCount() {
        String query =
                "select count(*) from (" +
                        "select" +
                        "  id, parent_comment_id, blog_post_i18n_content_id, comment, user_name, email, status, created" +
                        "  from CMS_BLOG_POST_COMMENTS" +
                        " ORDER BY created desc" +
                        ") as results";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public Long add(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[8];
        Long id = jdbcTemplate.queryForObject("select nextval('CMS_BLOG_POST_COMMENTS_S')", Long.class);
        if (blogPostComment.getCreated() == null) {
            blogPostComment.setCreated(new Date());
        }
        dbParams[0] = id;
        dbParams[1] = blogPostComment.getParentCommentId();
        dbParams[2] = blogPostComment.getBlogPostI18nContentId();
        dbParams[3] = blogPostComment.getComment();
        dbParams[4] = blogPostComment.getUsername();
        dbParams[5] = blogPostComment.getEmail();
        dbParams[6] = "N";
        dbParams[7] = blogPostComment.getCreated();
        jdbcTemplate.update("INSERT INTO CMS_BLOG_POST_COMMENTS(" +
                "id, parent_comment_id, blog_post_i18n_content_id, comment, user_name, email, status, created" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)", dbParams);
        return id;
    }

    public void update(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[2];
        dbParams[0] = blogPostComment.getComment();
        dbParams[1] = blogPostComment.getId();
        jdbcTemplate.update("UPDATE CMS_BLOG_POST_COMMENTS SET comment = ? WHERE id = ?", dbParams);
    }

    public void delete(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostComment.getId();
        jdbcTemplate.update("update CMS_BLOG_POST_COMMENTS set status = 'D' where id = ?", dbParams);
    }

    public void undelete(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostComment.getId();
        jdbcTemplate.update("update CMS_BLOG_POST_COMMENTS set status = 'N' where id = ?", dbParams);
    }

    public void publish(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostComment.getId();
        jdbcTemplate.update("update CMS_BLOG_POST_COMMENTS set status = 'P' where id = ?", dbParams);
    }

    public void block(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostComment.getId();
        jdbcTemplate.update("update CMS_BLOG_POST_COMMENTS set status = 'B' where id = ?", dbParams);
    }

    public void markAsSpam(BlogPostComment blogPostComment) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPostComment.getId();
        jdbcTemplate.update("update CMS_BLOG_POST_COMMENTS set status = 'S' where id = ?", dbParams);
    }

}
