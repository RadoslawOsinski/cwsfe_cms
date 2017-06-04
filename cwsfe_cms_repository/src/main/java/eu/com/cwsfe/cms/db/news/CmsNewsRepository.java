package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CmsNewsRepository {

    private final SessionFactory sessionFactory;

    public CmsNewsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    public int getTotalNumberNotDeleted() {
//        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsEntity.COUNT_FOR_AJAX_N);
//        return (Long) query.uniqueResult();
//
//        String query = "SELECT count(*) FROM CMS_NEWS WHERE status <> 'D'";
//        return jdbcTemplate.queryForObject(query, Integer.class);
//    }

//    public List<Object[]> searchByAjax(
//        int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchNewsCode
//    ) {
//        int numberOfSearchParams = 0;
//        String additionalQuery = "";
//        List<Object> additionalParams = new ArrayList<>(5);
//        if ((searchNewsCode != null) && !searchNewsCode.isEmpty()) {
//            ++numberOfSearchParams;
//            additionalQuery += " and lower(news_code) like lower(?) ";
//            additionalParams.add("%" + searchNewsCode + "%");
//        }
//        if (searchAuthorId != null) {
//            ++numberOfSearchParams;
//            additionalQuery += " and author_id = ? ";
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
//                " cn.id, (first_name || ' ' || last_name) as author, news_type_id, folder_id, creation_date, news_code, cn.status" +
//                " from CMS_NEWS cn left join CMS_AUTHORS ca ON cn.author_id = ca.id " +
//                " where cn.status <> 'D' and ca.status <> 'D' " + additionalQuery +
//                " and 1 = 1" +
//                " order by creation_date desc" +
//                " limit ? offset ?";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> {
//                Object[] o = new Object[7];
//                o[0] = resultSet.getInt("ID");
//                o[1] = resultSet.getString("AUTHOR");
//                o[2] = resultSet.getInt("NEWS_TYPE_ID");
//                o[3] = resultSet.getInt("FOLDER_ID");
//                o[4] = resultSet.getDate("CREATION_DATE");
//                o[5] = resultSet.getString("NEWS_CODE");
//                o[6] = resultSet.getString("STATUS");
//                return o;
//            });
//    }
//
//    public int searchByAjaxCount(Integer searchAuthorId, String searchNewsCode) {
//        int numberOfSearchParams = 0;
//        String additionalQuery = "";
//        List<Object> additionalParams = new ArrayList<>(5);
//        if ((searchNewsCode != null) && !searchNewsCode.isEmpty()) {
//            ++numberOfSearchParams;
//            additionalQuery += " and lower(news_code) like lower(?) ";
//            additionalParams.add("%" + searchNewsCode + "%");
//        }
//        if (searchAuthorId != null) {
//            ++numberOfSearchParams;
//            additionalQuery += " and author_id = ? ";
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
//                " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
//                " from CMS_NEWS" +
//                " where status <> 'D' " + additionalQuery +
//                " and 1 = 1" +
//                " order by creation_date desc" +
//                ") as results";
//        return jdbcTemplate.queryForObject(query, dbParamsForCount, Integer.class);
//    }
//
//    public List<CmsNewsEntity> listAll() {
//        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsEntity.LIST_N);
//        return query.getResultList();
//
//        String query =
//            "SELECT " +
//                "id, author_id, news_type_id, folder_id, creation_date, news_code, status " +
//                "FROM CMS_NEWS " +
//                "WHERE status <> 'D' " +
//                "ORDER BY creation_date DESC";
//        return jdbcTemplate.query(query, (resultSet, rowNum) -> mapCmsNews(resultSet));
//    }

    public CmsNewsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsNewsEntity.class, id);
    }

//    public CmsNews getByNewsTypeFolderAndNewsCode(Long newsTypeId, Long newsFolderId, String newsCode) {
//        String query =
//            "SELECT " +
//                " id, author_id, news_type_id, folder_id, creation_date, news_code, status" +
//                " FROM CMS_NEWS " +
//                "WHERE status = 'P' AND news_type_id = ? AND folder_id = ? AND news_code = ?";
//        Object[] dbParams = new Object[3];
//        dbParams[0] = newsTypeId;
//        dbParams[1] = newsFolderId;
//        dbParams[2] = newsCode;
//        return jdbcTemplate.queryForObject(query, dbParams, (resultSet, rowNum) -> mapCmsNews(resultSet));
//    }

    public Long add(CmsNewsEntity news) {
        news.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(news);
        currentSession.flush();
        return news.getId();
    }

    public void update(CmsNewsEntity newsPost) {
        sessionFactory.getCurrentSession().update(newsPost);
    }

    public void updatePostBasicInfo(CmsNewsEntity newsPost) {
        sessionFactory.getCurrentSession().update(newsPost);
    }

    public void delete(CmsNewsEntity newsPost) {
        newsPost.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(newsPost);
    }

    public void undelete(CmsNewsEntity newsPost) {
        newsPost.setStatus("HIDDEN");
        sessionFactory.getCurrentSession().update(newsPost);
    }

    public void publish(CmsNewsEntity newsPost) {
        newsPost.setStatus("PUBLISHED");
        sessionFactory.getCurrentSession().update(newsPost);    }

//    public List<Object[]> listByFolderLangAndNewsWithPaging(Integer newsFolderId, Long languageId, String newsType, int newsPerPage, int offset) {
//        Object[] dbParams = new Object[6];
//        dbParams[0] = newsFolderId;
//        dbParams[1] = newsFolderId;
//        dbParams[2] = languageId;
//        dbParams[3] = newsType;
//        dbParams[4] = newsPerPage;
//        dbParams[5] = offset;
//        String query =
//            "SELECT" +
//                " cn.id, cni18n.id " +
//                " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n " +
//                " WHERE " +
//                "  cn.id = cni18n.news_id AND " +
//                "  cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND " +
//                "  cni18n.language_id = ? AND " +
//                "  cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = ?) AND " +
//                "  cn.status = 'P' AND cni18n.status = 'P' " +
//                "ORDER BY cn.creation_date DESC " +
//                "LIMIT ? OFFSET ? ";
//        return jdbcTemplate.query(query, dbParams,
//            (resultSet, i) -> new Object[]{resultSet.getLong(1), resultSet.getLong(2)});
//    }
//
//    public Integer countListByFolderLangAndNewsWithPaging(Integer newsFolderId, Long languageId, String newsType) {
//        Object[] dbParams = new Object[4];
//        dbParams[0] = newsFolderId;
//        dbParams[1] = newsFolderId;
//        dbParams[2] = languageId;
//        dbParams[3] = newsType;
//        String query =
//            "SELECT count(*) FROM (" +
//                " SELECT " +
//                " cn.id, cni18n.id" +
//                " FROM CMS_NEWS cn, CMS_NEWS_I18N_CONTENTS cni18n" +
//                " WHERE" +
//                " cn.id = cni18n.news_id AND" +
//                " cn.folder_id IN (SELECT cf2.id FROM CMS_FOLDERS cf2 WHERE (cf2.id = ? OR cf2.parent_id = ?) AND cf2.status = 'N') AND" +
//                " cni18n.language_id = ? AND" +
//                " cn.news_type_id = (SELECT id FROM CMS_NEWS_TYPES WHERE status = 'N' AND type = ?) AND" +
//                " cn.status = 'P' AND cni18n.status = 'P'" +
//                " ORDER BY cn.creation_date DESC" +
//                ") AS results";
//        return jdbcTemplate.queryForObject(query, dbParams, Integer.class);
//    }

}
