package eu.com.cwsfe.cms.services.blog;

import eu.com.cwsfe.cms.db.blog.BlogPostImagesEntity;
import eu.com.cwsfe.cms.db.blog.BlogPostImagesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class BlogPostImagesService {

    private final BlogPostImagesRepository blogPostImagesRepository;
    private final SessionFactory sessionFactory;

    public BlogPostImagesService(BlogPostImagesRepository blogPostImagesRepository, SessionFactory sessionFactory) {
        this.blogPostImagesRepository = blogPostImagesRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<BlogPostImagesEntity> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long blogPostId) {
        return blogPostImagesRepository.searchByAjaxWithoutContent(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength, blogPostId);
    }

    @Transactional
    public Integer searchByAjaxCountWithoutContent(Long blogPostId) {
        return blogPostImagesRepository.searchByAjaxCountWithoutContent(sessionFactory.getCurrentSession(), blogPostId);
    }

    @Transactional
    public int getTotalNumberNotDeleted() {
        return blogPostImagesRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    @Transactional
    public long add(BlogPostImagesEntity blogPostImage) {
        return blogPostImagesRepository.add(sessionFactory.getCurrentSession(), blogPostImage);
    }

    @Transactional
    public void updateUrl(BlogPostImagesEntity blogPostImage) {
        blogPostImagesRepository.updateUrl(sessionFactory.getCurrentSession(), blogPostImage);
    }

    @Transactional
    public void delete(BlogPostImagesEntity blogPostImage) {
        blogPostImagesRepository.delete(sessionFactory.getCurrentSession(), blogPostImage);
    }
}
