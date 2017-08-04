package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsTextI18NEntity;
import eu.com.cwsfe.cms.db.news.CmsTextI18nRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsTextI18nService {

    private final CmsTextI18nRepository cmsTextI18nRepository;
    private final SessionFactory sessionFactory;

    public CmsTextI18nService(CmsTextI18nRepository cmsTextI18nRepository, SessionFactory sessionFactory) {
        this.cmsTextI18nRepository = cmsTextI18nRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CmsTextI18NEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsTextI18nRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return cmsTextI18nRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public void add(CmsTextI18NEntity cmsTextI18n) {
        cmsTextI18nRepository.add(sessionFactory.getCurrentSession(), cmsTextI18n);
    }

    @Transactional
    public void delete(CmsTextI18NEntity cmsTextI18n) {
        cmsTextI18nRepository.delete(sessionFactory.getCurrentSession(), cmsTextI18n);
    }

    @Transactional
    public Optional<CmsTextI18NEntity> getExisting(CmsTextI18NEntity cmsTextI18n) {
        return cmsTextI18nRepository.get(sessionFactory.getCurrentSession(),
            cmsTextI18n.getLangId(), cmsTextI18n.getI18nCategory(), cmsTextI18n.getI18NKey()
        );
    }

    @Transactional
    public Optional<String> findTranslation(String languageCode, String category, String key) {
        return cmsTextI18nRepository.findTranslation(sessionFactory.getCurrentSession(), languageCode, category, key);
    }
}
