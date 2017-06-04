package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsUserRolesRepository {

    private final SessionFactory sessionFactory;

    public CmsUserRolesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<CmsUserRolesEntity> listForUser(Long userId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserRolesEntity.LIST_FOR_USER);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public void add(CmsUserRolesEntity cmsUserRole) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsUserRole);
        currentSession.flush();
    }

    public void deleteForUser(CmsUserRolesEntity cmsUserRole) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserRolesEntity.DELETE_USER_ROLES);
        query.setParameter("userId", cmsUserRole.getCmsUserId());
        query.executeUpdate();
    }

    public void deleteForUser(Long id) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUserRolesEntity.DELETE_USER_ROLES);
        query.setParameter("userId", id);
        query.executeUpdate();
    }

}
