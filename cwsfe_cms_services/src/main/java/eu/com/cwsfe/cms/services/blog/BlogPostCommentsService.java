package eu.com.cwsfe.cms.services.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostCommentsRepository;
import eu.com.cwsfe.cms.db.blog.CmsBlogPostCommentsEntity;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<CmsBlogPostCommentsEntity> searchByAjax(int iDisplayStart, int iDisplayLength) {
        return blogPostCommentsRepository.searchByAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Integer searchByAjaxCount() {
        return blogPostCommentsRepository.searchByAjaxCount(sessionFactory.getCurrentSession());
    }

    @Transactional
    public int getTotalNumberNotDeleted() {
        return blogPostCommentsRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    @Transactional
    public void publish(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.publish(sessionFactory.getCurrentSession(), blogPostComment);
    }

    @Transactional
    public void block(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.block(sessionFactory.getCurrentSession(), blogPostComment);
    }

    @Transactional
    public void markAsSpam(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.markAsSpam(sessionFactory.getCurrentSession(), blogPostComment);
    }

    @Transactional
    public void delete(CmsBlogPostCommentsEntity blogPostComment) {
        blogPostCommentsRepository.delete(sessionFactory.getCurrentSession(), blogPostComment);
    }
}
