package eu.com.cwsfe.cms.db.blog;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogPostImagesRepository {

    private final SessionFactory sessionFactory;

    public BlogPostImagesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getTotalNumberNotDeleted() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<BlogPostImagesEntity> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long postId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("postId", postId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCountWithoutContent(Long postId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY);
        query.setParameter("postId", postId);
        return (int) query.getSingleResult();
    }

    public List<BlogPostImagesEntity> listForPost(Long postId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    public BlogPostImagesEntity getWithContent(Long id) {
        return sessionFactory.getCurrentSession().get(BlogPostImagesEntity.class, id);
    }

    public Long add(BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("NEW");
        BlogPostImagesEntity saved = (BlogPostImagesEntity) sessionFactory.getCurrentSession().save(blogPostImage);
        return saved.getId();
    }

    public void update(BlogPostImagesEntity blogPostImage) {
        sessionFactory.getCurrentSession().update(blogPostImage);
    }

    public void delete(BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(blogPostImage);
    }

    public void undelete(BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("NEW");
        sessionFactory.getCurrentSession().update(blogPostImage);
    }

    public void updateUrl(BlogPostImagesEntity blogPostImage) {
        sessionFactory.getCurrentSession().update(blogPostImage);
    }

}
