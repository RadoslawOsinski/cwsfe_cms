package eu.com.cwsfe.cms.db.blog;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BlogPostCommentsRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BlogPostCommentsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getTotalNumberNotDeleted() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsBlogPostCommentsEntity.COUNT_FOR_AJAX_N);
        return (Integer) query.uniqueResult();
    }

    public List<CmsBlogPostCommentsEntity> listPublishedForPostI18nContent(Long blogPostI18nContentId) {
        Query namedQuery = sessionFactory.getCurrentSession().getNamedQuery(CmsBlogPostCommentsEntity.LIST_PUBLISHED_FOR_POST_I18N_CONTENT);
        namedQuery.setParameter("blogPostI18NContentId", blogPostI18nContentId);
        return namedQuery.list();
    }

    public List<CmsBlogPostCommentsEntity> searchByAjax(
        int iDisplayStart, int iDisplayLength
    ) {
        Query namedQuery = sessionFactory.getCurrentSession().getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX);
        namedQuery.setFetchSize(iDisplayLength);
        namedQuery.setFirstResult(iDisplayStart);
        return namedQuery.list();
    }

    public int searchByAjaxCount() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX_COUNT);
        return (Integer) query.uniqueResult();
    }

    public CmsBlogPostCommentsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(CmsBlogPostCommentsEntity.class, id);
    }

    public Long add(CmsBlogPostCommentsEntity blogPostComment) {
        if (blogPostComment.getCreated() == null) {
            blogPostComment.setCreated(LocalDateTime.now());
        }
        blogPostComment.setStatus("N");
        CmsBlogPostCommentsEntity savedBlogPostComment = (CmsBlogPostCommentsEntity) sessionFactory.getCurrentSession().save(blogPostComment);
        return savedBlogPostComment.getId();
    }

    public void update(CmsBlogPostCommentsEntity blogPostComment) {
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public void delete(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("D");
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public void undelete(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("N");
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public void publish(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("P");
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public void block(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("B");
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public void markAsSpam(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("S");
        sessionFactory.getCurrentSession().update(blogPostComment);
    }

    public int countCommentsForPostI18n(long blogPostI18nContentId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX_COUNT);
        query.setParameter("blogPostI18NContentId", blogPostI18nContentId);
        return (Integer) query.uniqueResult();
    }

}
