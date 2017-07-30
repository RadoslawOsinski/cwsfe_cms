package eu.com.cwsfe.cms.services.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostCommentsRepository;
import eu.com.cwsfe.cms.db.blog.CmsBlogPostCommentsEntity;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class BlogPostCommentsService {

    private final BlogPostCommentsRepository blogPostCommentsRepository;
    private final SessionFactory sessionFactory;

    public BlogPostCommentsService(BlogPostCommentsRepository blogPostCommentsRepository, SessionFactory sessionFactory) {
        this.blogPostCommentsRepository = blogPostCommentsRepository;
        this.sessionFactory = sessionFactory;
    }

    public List<CmsBlogPostCommentsEntity> searchByAjax(int iDisplayStart, int iDisplayLength) {
        return blogPostCommentsRepository.searchByAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public Integer searchByAjaxCount() {
        return blogPostCommentsRepository.searchByAjaxCount(sessionFactory.getCurrentSession());
    }

    public int getTotalNumberNotDeleted() {
        return blogPostCommentsRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    public void publish(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.publish(sessionFactory.getCurrentSession(), blogPostComment);
    }

    public void block(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.block(sessionFactory.getCurrentSession(), blogPostComment);
    }

    public void markAsSpam(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.markAsSpam(sessionFactory.getCurrentSession(), blogPostComment);
    }

    public void delete(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.delete(sessionFactory.getCurrentSession(), blogPostComment);
    }
}
