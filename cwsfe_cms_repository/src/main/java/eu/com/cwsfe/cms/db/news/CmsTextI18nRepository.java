package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsTextI18nRepository {

    private final SessionFactory sessionFactory;

    public CmsTextI18nRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsTextI18NEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NEntity.LIST);
        return query.list();
    }

    public List<CmsTextI18NEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public CmsTextI18NEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsTextI18NEntity.class, id);
    }

    public String findTranslation(String language2LetterCode, String category, String key) {
        //TODO BY CRITERIA
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsTextI18NEntity.FIND_TRANSLATION);
        query.setParameter("language2LetterCode", language2LetterCode);
        query.setParameter("category", category);
        query.setParameter("key", key);
        return (String) query.getSingleResult();
    }

    public Long add(CmsTextI18NEntity cmsTextI18n) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsTextI18n);
        currentSession.flush();
        return cmsTextI18n.getId();
    }

    public void update(CmsTextI18NEntity cmsTextI18n) {
        sessionFactory.getCurrentSession().update(cmsTextI18n);
    }

    public void delete(CmsTextI18NEntity cmsTextI18n) {
        sessionFactory.getCurrentSession().delete(cmsTextI18n);
    }

}
