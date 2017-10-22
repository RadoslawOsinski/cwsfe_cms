package eu.com.cwsfe.cms.db.news;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import eu.com.cwsfe.cms.db.i18n.CmsLanguagesEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<CmsNewsI18NContentsEntity> get(Session session, Long id) {
        return Optional.ofNullable(session.get(CmsNewsI18NContentsEntity.class, id));
    }

    public Optional<CmsNewsI18NContentsEntity> getByLanguageForNews(Session session, Long newsId, Long languageId) {
        Query query = session.getNamedQuery(CmsNewsI18NContentsEntity.GET_BY_LANGUAGE_FOR_NEWS);
        query.setParameter("newsId", newsId);
        query.setParameter("languageId", languageId);
        return (Optional<CmsNewsI18NContentsEntity>) query.uniqueResultOptional();
    }

    public Long add(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus(PublishedHiddenStatus.HIDDEN);
        session.saveOrUpdate(cmsNewsI18nContent);
        session.flush();
        return cmsNewsI18nContent.getId();
    }

    public void updateContentWithStatus(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        session.update(cmsNewsI18nContent);
    }

    public void delete(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus(PublishedHiddenStatus.DELETED);
        session.update(cmsNewsI18nContent);
    }

    public void undelete(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus(PublishedHiddenStatus.HIDDEN);
        session.update(cmsNewsI18nContent);
    }

    public void publish(Session session, CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContent.setStatus(PublishedHiddenStatus.PUBLISHED);
        session.update(cmsNewsI18nContent);
    }

    public List<CmsNewsI18NContentsEntity> list(Session session, String folderName, String languageCode, String newsType, int newsPerPage, int offset) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<CmsNewsI18NContentsEntity> query = builder.createQuery(CmsNewsI18NContentsEntity.class);
        Root<CmsNewsI18NContentsEntity> cmsNewsI18NContent = query.from(CmsNewsI18NContentsEntity.class);
        Join<CmsNewsI18NContentsEntity, CmsNewsEntity> news = cmsNewsI18NContent.join("cmsNewsEntity");
        Join<CmsNewsEntity, CmsFoldersEntity> folder = news.join("folder");
        Join<CmsNewsEntity, CmsNewsTypesEntity> newsTypeEntity = news.join("type");
        Join<CmsNewsI18NContentsEntity, CmsLanguagesEntity> language = cmsNewsI18NContent.join("language");

        query.select(cmsNewsI18NContent);
        query.where(
            builder.and(
                builder.equal(folder.get("folderName"), folderName),
                builder.equal(folder.get("status"), NewDeletedStatus.NEW),
                builder.equal(language.get("code"), languageCode),
                builder.equal(cmsNewsI18NContent.get("status"), PublishedHiddenStatus.PUBLISHED),
                builder.equal(news.get("status"), PublishedHiddenStatus.PUBLISHED),
                builder.equal(newsTypeEntity.get("type"), newsType)
            )
        );

        List<Order> orders = new ArrayList<>();
        orders.add(builder.asc(cmsNewsI18NContent.get("newsTitle")));
        query.orderBy(orders);

        return session.createQuery(query)
            .setMaxResults(newsPerPage)
            .setFirstResult(offset)
            .list();
    }
}
