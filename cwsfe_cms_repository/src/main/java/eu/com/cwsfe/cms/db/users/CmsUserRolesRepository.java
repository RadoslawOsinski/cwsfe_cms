package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsUserRolesRepository {

    public List<CmsUserRolesEntity> listForUser(Session session, Long userId) {
        Query query = session.getNamedQuery(CmsUserRolesEntity.LIST_FOR_USER);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void add(Session session, CmsUserRolesEntity cmsUserRole) {
        session.saveOrUpdate(cmsUserRole);
        session.flush();
    }

    public void deleteForUser(Session session, CmsUserRolesEntity cmsUserRole) {
        Query query = session.getNamedQuery(CmsUserRolesEntity.DELETE_USER_ROLES);
        query.setParameter("userId", cmsUserRole.getCmsUserId());
        query.executeUpdate();
    }

    public void deleteForUser(Session session, Long id) {
        Query query = session.getNamedQuery(CmsUserRolesEntity.DELETE_USER_ROLES);
        query.setParameter("userId", id);
        query.executeUpdate();
    }

}
