package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsTypesEntity;
import eu.com.cwsfe.cms.db.news.NewsTypesRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class NewsTypesService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsTypesService.class);

    private final NewsTypesRepository newsTypesRepository;
    private final SessionFactory sessionFactory;

    public NewsTypesService(NewsTypesRepository newsTypesRepository, SessionFactory sessionFactory) {
        this.newsTypesRepository = newsTypesRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CmsNewsTypesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return newsTypesRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return newsTypesRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsNewsTypesEntity> listNewsTypesForDropList(String term, Integer limit) {
        return newsTypesRepository.listNewsTypesForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public void add(CmsNewsTypesEntity newsType) {
        LOG.info("Adding news type: {}", newsType);
        newsTypesRepository.add(sessionFactory.getCurrentSession(), newsType);
    }

    @Transactional
    public void delete(CmsNewsTypesEntity cmsNewsType) {
        LOG.info("Deleting news type: {}", cmsNewsType);
        newsTypesRepository.delete(sessionFactory.getCurrentSession(), cmsNewsType);
    }

    @Transactional
    public Optional<CmsNewsTypesEntity> get(long newsTypeId) {
        return newsTypesRepository.get(sessionFactory.getCurrentSession(), newsTypeId);
    }

    @Transactional
    public Optional<CmsNewsTypesEntity> getByType(String type) {
        return newsTypesRepository.getByType(sessionFactory.getCurrentSession(), type);
    }
}
