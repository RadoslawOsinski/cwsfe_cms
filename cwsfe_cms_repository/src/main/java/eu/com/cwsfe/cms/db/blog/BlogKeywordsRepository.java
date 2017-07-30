package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogKeywordsRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(BlogKeywordsEntity.COUNT_FOR_AJAX_N);
        return (Long) query.uniqueResult();
    }

    public List<BlogKeywordsEntity> list(Session session) {
        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
        return query.list();
    }

    public List<BlogKeywordsEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(BlogKeywordsEntity.LIST_N);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public BlogKeywordsEntity get(Session session, Long id) {
        return session.get(BlogKeywordsEntity.class, id);
    }

    public Long add(Session session, BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        session.saveOrUpdate(blogKeyword);
        session.flush();
        return blogKeyword.getId();
    }

    public void update(Session session, BlogKeywordsEntity blogKeyword) {
        session.update(blogKeyword);
    }

    public void delete(Session session, BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.DELETED);
        session.update(blogKeyword);
    }

    public void undelete(Session session, BlogKeywordsEntity blogKeyword) {
        blogKeyword.setStatus(NewDeletedStatus.NEW);
        session.update(blogKeyword);
    }

}
