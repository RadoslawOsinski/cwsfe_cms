package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsTextI18NEntity;
import eu.com.cwsfe.cms.db.news.CmsTextI18nRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CmsTextI18NEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsTextI18nRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsTextI18nRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public void add(CmsTextI18NEntity cmsTextI18n) {
        cmsTextI18nRepository.add(sessionFactory.getCurrentSession(), cmsTextI18n);
    }

    public void delete(CmsTextI18NEntity cmsTextI18n) {
        cmsTextI18nRepository.delete(sessionFactory.getCurrentSession(), cmsTextI18n);
    }
}
