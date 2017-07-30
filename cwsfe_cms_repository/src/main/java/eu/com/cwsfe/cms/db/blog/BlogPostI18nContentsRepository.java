package eu.com.cwsfe.cms.db.blog;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BlogPostI18nContentsRepository {

    public BlogPostI18NContentsEntity get(Session session, Long id) {
        return session.get(BlogPostI18NContentsEntity.class, id);
    }

    public BlogPostI18NContentsEntity getByLanguageForPost(Session session, Long postId, Long languageId) {
        Query query = session.getNamedQuery(BlogPostI18NContentsEntity.GET_BY_LANGUAGE_FOR_POST);
        query.setParameter("languageId", languageId);
        query.setParameter("postId", postId);
        return (BlogPostI18NContentsEntity) query.getSingleResult();
    }

    public Long add(Session session, BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("HIDDEN");
        session.saveOrUpdate(blogPostI18nContent);
        session.flush();
        return blogPostI18nContent.getId();
    }

    public void updateContentWithStatus(Session session, BlogPostI18NContentsEntity blogPostI18nContent) {
        session.update(blogPostI18nContent);
    }

    public void delete(Session session, BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("DELETED");
        session.update(blogPostI18nContent);
    }

    public void undelete(Session session, BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("HIDDEN");
        session.update(blogPostI18nContent);
    }

    public void publish(Session session, BlogPostI18NContentsEntity blogPostI18nContent) {
        blogPostI18nContent.setStatus("PUBLISHED");
        session.update(blogPostI18nContent);
    }

}
