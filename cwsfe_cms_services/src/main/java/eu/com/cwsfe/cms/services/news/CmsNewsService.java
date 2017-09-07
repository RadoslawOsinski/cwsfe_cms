package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsRepository;
import eu.com.cwsfe.cms.db.news.SearchedNewsDTO;
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
public class CmsNewsService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsNewsService.class);

    private final CmsNewsRepository cmsNewsRepository;
    private final SessionFactory sessionFactory;

    public CmsNewsService(CmsNewsRepository cmsNewsRepository, SessionFactory sessionFactory) {
        this.cmsNewsRepository = cmsNewsRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void add(CmsNewsEntity cmsNews) {
        LOG.info("Adding news: {}", cmsNews);
        cmsNewsRepository.add(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public void updatePostBasicInfo(CmsNewsEntity cmsNews) {
        LOG.info("Updating news: {}", cmsNews);
        cmsNewsRepository.updatePostBasicInfo(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public void delete(CmsNewsEntity cmsNews) {
        LOG.info("Deleting news: {}", cmsNews);
        cmsNewsRepository.delete(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public Optional<CmsNewsEntity> get(Long id) {
        return cmsNewsRepository.get(sessionFactory.getCurrentSession(), id);
    }

    @Transactional
    public List<SearchedNewsDTO> searchByAjax(int iDisplayStart, int iDisplayLength, Integer searchAuthorId, String searchNewsCode) {
        return cmsNewsRepository.searchByAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength, searchAuthorId, searchNewsCode);
    }

    @Transactional
    public Long searchByAjaxCount(Integer searchAuthorId, String searchNewsCode) {
        return cmsNewsRepository.searchByAjaxCount(sessionFactory.getCurrentSession(), searchAuthorId, searchNewsCode);
    }

    @Transactional
    public Long getTotalNumberNotDeleted() {
        return cmsNewsRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }
}
