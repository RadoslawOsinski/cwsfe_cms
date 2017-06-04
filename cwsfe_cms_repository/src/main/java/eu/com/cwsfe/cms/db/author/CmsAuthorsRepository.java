package eu.com.cwsfe.cms.db.author;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsAuthorsRepository {

    private final SessionFactory sessionFactory;

    public CmsAuthorsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsAuthorsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsAuthorsEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsAuthorsEntity.LIST);
        return query.list();
    }

    public List<CmsAuthorsEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsAuthorsEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsAuthorsEntity> listAuthorsForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsAuthorsEntity.LIST_AUTHORS_FOR_DROP_LIST);
        query.setParameter("firstName", '%' + term + '%');
        query.setParameter("lastName", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsAuthorsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsAuthorsEntity.class, id);
    }

    public Long add(CmsAuthorsEntity cmsAuthor) {
        cmsAuthor.setStatus(NewDeletedStatus.NEW);
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsAuthor);
        currentSession.flush();
        return cmsAuthor.getId();
    }

    public void update(CmsAuthorsEntity cmsAuthor) {
        sessionFactory.getCurrentSession().update(cmsAuthor);
    }

    public void delete(CmsAuthorsEntity cmsAuthor) {
        cmsAuthor.setStatus(NewDeletedStatus.DELETED);
        sessionFactory.getCurrentSession().update(cmsAuthor);
    }

    public void undelete(CmsAuthorsEntity cmsAuthor) {
        cmsAuthor.setStatus(NewDeletedStatus.NEW);
        sessionFactory.getCurrentSession().update(cmsAuthor);
    }

}
