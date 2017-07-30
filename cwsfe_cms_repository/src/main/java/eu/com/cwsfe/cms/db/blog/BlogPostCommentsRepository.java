package eu.com.cwsfe.cms.db.blog;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BlogPostCommentsRepository {

    public int getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(CmsBlogPostCommentsEntity.COUNT_FOR_AJAX_N);
        return (Integer) query.uniqueResult();
    }

    public List<CmsBlogPostCommentsEntity> listPublishedForPostI18nContent(Session session, Long blogPostI18nContentId) {
        Query namedQuery = session.getNamedQuery(CmsBlogPostCommentsEntity.LIST_PUBLISHED_FOR_POST_I18N_CONTENT);
        namedQuery.setParameter("blogPostI18NContentId", blogPostI18nContentId);
        return namedQuery.list();
    }

    public List<CmsBlogPostCommentsEntity> searchByAjax(
        Session session, int iDisplayStart, int iDisplayLength
    ) {
        Query namedQuery = session.getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX);
        namedQuery.setFetchSize(iDisplayLength);
        namedQuery.setFirstResult(iDisplayStart);
        return namedQuery.list();
    }

    public int searchByAjaxCount(Session session) {
        Query query = session.getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX_COUNT);
        return (Integer) query.uniqueResult();
    }

    public CmsBlogPostCommentsEntity get(Session session, Long id) {
        return session.get(CmsBlogPostCommentsEntity.class, id);
    }

    public Long add(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        if (blogPostComment.getCreated() == null) {
            blogPostComment.setCreated(LocalDateTime.now());
        }
        blogPostComment.setStatus("N");
        CmsBlogPostCommentsEntity savedBlogPostComment = (CmsBlogPostCommentsEntity) session.save(blogPostComment);
        return savedBlogPostComment.getId();
    }

    public void update(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        session.update(blogPostComment);
    }

    public void delete(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("D");
        session.update(blogPostComment);
    }

    public void undelete(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("N");
        session.update(blogPostComment);
    }

    public void publish(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("P");
        session.update(blogPostComment);
    }

    public void block(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("B");
        session.update(blogPostComment);
    }

    public void markAsSpam(Session session, CmsBlogPostCommentsEntity blogPostComment) {
        blogPostComment.setStatus("S");
        session.update(blogPostComment);
    }

    public int countCommentsForPostI18n(Session session, long blogPostI18nContentId) {
        Query query = session.getNamedQuery(CmsBlogPostCommentsEntity.SEARCH_BY_AJAX_COUNT);
        query.setParameter("blogPostI18NContentId", blogPostI18nContentId);
        return (Integer) query.uniqueResult();
    }

}
