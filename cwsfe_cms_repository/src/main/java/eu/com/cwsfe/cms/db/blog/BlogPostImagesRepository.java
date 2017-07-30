package eu.com.cwsfe.cms.db.blog;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogPostImagesRepository {

    public int getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(BlogPostImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(BlogPostImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<BlogPostImagesEntity> searchByAjaxWithoutContent(Session session, int iDisplayStart, int iDisplayLength, Long postId) {
        Query query = session.getNamedQuery(BlogPostImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("postId", postId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCountWithoutContent(Session session, Long postId) {
        Query query = session.getNamedQuery(BlogPostImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY);
        query.setParameter("postId", postId);
        return (int) query.getSingleResult();
    }

    public List<BlogPostImagesEntity> listForPost(Session session, Long postId) {
        Query query = session.getNamedQuery(BlogPostImagesEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    public BlogPostImagesEntity getWithContent(Session session, Long id) {
        return session.get(BlogPostImagesEntity.class, id);
    }

    public Long add(Session session, BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("NEW");
        BlogPostImagesEntity saved = (BlogPostImagesEntity) session.save(blogPostImage);
        return saved.getId();
    }

    public void update(Session session, BlogPostImagesEntity blogPostImage) {
        session.update(blogPostImage);
    }

    public void delete(Session session, BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("DELETED");
        session.update(blogPostImage);
    }

    public void undelete(Session session, BlogPostImagesEntity blogPostImage) {
        blogPostImage.setStatus("NEW");
        session.update(blogPostImage);
    }

    public void updateUrl(Session session, BlogPostImagesEntity blogPostImage) {
        session.update(blogPostImage);
    }

}
