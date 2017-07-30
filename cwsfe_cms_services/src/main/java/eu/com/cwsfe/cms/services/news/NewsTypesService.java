package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsTypesEntity;
import eu.com.cwsfe.cms.db.news.NewsTypesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class NewsTypesService {

    private final NewsTypesRepository newsTypesRepository;
    private final SessionFactory sessionFactory;

    public NewsTypesService(NewsTypesRepository newsTypesRepository, SessionFactory sessionFactory) {
        this.newsTypesRepository = newsTypesRepository;
        this.sessionFactory = sessionFactory;
    }


    public List<CmsNewsTypesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return newsTypesRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return newsTypesRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsNewsTypesEntity> listNewsTypesForDropList(String term, Integer limit) {
        return newsTypesRepository.listNewsTypesForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public void add(CmsNewsTypesEntity newsType) {
        newsTypesRepository.add(sessionFactory.getCurrentSession(), newsType);
    }

    public void delete(CmsNewsTypesEntity cmsNewsType) {
        newsTypesRepository.delete(sessionFactory.getCurrentSession(), cmsNewsType);
    }

    public CmsNewsTypesEntity get(long newsTypeId) {
        return newsTypesRepository.get(sessionFactory.getCurrentSession(), newsTypeId);
    }
}
