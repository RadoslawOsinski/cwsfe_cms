package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsNewsImagesService {

    private final CmsNewsImagesRepository cmsNewsImagesRepository;
    private final SessionFactory sessionFactory;

    public CmsNewsImagesService(CmsNewsImagesRepository cmsNewsImagesRepository, SessionFactory sessionFactory) {
        this.cmsNewsImagesRepository = cmsNewsImagesRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CmsNewsImagesEntity> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long newsId) {
        return cmsNewsImagesRepository.searchByAjaxWithoutContent(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength, newsId);
    }

    @Transactional
    public Integer searchByAjaxCountWithoutContent(Long newsId) {
        return cmsNewsImagesRepository.searchByAjaxCountWithoutContent(sessionFactory.getCurrentSession(), newsId);
    }

    @Transactional
    public Integer getTotalNumberNotDeleted() {
        return cmsNewsImagesRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    @Transactional
    public long add(CmsNewsImagesEntity cmsNewsImage) {
        return cmsNewsImagesRepository.add(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void updateUrl(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImagesRepository.updateUrl(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void delete(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImagesRepository.delete(sessionFactory.getCurrentSession(), cmsNewsImage);
    }
}
