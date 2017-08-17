package eu.com.cwsfe.cms.db.news;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsNewsRepository {

    public Long getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(CmsNewsEntity.GET_TOTAL_NUMBER_NOT_DELETED);
        query.setParameter("status", PublishedHiddenStatus.DELETED.name());
        return (Long) query.getSingleResult();
    }

    public List<SearchedNewsDTO> searchByAjax(
        Session session, int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchNewsCode
    ) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SearchedNewsDTO> query = builder.createQuery(SearchedNewsDTO.class);
        Root<CmsNewsEntity> news = query.from(CmsNewsEntity.class);
        Join<CmsNewsEntity, CmsAuthorsEntity> author = news.join("authorMapping");

        query.select(
            builder.construct(SearchedNewsDTO.class,
                news.get("id"),
                builder.concat(author.get("firstName"), builder.concat(" ", author.get("lastName"))).alias("author"),
                news.get("newsTypeId"),
                news.get("newsFolderId").alias("folderId"),
                news.get("creationDate"),
                news.get("newsCode"),
                news.get("status")
            )
        );
        query.where(builder.and(
            builder.not(builder.equal(news.get("status"), "DELETED"))),
            builder.not(builder.equal(author.get("status"), NewDeletedStatus.DELETED)),
            builder.like(builder.lower(news.get("newsCode")), "%" + searchNewsCode.toLowerCase() + "%")
        );

        List<Order> orders = new ArrayList<>();
        orders.add(builder.asc(news.get("creationDate")));
        query.orderBy(orders);

        return session.createQuery(query)
            .setMaxResults(iDisplayLength)
            .setFirstResult(iDisplayStart)
            .list();
    }

    public Long searchByAjaxCount(Session session, Integer searchAuthorId, String searchNewsCode) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<CmsNewsEntity> news = query.from(CmsNewsEntity.class);
        Join<CmsNewsEntity, CmsAuthorsEntity> author = news.join("authorMapping");

        query.select(builder.count(news));
        query.where(builder.and(
            builder.not(builder.equal(news.get("status"), "DELETED"))),
            builder.not(builder.equal(author.get("status"), NewDeletedStatus.DELETED)),
            builder.like(builder.lower(news.get("newsCode")), "%" + searchNewsCode.toLowerCase() + "%")
        );

        return session.createQuery(query).getSingleResult();
    }

//    public List<CmsNewsEntity> listAll(Session session) {
//        Query query = session.getNamedQuery(CmsNewsEntity.LIST_N);
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

    public CmsNewsEntity get(Session session, Long id) {
        return session.get(CmsNewsEntity.class, id);
    }

//    public CmsNews getByNewsTypeFolderAndNewsCode(Session session, Long newsTypeId, Long newsFolderId, String newsCode) {
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

    public Long add(Session session, CmsNewsEntity news) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, news.getId());
        dbCmsNewsEntity.setStatus("NEW");
        session.saveOrUpdate(dbCmsNewsEntity);
        session.flush();
        return dbCmsNewsEntity.getId();
    }

    public void update(Session session, CmsNewsEntity newsPost) {
        session.update(newsPost);
    }

    public void updatePostBasicInfo(Session session, CmsNewsEntity newsPost) {
        session.update(newsPost);
    }

    public void delete(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus("DELETED");
        session.update(dbCmsNewsEntity);
    }

    public void undelete(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus("HIDDEN");
        session.update(dbCmsNewsEntity);
    }

    public void publish(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus("PUBLISHED");
        session.update(dbCmsNewsEntity);
    }

//    public List<Object[]> listByFolderLangAndNewsWithPaging(Session session, Integer newsFolderId, Long languageId, String newsType, int newsPerPage, int offset) {
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
//    public Integer countListByFolderLangAndNewsWithPaging(Session session, Integer newsFolderId, Long languageId, String newsType) {
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
