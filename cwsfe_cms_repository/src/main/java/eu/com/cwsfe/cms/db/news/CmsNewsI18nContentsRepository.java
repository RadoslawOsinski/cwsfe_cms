package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsNewsI18nContentsRepository {

    public List<CmsNewsI18NContentsEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsNewsI18NContentsEntity.LIST);
        return query.getResultList();
    }

    public List<CmsNewsI18NContentsEntity> listForNews(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsI18NContentsEntity.LIST_FOR_NEWS);
        query.setParameter("newsId", newsId);
        return query.getResultList();
    }

    public CmsNewsI18NContentsEntity get(Session session, Long id) {
        return session.get(CmsNewsI18NContentsEntity.class, id);
    }

    public CmsNewsI18NContentsEntity getByLanguageForNews(Session session, Long newsId, Long languageId) {
        Query query = session.getNamedQuery(CmsNewsI18NContentsEntity.GET_BY_LANGUAGE_FOR_NEWS);
        query.setParameter("newsId", newsId);
        query.setParameter("languageId", languageId);
        return (CmsNewsI18NContentsEntity) query.getSingleResult();
    }

    public Long add(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("HIDDEN");
        session.saveOrUpdate(cmsNewsI18nContent);
        session.flush();
        return cmsNewsI18nContent.getId();
    }

    public void updateContentWithStatus(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        session.update(cmsNewsI18nContent);
    }

    public void delete(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("DELETED");
        session.update(cmsNewsI18nContent);
    }

    public void undelete(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("HIDDEN");
        session.update(cmsNewsI18nContent);
    }

    public void publish(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus("PUBLISHED");
        session.update(cmsNewsI18nContent);
    }

}
