package eu.com.cwsfe.cms.db.author;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsAuthorsRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsAuthorsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public List<CmsAuthorsEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsAuthorsEntity.LIST);
        query.setParameter("status", NewDeletedStatus.NEW);
        return query.list();
    }

    public List<CmsAuthorsEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsAuthorsEntity.LIST);
        query.setParameter("status", NewDeletedStatus.NEW);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsAuthorsEntity> listAuthorsForDropList(Session session, String term, int limit) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CmsAuthorsEntity> query = criteriaBuilder.createQuery(CmsAuthorsEntity.class);
        Root<CmsAuthorsEntity> authors = query.from(CmsAuthorsEntity.class);
        query.select(authors);
        query.where(
            criteriaBuilder.equal(authors.get("status"), NewDeletedStatus.NEW),
            criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(authors.get("firstName")), "%" + term.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(authors.get("lastName")), "%" + term.toLowerCase() + "%")
            )
        );
        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.asc(authors.get("firstName")));
        orders.add(criteriaBuilder.asc(authors.get("lastName")));
        query.orderBy(orders);
        return session.createQuery(query)
            .setMaxResults(limit)
            .list();
    }

    public CmsAuthorsEntity get(Session session, Long id) {
        return session.get(CmsAuthorsEntity.class, id);
    }

    public Long add(Session session, CmsAuthorsEntity cmsAuthor) {
        cmsAuthor.setStatus(NewDeletedStatus.NEW);
        session.saveOrUpdate(cmsAuthor);
        session.flush();
        return cmsAuthor.getId();
    }

    public void update(Session session, CmsAuthorsEntity cmsAuthor) {
        session.update(cmsAuthor);
    }

    public void delete(Session session, CmsAuthorsEntity cmsAuthor) {
        CmsAuthorsEntity cmsAuthorsEntity = session.get(CmsAuthorsEntity.class, cmsAuthor.getId());
        cmsAuthorsEntity.setStatus(NewDeletedStatus.DELETED);
        session.update(cmsAuthorsEntity);
    }

    public void undelete(Session session, CmsAuthorsEntity cmsAuthor) {
        CmsAuthorsEntity cmsAuthorsEntity = session.get(CmsAuthorsEntity.class, cmsAuthor.getId());
        cmsAuthorsEntity.setStatus(NewDeletedStatus.NEW);
        session.update(cmsAuthorsEntity);
    }

}
