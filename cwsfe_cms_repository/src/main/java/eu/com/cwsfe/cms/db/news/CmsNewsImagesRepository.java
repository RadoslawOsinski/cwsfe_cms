package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsNewsImagesRepository {

    public int getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsNewsImagesEntity> searchByAjaxWithoutContent(Session session, int iDisplayStart, int iDisplayLength, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("newsId", newsId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCountWithoutContent(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY);
        query.setParameter("newsId", newsId);
        return (int) query.getSingleResult();
    }

    public CmsNewsImagesEntity getWithContent(Session session, Long id) {
        return session.get(CmsNewsImagesEntity.class, id);
    }

    public Long add(Session session, CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("NEW");
        CmsNewsImagesEntity saved = (CmsNewsImagesEntity) session.save(cmsNewsImage);
        return saved.getId();
    }

    public void delete(Session session, CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("DELETE");
        session.update(cmsNewsImage);
    }

    public void undelete(Session session, CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("NEW");
        session.update(cmsNewsImage);
    }

    public List<CmsNewsImagesEntity> listImagesForNewsWithoutThumbnails(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.LIST_IMAGES_FOR_NEWS_WITHOUT_THUBNAILS);
        query.setParameter("newsId", newsId);
        return query.getResultList();
    }

    public CmsNewsImagesEntity getThumbnailForNews(Session session, Long newsId) {
        Query query = session.getNamedQuery(CmsNewsImagesEntity.GET_THUBNAIL_FOR_NEWS);
        query.setParameter("newsId", newsId);
        return (CmsNewsImagesEntity) query.getSingleResult();
    }

    public CmsNewsImagesEntity get(Session session, Long id) {
        return session.get(CmsNewsImagesEntity.class, id);
    }

    public void updateUrl(Session session, CmsNewsImagesEntity cmsNewsImage) {
        session.update(cmsNewsImage);
    }
}
