package eu.com.cwsfe.cms.db.blog;

import org.springframework.stereotype.Repository;

@Repository
public class BlogPostsRepository {

//    public List<Object[]> listArchiveStatistics(Session session, Long languageId) {
//        Object[] dbParams = new Object[1];
//        dbParams[0] = languageId;
//        String query =
//            "SELECT" +
//                "  COUNT(bp.id), CAST(EXTRACT(YEAR FROM bp.post_creation_date) AS TEXT) AS YEAR, CAST(EXTRACT(MONTH FROM bp.post_creation_date) AS TEXT) AS MONTH" +
//                "  FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                "  WHERE" +
//                "  bp.id = bpi18n.post_id AND" +
//                "  bp.status = 'P' AND bpi18n.status = 'P' AND" +
//                "  bpi18n.language_id = ?" +
//                "  GROUP BY MONTH, YEAR" +
//                " ORDER BY YEAR DESC, MONTH DESC";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3)});
//    }
//
//    public List<Object[]> listArchiveStatistics(Session session) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        return query.list();
//
//        String query =
//            "SELECT" +
//                "  COUNT(bp.id), CAST(EXTRACT(YEAR FROM bp.post_creation_date) AS TEXT) AS YEAR, CAST(EXTRACT(MONTH FROM bp.post_creation_date) AS TEXT) AS MONTH" +
//                "  FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                "  WHERE" +
//                "  bp.id = bpi18n.post_id AND" +
//                "  bp.status = 'P' AND bpi18n.status = 'P'" +
//                "  GROUP BY MONTH, YEAR" +
//                " ORDER BY YEAR DESC, MONTH DESC";
//        return jdbcTemplate.query(query,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3)});
//    }
//
//    public List<Object[]> listForPageWithPaging(Session session, Long languageId, Integer limit, Integer offset) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        query.setMaxResults(limit);
//        query.setFirstResult(offset);
//        return query.getResultList();
//
//        Object[] dbParams = new Object[3];
//        dbParams[0] = languageId;
//        dbParams[1] = limit;
//        dbParams[2] = offset;
//        String query =
//            "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                " LIMIT ? OFFSET ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }
//
//    public long listCountForPageWithPaging(Session session, Long languageId) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.COUNT_FOR_AJAX_N);
//        return (Long) query.uniqueResult();
//
//        Object[] dbParams = new Object[1];
//        dbParams[0] = languageId;
//        String query =
//            "SELECT count(*) FROM(" +
//                "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                ") AS results";
//        return jdbcTemplate.queryForObject(query, dbParams, Long.class);
//    }
//
//    public List<Object[]> listForPageWithCategoryAndPaging(Session session, Long categoryId, Long languageId, Integer limit, Integer offset) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        query.setMaxResults(limit);
//        query.setFirstResult(offset);
//        return query.getResultList();
//
//        Object[] dbParams = new Object[4];
//        dbParams[0] = categoryId;
//        dbParams[1] = languageId;
//        dbParams[2] = limit;
//        dbParams[3] = offset;
//        String query =
//            "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n, BLOG_POST_KEYWORDS bpk" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bp.id = bpk.blog_post_id AND" +
//                " bpk.blog_keyword_id = ? AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                " LIMIT ? OFFSET ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }
//
//    public long listCountForPageWithCategoryAndPaging(Session session, Long categoryId, Long languageId) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.COUNT_FOR_AJAX_N);
//        return (Long) query.uniqueResult();
//
//        Object[] dbParams = new Object[2];
//        dbParams[0] = categoryId;
//        dbParams[1] = languageId;
//        String query =
//            "SELECT count(*) FROM(" +
//                "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n, BLOG_POST_KEYWORDS bpk" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bp.id = bpk.blog_post_id AND" +
//                " bpk.blog_keyword_id = ? AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                ")AS results";
//        return jdbcTemplate.queryForObject(query, dbParams, Long.class);
//    }
//
//    public List<Object[]> listForPageWithSearchTextAndPaging(Session session, String searchText, Long languageId, Integer limit, Integer offset) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        query.setMaxResults(limit);
//        query.setFirstResult(offset);
//        return query.getResultList();
//
//        Object[] dbParams = new Object[6];
//        dbParams[0] = languageId;
//        dbParams[1] = '%' + searchText + '%';
//        dbParams[2] = '%' + searchText + '%';
//        dbParams[3] = '%' + searchText + '%';
//        dbParams[4] = limit;
//        dbParams[5] = offset;
//        String query =
//            "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'and" +
//                " (" +
//                " lower(bpi18n.post_title) like lower( ?) or" +
//                " lower(bpi18n.post_shortcut) like lower (?) or" +
//                " lower(bpi18n.post_description) like lower (?)" +
//                " )" +
//                " ORDER BY bp.post_creation_date DESC" +
//                " LIMIT ? OFFSET ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }
//
//    public long listCountForPageWithSearchTextAndPaging(Session session, String searchText, Long languageId) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        return query.list();
//
//        Object[] dbParams = new Object[4];
//        dbParams[0] = languageId;
//        dbParams[1] = '%' + searchText + '%';
//        dbParams[2] = '%' + searchText + '%';
//        dbParams[3] = '%' + searchText + '%';
//        String query =
//            "SELECT count(*) FROM(" +
//                "SELECT" +
//                " bp.id, bpi18n.id" +
//                " from BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P' and" +
//                "(" +
//                " lower(bpi18n.post_title)like lower(?) or" +
//                " lower(bpi18n.post_shortcut) like lower (?) or" +
//                " lower(bpi18n.post_description) like lower (?)" +
//                ")" +
//                " ORDER BY bp.post_creation_date DESC" +
//                ")AS results";
//        return jdbcTemplate.queryForObject(query, dbParams, Long.class);
//    }
//
//    public List<Object[]> listForPageWithArchiveDateAndPaging(Session session, Date startDate, Date endDate, Long languageId, Integer limit, Integer offset) {
//        Object[] dbParams = new Object[5];
//        dbParams[0] = languageId;
//        dbParams[1] = startDate;
//        dbParams[2] = endDate;
//        dbParams[3] = limit;
//        dbParams[4] = offset;
//        String query =
//            "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.post_creation_date >= ? AND bp.post_creation_date<? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                " LIMIT ? OFFSET ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }
//
//    public long listCountForPageWithArchiveDateAndPaging(Session session, Date startDate, Date endDate, Long languageId) {
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        query.setMaxResults(limit);
//        query.setFirstResult(offset);
//        return query.getResultList();
//
//        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
//        return query.list();
//
//        Object[] dbParams = new Object[3];
//        dbParams[0] = languageId;
//        dbParams[1] = startDate;
//        dbParams[2] = endDate;
//        String query =
//            "SELECT count(*) FROM(" +
//                "SELECT" +
//                " bp.id, bpi18n.id" +
//                " FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi18n" +
//                " WHERE" +
//                " bp.id = bpi18n.post_id AND" +
//                " bpi18n.language_id = ? AND" +
//                " bp.post_creation_date >= ? AND bp.post_creation_date<? AND" +
//                " bp.status = 'P' AND bpi18n.status = 'P'" +
//                " ORDER BY bp.post_creation_date DESC" +
//                ")AS results";
//        return jdbcTemplate.queryForObject(query, dbParams, Long.class);
//    }
//
//    public int getTotalNumberNotDeleted(Session session) {
//        return jdbcTemplate.queryForObject("select count(*) from BLOG_POSTS where status <> 'D'", Integer.class);
//    }
//
//    public List<Object[]> searchByAjax(
//        Session session, int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchPostTextCode
//    ) {
//        int numberOfSearchParams = 0;
//        String additionalQuery = "";
//        List<Object> additionalParams = new ArrayList<>(5);
//        if ((searchPostTextCode != null) && !searchPostTextCode.isEmpty()) {
//            ++numberOfSearchParams;
//            additionalQuery += " and lower(post_text_code) like lower(?) ";
//            additionalParams.add("%" + searchPostTextCode + "%");
//        }
//        if (searchAuthorId != null) {
//            ++numberOfSearchParams;
//            additionalQuery += " and post_author_id = ? ";
//            additionalParams.add(searchAuthorId);
//        }
//
//        numberOfSearchParams++;
//        additionalParams.add(iDisplayLength);
//        numberOfSearchParams++;
//        additionalParams.add(iDisplayStart);
//        Object[] dbParams = new Object[numberOfSearchParams];
//        for (int i = 0; i < numberOfSearchParams; ++i) {
//            dbParams[i] = additionalParams.get(i);
//        }
//        String query =
//            "select " +
//                " bp.id, (first_name || ' ' || last_name) as author, post_text_code, post_creation_date, bp.status" +
//                " from BLOG_POSTS bp left join CMS_AUTHORS ca ON bp.post_author_id = ca.id " +
//                " where bp.status <> 'D' and ca.status <> 'D' " + additionalQuery +
//                " and 1 = 1" +
//                " order by post_creation_date desc" +
//                " limit ? offset ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> {
//                Object[] o = new Object[5];
//                o[0] = resultSet.getInt("ID");
//                o[1] = resultSet.getString("AUTHOR");
//                o[2] = resultSet.getString("POST_TEXT_CODE");
//                o[3] = resultSet.getDate("POST_CREATION_DATE");
//                o[4] = resultSet.getString("STATUS");
//                return o;
//            });
//    }
//
//    public int searchByAjaxCount(Session session, Integer searchAuthorId, String searchPostTextCode) {
//        int numberOfSearchParams = 0;
//        String additionalQuery = "";
//        List<Object> additionalParams = new ArrayList<>(5);
//        if ((searchPostTextCode != null) && !searchPostTextCode.isEmpty()) {
//            ++numberOfSearchParams;
//            additionalQuery += " and lower(post_text_code) like lower(?) ";
//            additionalParams.add("%" + searchPostTextCode + "%");
//        }
//        if (searchAuthorId != null) {
//            ++numberOfSearchParams;
//            additionalQuery += " and post_author_id = ? ";
//            additionalParams.add(searchAuthorId);
//        }
//
//        Object[] dbParamsForCount = new Object[numberOfSearchParams];
//        for (int i = 0; i < numberOfSearchParams; ++i) {
//            dbParamsForCount[i] = additionalParams.get(i);
//        }
//        String query =
//            "select count(*) from (" +
//                "select " +
//                " bp.id, (first_name || ' ' || last_name) as author, post_text_code, post_creation_date, bp.status" +
//                " from BLOG_POSTS bp left join CMS_AUTHORS ca ON bp.post_author_id = ca.id " +
//                " where bp.status <> 'D' and ca.status <> 'D' " + additionalQuery +
//                " and 1 = 1" +
//                " order by post_creation_date desc" +
//                ") as results";
//        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
//    }
//
//    public BlogPost get(Session session, Long id) {
//        return session.get(BlogKeywordsEntity.class, id);
//        String query =
//            "SELECT " +
//                " id, post_author_id, post_text_code, post_creation_date, status" +
//                " FROM BLOG_POSTS " +
//                "WHERE id = ?";
//        Object[] dbParams = new Object[1];
//        dbParams[0] = id;
//        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPost(resultSet));
//    }
//
//    public BlogPost getByCode(Session session, String code) {
//        return session.get(BlogKeywordsEntity.class, id);
//        String query =
//            "SELECT " +
//                " id, post_author_id, post_text_code, post_creation_date, status" +
//                " FROM BLOG_POSTS " +
//                "WHERE post_text_code = ?";
//        Object[] dbParams = new Object[1];
//        dbParams[0] = code;
//        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapBlogPost(resultSet));
//    }
//
//    public Long add(Session session, BlogPost blogPost) {
//        blogKeyword.setStatus(NewDeletedStatus.NEW);
//        session.saveOrUpdate(blogKeyword);
//        session.flush();
//        return blogKeyword.getId();
//
//        Object[] dbParams = new Object[5];
//        Long id = jdbcTemplate.queryForObject("select nextval('BLOG_POSTS_S')", Long.class);
//        dbParams[0] = id;
//        dbParams[1] = blogPost.getPostAuthorId();
//        dbParams[2] = blogPost.getPostTextCode();
//        dbParams[3] = blogPost.getPostCreationDate();
//        dbParams[4] = blogPost.getStatus().getCode();
//        jdbcTemplate.update("INSERT INTO BLOG_POSTS(id, post_author_id, post_text_code, post_creation_date, status)" +
//            "VALUES (?, ?, ?, ?, ?)", dbParams);
//        return id;
//    }
//
//    public void update(Session session, BlogPost blogPost) {
//        session.update(blogKeyword);
//        Object[] dbParams = new Object[3];
//        dbParams[0] = blogPost.getPostTextCode();
//        dbParams[1] = blogPost.getStatus().getCode();
//        dbParams[2] = blogPost.getId();
//        jdbcTemplate.update("UPDATE BLOG_POSTS SET post_text_code = ?, status = ? WHERE id = ?", dbParams);
//    }
//
//    public void updatePostBasicInfo(Session session, BlogPost blogPost) {
//        session.update(blogKeyword);
//
//        Object[] dbParams = new Object[3];
//        dbParams[0] = blogPost.getPostTextCode();
//        dbParams[1] = blogPost.getStatus().getCode();
//        dbParams[2] = blogPost.getId();
//        jdbcTemplate.update("UPDATE BLOG_POSTS SET post_text_code = ?, status = ? WHERE id = ?", dbParams);
//    }
//
//    public void delete(Session session, BlogPost blogPost) {
//        blogKeyword.setStatus(NewDeletedStatus.DELETED);
//        session.update(blogKeyword);
//
//        Object[] dbParams = new Object[1];
//        dbParams[0] = blogPost.getId();
//        jdbcTemplate.update("update BLOG_POSTS set status = 'D' where id = ?", dbParams);
//    }
//
//    public void undelete(Session session, BlogPost blogPost) {
//        blogKeyword.setStatus(NewDeletedStatus.NEW);
//        session.update(blogKeyword);
//
//        Object[] dbParams = new Object[1];
//        dbParams[0] = blogPost.getId();
//        jdbcTemplate.update("update BLOG_POSTS set status = 'H' where id = ?", dbParams);
//    }
//
//    public void publish(Session session, BlogPost blogPost) {
//        blogKeyword.setStatus(NewDeletedStatus.NEW);
//        session.update(blogKeyword);
//
//        Object[] dbParams = new Object[1];
//        dbParams[0] = blogPost.getId();
//        jdbcTemplate.update("update BLOG_POSTS set status = 'P' where id = ?", dbParams);
//    }
//
//    public List<Object[]> listI18nPosts(Session session, Long languageId) {
//        Object[] dbParams = new Object[1];
//        dbParams[0] = languageId;
//        String query =
//            "SELECT bp.id, bpi.id " +
//                "FROM BLOG_POSTS bp, BLOG_POST_I18N_CONTENTS bpi " +
//                "WHERE " +
//                "bp.status = 'P' AND bpi.status = 'P' AND " +
//                "bp.id = bpi.post_id AND " +
//                "bpi.language_id = ? " +
//                "ORDER BY bp.post_creation_date DESC ";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }

}
