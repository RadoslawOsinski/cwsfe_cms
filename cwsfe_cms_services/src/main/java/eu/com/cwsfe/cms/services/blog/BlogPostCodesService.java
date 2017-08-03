package eu.com.cwsfe.cms.services.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostCodeEntity;
import eu.com.cwsfe.cms.db.blog.BlogPostCodesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class BlogPostCodesService {

    private final BlogPostCodesRepository blogPostCodesRepository;
    private final SessionFactory sessionFactory;

    public BlogPostCodesService(BlogPostCodesRepository blogPostCodesRepository, SessionFactory sessionFactory) {
        this.blogPostCodesRepository = blogPostCodesRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<BlogPostCodeEntity> searchByAjax(int iDisplayStart, int iDisplayLength, Long blogPostId) {
        return blogPostCodesRepository.searchByAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength, blogPostId);
    }

    @Transactional
    public Integer searchByAjaxCount(Long blogPostId) {
        return blogPostCodesRepository.searchByAjaxCount(sessionFactory.getCurrentSession(), blogPostId);
    }

    @Transactional
    public Integer getTotalNumberNotDeleted() {
        return blogPostCodesRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    @Transactional
    public BlogPostCodeEntity getCodeForPostByCodeId(Long blogPostId, String codeId) {
        return blogPostCodesRepository.getCodeForPostByCodeId(sessionFactory.getCurrentSession(), blogPostId, codeId);
    }

    @Transactional
    public void add(BlogPostCodeEntity blogPostCode) {
        blogPostCodesRepository.add(sessionFactory.getCurrentSession(), blogPostCode);
    }

    @Transactional
    public void delete(BlogPostCodeEntity blogPostCode) {
        blogPostCodesRepository.delete(sessionFactory.getCurrentSession(), blogPostCode);
    }
}
