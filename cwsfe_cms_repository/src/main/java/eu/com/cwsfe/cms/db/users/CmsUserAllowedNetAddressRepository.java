package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsUserAllowedNetAddressRepository {

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsUserAllowedNetAddressEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsUserAllowedNetAddressEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsUserAllowedNetAddressEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public Long countAddressesForUser(Session session, long userId) {
        Query query = session.getNamedQuery(CmsUserAllowedNetAddressEntity.COUNT_ADDRESSES_FOR_USER);
        query.setParameter("userId", userId);
        return (Long) query.getSingleResult();
    }

    public List<CmsUserAllowedNetAddressEntity> listForUser(Session session, long userId) {
        Query query = session.getNamedQuery(CmsUserAllowedNetAddressEntity.LIST_FOR_USER);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public CmsUserAllowedNetAddressEntity get(Session session, Long id) {
        return session.get(CmsUserAllowedNetAddressEntity.class, id);
    }

    public Long add(Session session, CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        session.saveOrUpdate(cmsUserAllowedNetAddress);
        session.flush();
        return cmsUserAllowedNetAddress.getId();
    }

    public void delete(Session session, CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        session.delete(cmsUserAllowedNetAddress);
    }

}
