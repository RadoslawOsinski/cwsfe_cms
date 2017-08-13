package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsI18NContentsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsI18nContentsRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsNewsI18nContentsService {

    private final CmsNewsI18nContentsRepository cmsNewsI18nContentsRepository;
    private final SessionFactory sessionFactory;

    public CmsNewsI18nContentsService(CmsNewsI18nContentsRepository cmsNewsI18nContentsRepository, SessionFactory sessionFactory) {
        this.cmsNewsI18nContentsRepository = cmsNewsI18nContentsRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Optional<CmsNewsI18NContentsEntity> getByLanguageForNews(Long newsId, Long langId) {
        return cmsNewsI18nContentsRepository.getByLanguageForNews(sessionFactory.getCurrentSession(), newsId, langId);
    }

    @Transactional
    public void add(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContentsRepository.add(sessionFactory.getCurrentSession(), cmsNewsI18nContent);
    }

    @Transactional
    public void updateContentWithStatus(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        cmsNewsI18nContentsRepository.updateContentWithStatus(sessionFactory.getCurrentSession(), cmsNewsI18nContent);
    }
}