package eu.com.cwsfe.cms.services.blog;

import eu.com.cwsfe.cms.db.blog.BlogKeywordsEntity;
import eu.com.cwsfe.cms.db.blog.BlogKeywordsRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radoslaw Osinski.
 */
@Service
public class BlogKeywordsService {

    private final BlogKeywordsRepository blogKeywordsRepository;
    private final SessionFactory sessionFactory;

    public BlogKeywordsService(BlogKeywordsRepository blogKeywordsRepository, SessionFactory sessionFactory) {
        this.blogKeywordsRepository = blogKeywordsRepository;
        this.sessionFactory = sessionFactory;
    }

    public List<BlogKeywordsEntity> list() {
        return blogKeywordsRepository.list(sessionFactory.getCurrentSession());
    }

    public List<BlogKeywordsEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return blogKeywordsRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public long countForAjax() {
        return blogKeywordsRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public void add(BlogKeywordsEntity blogKeyword) {
        blogKeywordsRepository.add(sessionFactory.getCurrentSession(), blogKeyword);
    }

    public void delete(BlogKeywordsEntity blogKeyword) {
        blogKeywordsRepository.delete(sessionFactory.getCurrentSession(), blogKeyword);
    }
}
