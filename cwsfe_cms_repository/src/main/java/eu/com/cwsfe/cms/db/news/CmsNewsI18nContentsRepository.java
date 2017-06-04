package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsNewsI18nContentsRepository {

    private final SessionFactory sessionFactory;

    public CmsNewsI18nContentsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<CmsNewsI18NContentsEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsI18NContentsEntity.LIST);
        return query.getResultList();
    }

    public List<CmsNewsI18NContentsEntity> listForNews(Long newsId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsI18NContentsEntity.LIST_FOR_NEWS);
        query.setParameter("newsId", newsId);
        return query.getResultList();
    }

    public CmsNewsI18NContentsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsNewsI18NContentsEntity.class, id);
    }

    public CmsNewsI18NContentsEntity getByLanguageForNews(Long newsId, Long languageId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsI18NContentsEntity.GET_BY_LANGUAGE_FOR_NEWS);
        query.setParameter("newsId", newsId);
        query.setParameter("languageId", languageId);
        return (CmsNewsI18NContentsEntity) query.getSingleResult();
    }

    public Long add(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("HIDDEN");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(cmsNewsI18nContent);
        currentSession.flush();
        return cmsNewsI18nContent.getId();
    }

    public void updateContentWithStatus(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        sessionFactory.getCurrentSession().update(cmsNewsI18nContent);
    }

    public void delete(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(cmsNewsI18nContent);
    }

    public void undelete(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("HIDDEN");
        sessionFactory.getCurrentSession().update(cmsNewsI18nContent);
    }

    public void publish(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("PUBLISHED");
        sessionFactory.getCurrentSession().update(cmsNewsI18nContent);
    }

}
