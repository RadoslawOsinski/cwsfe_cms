package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsI18NContentsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsI18nContentsRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsNewsI18nContentsService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsNewsI18nContentsService.class);

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
        LOG.info("Adding news i18n content: {}", cmsNewsI18nContent);
        cmsNewsI18nContentsRepository.add(sessionFactory.getCurrentSession(), cmsNewsI18nContent);
    }

    @Transactional
    public void updateContentWithStatus(CmsNewsI18NContentsEntity cmsNewsI18nContent) {
        LOG.info("Updating news i18n content: {}", cmsNewsI18nContent);
        cmsNewsI18nContentsRepository.updateContentWithStatus(sessionFactory.getCurrentSession(), cmsNewsI18nContent);
    }

    @Transactional
    public List<CmsNewsI18nPairDTO> list(String folderName, String languageCode, String newsType, int newsPerPage, int offset) {
        List<CmsNewsI18NContentsEntity> list = cmsNewsI18nContentsRepository.list(sessionFactory.getCurrentSession(), folderName, languageCode, newsType, newsPerPage, offset);
        List<CmsNewsI18nPairDTO> cmsNewsI18nPairs = new ArrayList<>();
        for (CmsNewsI18NContentsEntity cmsNewsI18NContent : list) {
            cmsNewsI18nPairs.add(new CmsNewsI18nPairDTO(new CmsNewsDTO(cmsNewsI18NContent.getCmsNewsEntity()), new CmsNewsI18nContentDTO(cmsNewsI18NContent)));
        }
        return cmsNewsI18nPairs;
    }
}
