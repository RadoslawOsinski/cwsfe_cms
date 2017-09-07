package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CmsNewsImagesRepository {

    public Long getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public List<CmsNewsImagesEntity> searchByAjaxWithoutContent(Session session, int iDisplayStart, int iDisplayLength, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("newsId", newsId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public Long searchByAjaxCountWithoutContent(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY);
        query.setParameter("newsId", newsId);
        return (Long) query.getSingleResult();
    }

    public CmsNewsImagesEntity getWithContent(Session session, Long id) {
        return session.get(CmsNewsImagesEntity.class, id);
    }

    public Long add(Session session, CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("NEW");
        return (Long) session.save(cmsNewsImage);
    }

    public void delete(Session session, CmsNewsImagesEntity cmsNewsImage) {
        CmsNewsImagesEntity dbCmsNewsImage = session.get(CmsNewsImagesEntity.class, cmsNewsImage.getId());
        dbCmsNewsImage.setStatus("DELETED");
        session.update(dbCmsNewsImage);
    }

    public void undelete(Session session, CmsNewsImagesEntity cmsNewsImage) {
        CmsNewsImagesEntity dbCmsNewsImage = session.get(CmsNewsImagesEntity.class, cmsNewsImage.getId());
        dbCmsNewsImage.setStatus("NEW");
        session.update(dbCmsNewsImage);
    }

    public List<CmsNewsImagesEntity> listImagesForNewsWithoutThumbnails(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.LIST_IMAGES_FOR_NEWS_WITHOUT_THUBNAILS);
        query.setParameter("newsId", newsId);
        return query.getResultList();
    }

    public Optional<CmsNewsImagesEntity> getThumbnailForNews(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.GET_THUBNAIL_FOR_NEWS);
        query.setParameter("newsId", newsId);
        return (Optional<CmsNewsImagesEntity>) query.uniqueResultOptional();
    }

    public CmsNewsImagesEntity get(Session session, Long id) {
        return session.get(CmsNewsImagesEntity.class, id);
    }

    public void updateUrl(Session session, CmsNewsImagesEntity cmsNewsImage) {
        session.update(cmsNewsImage);
    }
}
