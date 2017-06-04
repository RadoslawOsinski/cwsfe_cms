package eu.com.cwsfe.cms.db.users;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsRolesRepository {

    private final SessionFactory sessionFactory;

    public CmsRolesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<CmsRolesEntity> listUserRoles(Long userId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.LIST_USER_ROLES);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsRolesEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.LIST);
        return query.list();
    }

    public List<CmsRolesEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsRolesEntity> listRolesForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("roleName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsRolesEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsRolesEntity.class, id);
    }

    public CmsRolesEntity getByCode(String roleCode) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsRolesEntity.GET_BY_CODE);
        query.setParameter("roleCode", roleCode);
        return (CmsRolesEntity) query.getSingleResult();
    }

}
