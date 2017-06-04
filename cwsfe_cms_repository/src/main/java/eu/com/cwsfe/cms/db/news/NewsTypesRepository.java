package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsTypesRepository {

    private final SessionFactory sessionFactory;

    public NewsTypesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsTypesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsNewsTypesEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsTypesEntity.LIST);
        return query.list();
    }

    public List<CmsNewsTypesEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsTypesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsNewsTypesEntity> listNewsTypesForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsTypesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("type", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsNewsTypesEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsNewsTypesEntity.class, id);
    }

    public CmsNewsTypesEntity getByType(String type) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsTypesEntity.GET_BY_TYPE);
        query.setParameter("type", type);
        return (CmsNewsTypesEntity) query.getSingleResult();
    }

    public Long add(CmsNewsTypesEntity newsType) {
        newsType.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(newsType);
        currentSession.flush();
        return newsType.getId();
    }

    public void update(CmsNewsTypesEntity newsType) {
        sessionFactory.getCurrentSession().update(newsType);
    }

    public void delete(CmsNewsTypesEntity newsType) {
        newsType.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(newsType);
    }

    public void undelete(CmsNewsTypesEntity newsType) {
        newsType.setStatus("NEW");
        sessionFactory.getCurrentSession().update(newsType);
    }

}
