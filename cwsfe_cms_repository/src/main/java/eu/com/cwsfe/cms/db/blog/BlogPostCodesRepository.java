package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogPostCodesRepository {

    public int getTotalNumberNotDeleted(Session session) {
        Query query = session.getNamedQuery(BlogPostCodeEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax(Session session) {
        return getTotalNumberNotDeleted(session);
    }

    public List<BlogPostCodeEntity> searchByAjax(Session session, int iDisplayStart, int iDisplayLength, Long postId) {
        Query query = session.getNamedQuery(BlogPostCodeEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("blogPostId", postId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCount(Session session, Long postId) {
        Query query = session.getNamedQuery(BlogPostCodeEntity.SEARCH_BY_AJAX_COUNT_QUERY);
        query.setParameter("blogPostId", postId);
        return (int) query.getSingleResult();
    }

    public BlogPostCodeEntity getCodeForPostByCodeId(Session session, Long postId, String codeId) {
        Query query = session.getNamedQuery(BlogPostCodeEntity.CODE_FOR_POST_BY_CODE_ID_QUERY);
        query.setParameter("blogPostId", postId);
        query.setParameter("codeId", codeId);
        return (BlogPostCodeEntity) query.getSingleResult();
    }

    public void add(Session session, BlogPostCodeEntity blogPostCode) {
        blogPostCode.setStatus(NewDeletedStatus.NEW);
        session.save(blogPostCode);
    }

    public void update(Session session, BlogPostCodeEntity blogPostCode) {
        session.update(blogPostCode);
    }

    public void delete(Session session, BlogPostCodeEntity blogPostCode) {
        Query query = session.getNamedQuery(BlogPostCodeEntity.DELETE_QUERY);
        query.setParameter("blogPostId", blogPostCode.getBlogPostId());
        query.setParameter("codeId", blogPostCode.getCode());
        query.executeUpdate();
    }

}
