package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogPostCodesRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BlogPostCodesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getTotalNumberNotDeleted() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostCodeEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public int countForAjax() {
        return getTotalNumberNotDeleted();
    }

    public List<BlogPostCodeEntity> searchByAjax(int iDisplayStart, int iDisplayLength, Long postId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostCodeEntity.SEARCH_BY_AJAX_QUERY);
        query.setParameter("blogPostId", postId);
        query.setMaxResults(iDisplayLength);
        query.setFirstResult(iDisplayStart);
        return query.getResultList();
    }

    public int searchByAjaxCount(Long postId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostCodeEntity.SEARCH_BY_AJAX_COUNT_QUERY);
        query.setParameter("blogPostId", postId);
        return (int) query.getSingleResult();
    }

    public BlogPostCodeEntity getCodeForPostByCodeId(Long postId, String codeId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostCodeEntity.CODE_FOR_POST_BY_CODE_ID_QUERY);
        query.setParameter("blogPostId", postId);
        query.setParameter("codeId", codeId);
        return (BlogPostCodeEntity) query.getSingleResult();
    }

    public void add(BlogPostCodeEntity blogPostCode) {
        blogPostCode.setStatus(NewDeletedStatus.NEW);
        sessionFactory.getCurrentSession().save(blogPostCode);
    }

    public void update(BlogPostCodeEntity blogPostCode) {
        sessionFactory.getCurrentSession().update(blogPostCode);
    }

    public void delete(BlogPostCodeEntity blogPostCode) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostCodeEntity.DELETE_QUERY);
        query.setParameter("blogPostId", blogPostCode.getBlogPostId());
        query.setParameter("codeId", blogPostCode.getCode());
        query.executeUpdate();
    }

}
