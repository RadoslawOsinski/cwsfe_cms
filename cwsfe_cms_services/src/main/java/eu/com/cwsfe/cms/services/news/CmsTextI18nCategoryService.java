package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsTextI18NCategoriesEntity;
import eu.com.cwsfe.cms.db.news.CmsTextI18nCategoryRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsTextI18nCategoryService {

    private final CmsTextI18nCategoryRepository cmsTextI18nCategoryRepository;
    private final SessionFactory sessionFactory;

    public CmsTextI18nCategoryService(CmsTextI18nCategoryRepository cmsTextI18nCategoryRepository, SessionFactory sessionFactory) {
        this.cmsTextI18nCategoryRepository = cmsTextI18nCategoryRepository;
        this.sessionFactory = sessionFactory;
    }

    public List<CmsTextI18NCategoriesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsTextI18nCategoryRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsTextI18nCategoryRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsTextI18NCategoriesEntity> listForDropList(String term, Integer limit) {
        return cmsTextI18nCategoryRepository.listForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public void add(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategoryRepository.add(sessionFactory.getCurrentSession(), cmsTextI18nCategory);
    }

    public void delete(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        cmsTextI18nCategoryRepository.delete(sessionFactory.getCurrentSession(), cmsTextI18nCategory);
    }
}
