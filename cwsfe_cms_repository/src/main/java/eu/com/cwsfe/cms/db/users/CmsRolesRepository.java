package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsRolesRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsRolesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public List<CmsRolesEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsRolesEntity.LIST);
        return query.list();
    }

    public List<CmsRolesEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsRolesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsRolesEntity> listRolesForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsRolesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("roleName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public Optional<CmsRolesEntity> get(Session session, Long id) {
        return Optional.ofNullable(session.get(CmsRolesEntity.class, id));
    }

    public Optional<CmsRolesEntity> getByCode(Session session, String roleCode) {
        Query query = session.getNamedQuery(CmsRolesEntity.GET_BY_CODE);
        query.setParameter("roleCode", roleCode);
        return (Optional<CmsRolesEntity>) query.uniqueResultOptional();
    }

}
