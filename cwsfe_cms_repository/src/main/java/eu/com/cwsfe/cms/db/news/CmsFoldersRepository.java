package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsFoldersRepository {

    private final SessionFactory sessionFactory;

    public CmsFoldersRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsFoldersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsFoldersEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsFoldersEntity.LIST);
        return query.list();
    }

    public List<CmsFoldersEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsFoldersEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsFoldersEntity> listFoldersForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsFoldersEntity.LIST_FOLDERS_FOR_DROP_LIST);
        query.setParameter("folderName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsFoldersEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsFoldersEntity.class, id);
    }

    public CmsFoldersEntity getByFolderName(String folderName) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsFoldersEntity.GET_BY_FOLDER_NAME);
        query.setParameter("folderName", folderName);
        return (CmsFoldersEntity) query.getSingleResult();
    }

    public Long add(CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsFolder);
        currentSession.flush();
        return cmsFolder.getId();
    }

    public void update(CmsFoldersEntity cmsFolder) {
        sessionFactory.getCurrentSession().update(cmsFolder);
    }

    public void delete(CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(cmsFolder);
    }

    public void undelete(CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("NEW");
        sessionFactory.getCurrentSession().update(cmsFolder);
    }

}
