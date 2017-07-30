package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsFoldersRepository {

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsFoldersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsFoldersEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsFoldersEntity.LIST);
        return query.list();
    }

    public List<CmsFoldersEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsFoldersEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsFoldersEntity> listFoldersForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsFoldersEntity.LIST_FOLDERS_FOR_DROP_LIST);
        query.setParameter("folderName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsFoldersEntity get(Session session, Long id) {
        return session.get(CmsFoldersEntity.class, id);
    }

    public CmsFoldersEntity getByFolderName(Session session, String folderName) {
        Query query = session.getNamedQuery(CmsFoldersEntity.GET_BY_FOLDER_NAME);
        query.setParameter("folderName", folderName);
        return (CmsFoldersEntity) query.getSingleResult();
    }

    public Long add(Session session, CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("NEW");
        session.saveOrUpdate(cmsFolder);
        session.flush();
        return cmsFolder.getId();
    }

    public void update(Session session, CmsFoldersEntity cmsFolder) {
        session.update(cmsFolder);
    }

    public void delete(Session session, CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("DELETED");
        session.update(cmsFolder);
    }

    public void undelete(Session session, CmsFoldersEntity cmsFolder) {
        cmsFolder.setStatus("NEW");
        session.update(cmsFolder);
    }

}
