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
public class CmsUsersRepository {

    private final SessionFactory sessionFactory;

    public CmsUsersRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean isActiveUsernameInDatabase(String username) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.IS_USER_ACTIVE);
        query.setParameter("userName", username);
        Integer numberOfUsers = (Integer) query.getSingleResult();
        return numberOfUsers == 1;
    }

    public CmsUsersEntity getByUsername(String username) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.GET_BY_USER_NAME);
        query.setParameter("userName", username);
        return (CmsUsersEntity) query.getSingleResult();
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsUsersEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.LIST);
        return query.list();
    }

    public List<CmsUsersEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsUsersEntity> listUsersForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsUsersEntity.LIST_FOR_DROP_LIST);
        query.setParameter("userName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsUsersEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsUsersEntity.class, id);
    }

    public Long add(CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsUser);
        currentSession.flush();
        return cmsUser.getId();
    }

    public void update(CmsUsersEntity cmsUser) {
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void updatePostBasicInfo(CmsUsersEntity cmsUser) {
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void updateWithoutPassword(CmsUsersEntity cmsUser) {
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void delete(CmsUsersEntity cmsUser) {
        cmsUser.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void undelete(CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void lock(CmsUsersEntity cmsUser) {
        cmsUser.setStatus("LOCK");
        sessionFactory.getCurrentSession().update(cmsUser);
    }

    public void unlock(CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        sessionFactory.getCurrentSession().update(cmsUser);
    }

}
