package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsTypesRepository {

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsNewsTypesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsNewsTypesEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsNewsTypesEntity.LIST);
        return query.list();
    }

    public List<CmsNewsTypesEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsNewsTypesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsNewsTypesEntity> listNewsTypesForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsNewsTypesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("type", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsNewsTypesEntity get(Session session, Long id) {
        return session.get(CmsNewsTypesEntity.class, id);
    }

    public CmsNewsTypesEntity getByType(Session session, String type) {
        Query query = session.getNamedQuery(CmsNewsTypesEntity.GET_BY_TYPE);
        query.setParameter("type", type);
        return (CmsNewsTypesEntity) query.getSingleResult();
    }

    public Long add(Session session, CmsNewsTypesEntity newsType) {
        newsType.setStatus("NEW");
        session.saveOrUpdate(newsType);
        session.flush();
        return newsType.getId();
    }

    public void update(Session session, CmsNewsTypesEntity newsType) {
        session.update(newsType);
    }

    public void delete(Session session, CmsNewsTypesEntity newsType) {
        newsType.setStatus("DELETED");
        session.update(newsType);
    }

    public void undelete(Session session, CmsNewsTypesEntity newsType) {
        newsType.setStatus("NEW");
        session.update(newsType);
    }

}
