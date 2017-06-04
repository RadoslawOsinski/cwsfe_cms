package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsUserAllowedNetAddressRepository {

    private final SessionFactory sessionFactory;

    public CmsUserAllowedNetAddressRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserAllowedNetAddressEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsUserAllowedNetAddressEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserAllowedNetAddressEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public int countAddressesForUser(long userId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserAllowedNetAddressEntity.COUNT_ADDRESSES_FOR_USER);
        query.setParameter("userId", userId);
        return (int) query.getSingleResult();
    }

    public List<CmsUserAllowedNetAddressEntity> listForUser(long userId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserAllowedNetAddressEntity.LIST_FOR_USER);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public CmsUserAllowedNetAddressEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsUserAllowedNetAddressEntity.class, id);
    }

    public Long add(CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsUserAllowedNetAddress);
        currentSession.flush();
        return cmsUserAllowedNetAddress.getId();
    }

    public void delete(CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        sessionFactory.getCurrentSession().delete(cmsUserAllowedNetAddress);
    }

}
