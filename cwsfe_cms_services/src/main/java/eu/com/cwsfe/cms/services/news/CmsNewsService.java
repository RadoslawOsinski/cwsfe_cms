package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsNewsService {

    private final CmsNewsRepository cmsNewsRepository;
    private final SessionFactory sessionFactory;

    public CmsNewsService(CmsNewsRepository cmsNewsRepository, SessionFactory sessionFactory) {
        this.cmsNewsRepository = cmsNewsRepository;
        this.sessionFactory = sessionFactory;
    }

    public void add(CmsNewsEntity cmsNews) {
        cmsNewsRepository.add(sessionFactory.getCurrentSession(), cmsNews);
    }

    public void updatePostBasicInfo(CmsNewsEntity cmsNews) {
        cmsNewsRepository.add(sessionFactory.getCurrentSession(), cmsNews);
    }

    public void delete(CmsNewsEntity cmsNews) {
        cmsNewsRepository.delete(sessionFactory.getCurrentSession(), cmsNews);
    }

    public CmsNewsEntity get(Long id) {
        return cmsNewsRepository.get(sessionFactory.getCurrentSession(), id);
    }
}
