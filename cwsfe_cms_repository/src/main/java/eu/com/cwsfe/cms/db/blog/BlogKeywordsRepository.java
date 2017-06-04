package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogKeywordsRepository {

    private final SessionFactory sessionFactory;

    public BlogKeywordsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogKeywordsEntity.COUNT_FOR_AJAX_N);
        return (Long) query.uniqueResult();
    }

    public List<BlogKeywordsEntity> list() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogKeywordsEntity.LIST_N);
        return query.list();
    }

    public List<BlogKeywordsEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogKeywordsEntity.LIST_N);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public BlogKeywordsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(BlogKeywordsEntity.class, id);
    }

    public Long add(BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(blogKeyword);
        currentSession.flush();
        return blogKeyword.getId();
    }

    public void update(BlogKeywordsEntity blogKeyword) {
        sessionFactory.getCurrentSession().update(blogKeyword);
    }

    public void delete(BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.DELETED);
        sessionFactory.getCurrentSession().update(blogKeyword);
    }

    public void undelete(BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        sessionFactory.getCurrentSession().update(blogKeyword);
    }

}
