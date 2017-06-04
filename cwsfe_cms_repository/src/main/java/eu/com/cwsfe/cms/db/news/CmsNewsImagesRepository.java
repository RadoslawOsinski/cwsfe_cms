package eu.com.cwsfe.cms.db.news;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsNewsImagesRepository {

    private final SessionFactory sessionFactory;

    public CmsNewsImagesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getTotalNumberNotDeleted() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsNewsImagesEntity> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long newsId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("newsId", newsId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCountWithoutContent(Long newsId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY);
        query.setParameter("newsId", newsId);
        return (int) query.getSingleResult();
    }

    public CmsNewsImagesEntity getWithContent(Long id) {
        return sessionFactory.getCurrentSession().get(CmsNewsImagesEntity.class, id);
    }

    public Long add(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("NEW");
        CmsNewsImagesEntity saved = (CmsNewsImagesEntity) sessionFactory.getCurrentSession().save(cmsNewsImage);
        return saved.getId();
    }

    public void delete(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("DELETE");
        sessionFactory.getCurrentSession().update(cmsNewsImage);
    }

    public void undelete(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImage.setStatus("NEW");
        sessionFactory.getCurrentSession().update(cmsNewsImage);
    }

    public List<CmsNewsImagesEntity> listImagesForNewsWithoutThumbnails(Long newsId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.LIST_IMAGES_FOR_NEWS_WITHOUT_THUBNAILS);
        query.setParameter("newsId", newsId);
        return query.getResultList();
    }

    public CmsNewsImagesEntity getThumbnailForNews(Long newsId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsNewsImagesEntity.GET_THUBNAIL_FOR_NEWS);
        query.setParameter("newsId", newsId);
        return (CmsNewsImagesEntity) query.getSingleResult();
    }

    public CmsNewsImagesEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsNewsImagesEntity.class, id);
    }

    public void updateUrl(CmsNewsImagesEntity cmsNewsImage) {
        sessionFactory.getCurrentSession().update(cmsNewsImage);
    }
}
