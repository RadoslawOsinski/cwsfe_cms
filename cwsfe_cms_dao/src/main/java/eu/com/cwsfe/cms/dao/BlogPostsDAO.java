package eu.com.cwsfe.cms.dao;

import eu.com.cwsfe.cms.model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BlogPostsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Object[]> listArchiveStatistics(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT" +
                        "  COUNT(bp.id), CAST(EXTRACT(YEAR FROM bp.post_creation_date) AS TEXT) AS YEAR, CAST(EXTRACT(MONTH FROM bp.post_creation_date) AS TEXT) AS MONTH" +
                        "  FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        "  WHERE" +
                        "  bp.id = bpi18n.post_id AND" +
                        "  bp.status = 'P' AND bpi18n.status = 'P' AND" +
                        "  bpi18n.language_id = ?" +
                        "  GROUP BY MONTH, YEAR" +
                        " ORDER BY YEAR DESC, MONTH DESC";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3)});
    }

    public List<Object[]> listArchiveStatistics() {
        String query =
                "SELECT" +
                        "  COUNT(bp.id), CAST(EXTRACT(YEAR FROM bp.post_creation_date) AS TEXT) AS YEAR, CAST(EXTRACT(MONTH FROM bp.post_creation_date) AS TEXT) AS MONTH" +
                        "  FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        "  WHERE" +
                        "  bp.id = bpi18n.post_id AND" +
                        "  bp.status = 'P' AND bpi18n.status = 'P'" +
                        "  GROUP BY MONTH, YEAR" +
                        " ORDER BY YEAR DESC, MONTH DESC";
        return jdbcTemplate.query(query,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3)});
    }

    public List<Object[]> listForPageWithPaging(Long languageId, Integer articlesPerPage, Integer currentPage) {
        Object[] dbParams = new Object[3];
        dbParams[0] = languageId;
        dbParams[1] = articlesPerPage;
        dbParams[2] = currentPage * articlesPerPage;
        String query =
                "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Object[] listCountForPageWithPaging(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT count(*) FROM(" +
                        "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        ")AS results";
        return jdbcTemplate.queryForObject(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getInt(1)});
    }

    public List<Object[]> listForPageWithCategoryAndPaging(Long categoryId, Long languageId, Integer articlesPerPage, Integer currentPage) {
        Object[] dbParams = new Object[4];
        dbParams[0] = categoryId;
        dbParams[1] = languageId;
        dbParams[2] = articlesPerPage;
        dbParams[3] = currentPage * articlesPerPage;
        String query =
                "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n, BLOG_POST_KEYWORDS bpk" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bp.id = bpk.blog_post_id AND" +
                        " bpk.blog_keyword_id = ? AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Object[] listCountForPageWithCategoryAndPaging(Long categoryId, Long languageId) {
        Object[] dbParams = new Object[2];
        dbParams[0] = categoryId;
        dbParams[1] = languageId;
        String query =
                "SELECT count(*) FROM(" +
                        "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n, BLOG_POST_KEYWORDS bpk" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bp.id = bpk.blog_post_id AND" +
                        " bpk.blog_keyword_id = ? AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        ")AS results";
        return jdbcTemplate.queryForObject(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getInt(1)});
    }

    public List<Object[]> listForPageWithSearchTextAndPaging(String searchText, Long languageId, Integer articlesPerPage, Integer currentPage) {
        Object[] dbParams = new Object[6];
        dbParams[0] = languageId;
        dbParams[1] = '%' + searchText + '%';
        dbParams[2] = '%' + searchText + '%';
        dbParams[3] = '%' + searchText + '%';
        dbParams[4] = articlesPerPage;
        dbParams[5] = currentPage * articlesPerPage;
        String query =
                "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'and" +
                        " (" +
                        " lower(bpi18n.post_title) like lower( ?) or" +
                        " lower(bpi18n.post_shortcut) like lower (?) or" +
                        " lower(bpi18n.post_description) like lower (?)" +
                        " )" +
                        " ORDER BY bp.post_creation_date DESC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Object[] listCountForPageWithSearchTextAndPaging(String searchText, Long languageId) {
        Object[] dbParams = new Object[4];
        dbParams[0] = languageId;
        dbParams[1] = '%' + searchText + '%';
        dbParams[2] = '%' + searchText + '%';
        dbParams[3] = '%' + searchText + '%';
        String query =
                "SELECT count(*) FROM(" +
                        "SELECT" +
                        " bp.id, bpi18n.id" +
                        " from BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P' and" +
                        "(" +
                        " lower(bpi18n.post_title)like lower(?) or" +
                        " lower(bpi18n.post_shortcut) like lower (?) or" +
                        " lower(bpi18n.post_description) like lower (?)" +
                        ")" +
                        " ORDER BY bp.post_creation_date DESC" +
                        ")AS results";
        return jdbcTemplate.queryForObject(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getInt(1)});
    }

    public List<Object[]> listForPageWithArchiveDateAndPaging(Date startDate, Date endDate, Long languageId, Integer articlesPerPage, Integer currentPage) {
        Object[] dbParams = new Object[5];
        dbParams[0] = languageId;
        dbParams[1] = startDate;
        dbParams[2] = endDate;
        dbParams[3] = articlesPerPage;
        dbParams[4] = currentPage * articlesPerPage;
        String query =
                "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.post_creation_date >= ? AND bp.post_creation_date<? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

    public Object[] listCountForPageWithArchiveDateAndPaging(Date startDate, Date endDate, Long languageId) {
        Object[] dbParams = new Object[3];
        dbParams[0] = languageId;
        dbParams[1] = startDate;
        dbParams[2] = endDate;
        String query =
                "SELECT count(*) FROM(" +
                        "SELECT" +
                        " bp.id, bpi18n.id" +
                        " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
                        " WHERE" +
                        " bp.id = bpi18n.post_id AND" +
                        " bpi18n.language_id = ? AND" +
                        " bp.post_creation_date >= ? AND bp.post_creation_date<? AND" +
                        " bp.status = 'P' AND bpi18n.status = 'P'" +
                        " ORDER BY bp.post_creation_date DESC" +
                        ")AS results";
        return jdbcTemplate.queryForObject(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getInt(1)});
    }

    public int getTotalNumberNotDeleted() {
        return jdbcTemplate.queryForObject("select count(*) from BLOG_POSTS where status <> 'D'", Integer.class);
    }

    public List<Object[]> searchByAjax(
            int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchPostTextCode
    ) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(5);
        if ((searchPostTextCode != null) && !searchPostTextCode.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(post_text_code) like lower(?) ";
            additionalParams.add("%" + searchPostTextCode + "%");
        }
        if (searchAuthorId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and post_author_id = ? ";
            additionalParams.add(searchAuthorId);
        }

        numberOfSearchParams++;
        additionalParams.add(iDisplayLength);
        numberOfSearchParams++;
        additionalParams.add(iDisplayStart);
        Object[] dbParams = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParams[i] = additionalParams.get(i);
        }
        String query =
                "select " +
                        " bp.id, (first_name || ' ' || last_name) as author, post_text_code, post_creation_date, bp.status" +
                        " from BLOG_POSTS bp left join CMS_AUTHORS ca ON bp.post_author_id = ca.id " +
                        " where bp.status <> 'D' and ca.status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by post_creation_date desc" +
                        " limit ? offset ?";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> {
                    Object[] o = new Object[5];
                    o[0] = resultSet.getInt("ID");
                    o[1] = resultSet.getString("AUTHOR");
                    o[2] = resultSet.getString("POST_TEXT_CODE");
                    o[3] = resultSet.getDate("POST_CREATION_DATE");
                    o[4] = resultSet.getString("STATUS");
                    return o;
                });
    }

    public int searchByAjaxCount(Integer searchAuthorId, String searchPostTextCode) {
        int numberOfSearchParams = 0;
        String additionalQuery = "";
        List<Object> additionalParams = new ArrayList<>(5);
        if ((searchPostTextCode != null) && !searchPostTextCode.isEmpty()) {
            ++numberOfSearchParams;
            additionalQuery += " and lower(post_text_code) like lower(?) ";
            additionalParams.add("%" + searchPostTextCode + "%");
        }
        if (searchAuthorId != null) {
            ++numberOfSearchParams;
            additionalQuery += " and post_author_id = ? ";
            additionalParams.add(searchAuthorId);
        }

        Object[] dbParamsForCount = new Object[numberOfSearchParams];
        for (int i = 0; i < numberOfSearchParams; ++i) {
            dbParamsForCount[i] = additionalParams.get(i);
        }
        String query =
                "select count(*) from (" +
                        "select " +
                        " bp.id, (first_name || ' ' || last_name) as author, post_text_code, post_creation_date, bp.status" +
                        " from BLOG_POSTS bp left join CMS_AUTHORS ca ON bp.post_author_id = ca.id " +
                        " where bp.status <> 'D' and ca.status <> 'D' " + additionalQuery +
                        " and 1 = 1" +
                        " order by post_creation_date desc" +
                        ") as results";
        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
    }

    public BlogPost get(Long id) {
        String query =
                "SELECT " +
                        " id, post_author_id, post_text_code, post_creation_date, status" +
                        " FROM BLOG_POSTS " +
                        "WHERE id = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = id;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPost(resultSet));
    }

    public BlogPost getByCode(String code) {
        String query =
                "SELECT " +
                        " id, post_author_id, post_text_code, post_creation_date, status" +
                        " FROM BLOG_POSTS " +
                        "WHERE post_text_code = ?";
        Object[] dbParams = new Object[1];
        dbParams[0] = code;
        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPost(resultSet));
    }

    private BlogPost mapBlogPost(ResultSet resultSet) throws SQLException {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(resultSet.getLong("ID"));
        blogPost.setPostAuthorId(resultSet.getLong("POST_AUTHOR_ID"));
        blogPost.setPostTextCode(resultSet.getString("POST_TEXT_CODE"));
        blogPost.setPostCreationDate(resultSet.getDate("POST_CREATION_DATE"));
        blogPost.setStatus(resultSet.getString("STATUS"));
        return blogPost;
    }

    public Long add(BlogPost blogPost) {
        Object[] dbParams = new Object[5];
        Long id = jdbcTemplate.queryForObject("select nextval('BLOG_POSTS_S')", Long.class);
        dbParams[0] = id;
        dbParams[1] = blogPost.getPostAuthorId();
        dbParams[2] = blogPost.getPostTextCode();
        dbParams[3] = blogPost.getPostCreationDate();
        dbParams[4] = blogPost.getStatus();
        jdbcTemplate.update("INSERT INTO BLOG_POSTS(id, post_author_id, post_text_code, post_creation_date, status)" +
                "VALUES (?, ?, ?, ?, ?)", dbParams);
        return id;
    }

    public void update(BlogPost blogPost) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPost.getPostTextCode();
        dbParams[1] = blogPost.getStatus();
        dbParams[2] = blogPost.getId();
        jdbcTemplate.update("UPDATE BLOG_POSTS SET post_text_code = ?, status = ? WHERE id = ?", dbParams);
    }

    public void updatePostBasicInfo(BlogPost blogPost) {
        Object[] dbParams = new Object[3];
        dbParams[0] = blogPost.getPostTextCode();
        dbParams[1] = blogPost.getStatus();
        dbParams[2] = blogPost.getId();
        jdbcTemplate.update("UPDATE BLOG_POSTS SET post_text_code = ?, status = ? WHERE id = ?", dbParams);
    }

    public void delete(BlogPost blogPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPost.getId();
        jdbcTemplate.update("update BLOG_POSTS set status = 'D' where id = ?", dbParams);
    }

    public void undelete(BlogPost blogPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPost.getId();
        jdbcTemplate.update("update BLOG_POSTS set status = 'H' where id = ?", dbParams);
    }

    public void publish(BlogPost blogPost) {
        Object[] dbParams = new Object[1];
        dbParams[0] = blogPost.getId();
        jdbcTemplate.update("update BLOG_POSTS set status = 'P' where id = ?", dbParams);
    }

    public List<Object[]> listI18nPosts(Long languageId) {
        Object[] dbParams = new Object[1];
        dbParams[0] = languageId;
        String query =
                "SELECT bp.id, bpi.id " +
                        "FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi " +
                        "WHERE " +
                        "bp.status = 'P' AND bpi.status = 'P' AND " +
                        "bp.id = bpi.post_id AND " +
                        "bpi.language_id = ? " +
                        "ORDER BY bp.post_creation_date DESC ";
        return jdbcTemplate.query(query, dbParams,
                (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
    }

}
