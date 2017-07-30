package eu.com.cwsfe.cms.services.i18n;

import eu.com.cwsfe.cms.db.i18n.CmsLanguagesEntity;
import eu.com.cwsfe.cms.db.i18n.CmsLanguagesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsLanguagesService {

    private final CmsLanguagesRepository cmsLanguagesRepository;
    private final SessionFactory sessionFactory;

    public CmsLanguagesService(CmsLanguagesRepository cmsLanguagesRepository, SessionFactory sessionFactory) {
        this.cmsLanguagesRepository = cmsLanguagesRepository;
        this.sessionFactory = sessionFactory;
    }

    public CmsLanguagesEntity getById(long langId) {
        return cmsLanguagesRepository.getById(sessionFactory.getCurrentSession(), langId);
    }

    public List<CmsLanguagesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsLanguagesRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsLanguagesRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsLanguagesEntity> listForDropList(String term, Integer limit) {
        return cmsLanguagesRepository.listForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public void add(CmsLanguagesEntity cmsLanguage) {
        cmsLanguagesRepository.add(sessionFactory.getCurrentSession(), cmsLanguage);
    }

    public void delete(CmsLanguagesEntity cmsLanguage) {
        cmsLanguagesRepository.delete(sessionFactory.getCurrentSession(), cmsLanguage);
    }

    public List<CmsLanguagesEntity> listAll() {
        return cmsLanguagesRepository.listAll(sessionFactory.getCurrentSession());
    }
}
