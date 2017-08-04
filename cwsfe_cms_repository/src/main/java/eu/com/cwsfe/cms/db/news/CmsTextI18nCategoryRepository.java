package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsTextI18nCategoryRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsTextI18NCategoriesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public List<CmsTextI18NCategoriesEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsTextI18NCategoriesEntity.LIST);
        return query.list();
    }

    public List<CmsTextI18NCategoriesEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsTextI18NCategoriesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsTextI18NCategoriesEntity> listForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsTextI18NCategoriesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("category", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsTextI18NCategoriesEntity get(Session session, Long id) {
        return session.get(CmsTextI18NCategoriesEntity.class, id);
    }

    public Long add(Session session, CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategory.setStatus("NEW");
        session.saveOrUpdate(cmsTextI18nCategory);
        session.flush();
        return cmsTextI18nCategory.getId();
    }

    public void update(Session session, CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        session.update(cmsTextI18nCategory);
    }

    public void delete(Session session, CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        CmsTextI18NCategoriesEntity cmsTextI18NCategoriesEntity = session.get(CmsTextI18NCategoriesEntity.class, cmsTextI18nCategory.getId());
        cmsTextI18NCategoriesEntity.setStatus("DELETED");
        session.update(cmsTextI18NCategoriesEntity);
    }

    public void undelete(Session session, CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        CmsTextI18NCategoriesEntity cmsTextI18NCategoriesEntity = session.get(CmsTextI18NCategoriesEntity.class, cmsTextI18nCategory.getId());
        cmsTextI18NCategoriesEntity.setStatus("NEW");
        session.update(cmsTextI18NCategoriesEntity);
    }

}
