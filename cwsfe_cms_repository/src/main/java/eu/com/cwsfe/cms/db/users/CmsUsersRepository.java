package eu.com.cwsfe.cms.db.users;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Repository
public class CmsUsersRepository {

    public boolean isActiveUsernameInDatabase(Session session, String username) {
        Query query = session.getNamedQuery(CmsUsersEntity.IS_USER_ACTIVE);
        query.setParameter("userName", username);
        Integer numberOfUsers = (Integer) query.getSingleResult();
        return numberOfUsers == 1;
    }

    public CmsUsersEntity getByUsername(Session session, String username) {
        Query query = session.getNamedQuery(CmsUsersEntity.GET_BY_USER_NAME);
        query.setParameter("userName", username);
        return (CmsUsersEntity) query.getSingleResult();
    }

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsUsersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsUsersEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsUsersEntity.LIST);
        return query.list();
    }

    public List<CmsUsersEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsUsersEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsUsersEntity> listUsersForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsUsersEntity.LIST_FOR_DROP_LIST);
        query.setParameter("userName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsUsersEntity get(Session session, Long id) {
        return session.get(CmsUsersEntity.class, id);
    }

    public Long add(Session session, CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        session.saveOrUpdate(cmsUser);
        session.flush();
        return cmsUser.getId();
    }

    public void update(Session session, CmsUsersEntity cmsUser) {
        session.update(cmsUser);
    }

    public void updatePostBasicInfo(Session session, CmsUsersEntity cmsUser) {
        session.update(cmsUser);
    }

    public void updateWithoutPassword(Session session, CmsUsersEntity cmsUser) {
        session.update(cmsUser);
    }

    public void delete(Session session, CmsUsersEntity cmsUser) {
        cmsUser.setStatus("DELETED");
        session.update(cmsUser);
    }

    public void undelete(Session session, CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        session.update(cmsUser);
    }

    public void lock(Session session, CmsUsersEntity cmsUser) {
        cmsUser.setStatus("LOCK");
        session.update(cmsUser);
    }

    public void unlock(Session session, CmsUsersEntity cmsUser) {
        cmsUser.setStatus("NEW");
        session.update(cmsUser);
    }

}
