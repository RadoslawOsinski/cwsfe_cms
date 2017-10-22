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
import java.util.Optional;

@Repository
public class CmsNewsRepository {

    public Long getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(CmsNewsEntity.GET_TOTAL_NUMBER_NOT_DELETED);
        query.setParameter("status", PublishedHiddenStatus.DELETED.name());
        return (Long) query.getSingleResult();
    }

    public List<SearchedNewsDTO> searchByAjax(
        Session session, int iDisplayStart, int iDisplayLength, String searchNewsCode
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

    public Long searchByAjaxCount(Session session, String searchNewsCode) {
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

    public Optional<CmsNewsEntity> get(Session session, Long id) {
        return Optional.ofNullable(session.get(CmsNewsEntity.class, id));
    }

    public Long add(Session session, CmsNewsEntity news) {
        news.setStatus(PublishedHiddenStatus.NEW);
        session.saveOrUpdate(news);
        session.flush();
        return news.getId();
    }

    public void update(Session session, CmsNewsEntity newsPost) {
        session.update(newsPost);
    }

    public void updatePostBasicInfo(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setNewsTypeId(newsPost.getNewsTypeId());
        dbCmsNewsEntity.setNewsFolderId(newsPost.getNewsFolderId());
        dbCmsNewsEntity.setNewsCode(newsPost.getNewsCode());
        dbCmsNewsEntity.setStatus(newsPost.getStatus());
        session.update(dbCmsNewsEntity);
    }

    public void delete(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus(PublishedHiddenStatus.DELETED);
        session.update(dbCmsNewsEntity);
    }

    public void undelete(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus(PublishedHiddenStatus.HIDDEN);
        session.update(dbCmsNewsEntity);
    }

    public void publish(Session session, CmsNewsEntity newsPost) {
        CmsNewsEntity dbCmsNewsEntity = session.get(CmsNewsEntity.class, newsPost.getId());
        dbCmsNewsEntity.setStatus(PublishedHiddenStatus.PUBLISHED);
        session.update(dbCmsNewsEntity);
    }

}
