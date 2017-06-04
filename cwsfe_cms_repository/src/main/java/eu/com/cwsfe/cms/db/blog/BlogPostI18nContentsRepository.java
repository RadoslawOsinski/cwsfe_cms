package eu.com.cwsfe.cms.db.blog;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BlogPostI18nContentsRepository {

    private final SessionFactory sessionFactory;

    public BlogPostI18nContentsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public BlogPostI18NContentsEntity get(Long id) {
        return sessionFactory.getCurrentSession().get(BlogPostI18NContentsEntity.class, id);
    }

    public BlogPostI18NContentsEntity getByLanguageForPost(Long postId, Long languageId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(BlogPostI18NContentsEntity.GET_BY_LANGUAGE_FOR_POST);
        query.setParameter("languageId", languageId);
        query.setParameter("postId", postId);
        return (BlogPostI18NContentsEntity) query.getSingleResult();
    }

    public Long add(BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("HIDDEN");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(blogPostI18nContent);
        currentSession.flush();
        return blogPostI18nContent.getId();
    }

    public void updateContentWithStatus(BlogPostI18NContentsEntity blogPostI18nContent) {
        sessionFactory.getCurrentSession().update(blogPostI18nContent);
    }

    public void delete(BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(blogPostI18nContent);
    }

    public void undelete(BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("HIDDEN");
        sessionFactory.getCurrentSession().update(blogPostI18nContent);
    }

    public void publish(BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("PUBLISHED");
        sessionFactory.getCurrentSession().update(blogPostI18nContent);
    }

}
