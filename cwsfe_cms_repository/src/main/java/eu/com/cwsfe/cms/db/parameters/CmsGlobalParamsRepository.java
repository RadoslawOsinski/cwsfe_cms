package eu.com.cwsfe.cms.db.parameters;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsGlobalParamsRepository {

    private final SessionFactory sessionFactory;

    public CmsGlobalParamsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsGlobalParamsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsGlobalParamsEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsGlobalParamsEntity.LIST);
        return query.list();
    }

    public List<CmsGlobalParamsEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsGlobalParamsEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsGlobalParamsEntity> listForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsGlobalParamsEntity.LIST_FOLDERS_FOR_DROP_LIST);
        query.setParameter("code", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsGlobalParamsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsGlobalParamsEntity.class, id);
    }

    public CmsGlobalParamsEntity getByCode(String code) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsGlobalParamsEntity.GET_BY_CODE);
        query.setParameter("code", code);
        return (CmsGlobalParamsEntity) query.getSingleResult();
    }

    public Long add(CmsGlobalParamsEntity cmsGlobalParam) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsGlobalParam);
        currentSession.flush();
        return cmsGlobalParam.getId();
    }

    public void update(CmsGlobalParamsEntity cmsGlobalParam) {
        sessionFactory.getCurrentSession().update(cmsGlobalParam);
    }

    public void delete(CmsGlobalParamsEntity cmsGlobalParam) {
        sessionFactory.getCurrentSession().delete(cmsGlobalParam);
    }

}
