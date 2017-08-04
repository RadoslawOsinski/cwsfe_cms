package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsRepository;
import eu.com.cwsfe.cms.db.news.SearchedNewsDTO;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void add(CmsNewsEntity cmsNews) {
        cmsNewsRepository.add(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public void updatePostBasicInfo(CmsNewsEntity cmsNews) {
        cmsNewsRepository.add(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public void delete(CmsNewsEntity cmsNews) {
        cmsNewsRepository.delete(sessionFactory.getCurrentSession(), cmsNews);
    }

    @Transactional
    public CmsNewsEntity get(Long id) {
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
