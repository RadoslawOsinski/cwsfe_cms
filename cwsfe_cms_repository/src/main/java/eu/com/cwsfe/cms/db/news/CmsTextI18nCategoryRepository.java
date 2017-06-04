package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsTextI18nCategoryRepository {

    private final SessionFactory sessionFactory;

    public CmsTextI18nCategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NCategoriesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsTextI18NCategoriesEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NCategoriesEntity.LIST);
        return query.list();
    }

    public List<CmsTextI18NCategoriesEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NCategoriesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsTextI18NCategoriesEntity> listForDropList(String term, int limit) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NCategoriesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("category", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsTextI18NCategoriesEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsTextI18NCategoriesEntity.class, id);
    }

    public Long add(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategory.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsTextI18nCategory);
        currentSession.flush();
        return cmsTextI18nCategory.getId();
    }

    public void update(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        sessionFactory.getCurrentSession().update(cmsTextI18nCategory);
    }

    public void delete(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategory.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(cmsTextI18nCategory);
    }

    public void undelete(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategory.setStatus("NEW");
        sessionFactory.getCurrentSession().update(cmsTextI18nCategory);
    }

}
