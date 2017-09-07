package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsTextI18NCategoriesEntity;
import eu.com.cwsfe.cms.db.news.CmsTextI18nCategoryRepository;
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
public class CmsTextI18nCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsTextI18nCategoryService.class);

    private final CmsTextI18nCategoryRepository cmsTextI18nCategoryRepository;
    private final SessionFactory sessionFactory;

    public CmsTextI18nCategoryService(CmsTextI18nCategoryRepository cmsTextI18nCategoryRepository, SessionFactory sessionFactory) {
        this.cmsTextI18nCategoryRepository = cmsTextI18nCategoryRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CmsTextI18NCategoriesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsTextI18nCategoryRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return cmsTextI18nCategoryRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsTextI18NCategoriesEntity> listForDropList(String term, Integer limit) {
        return cmsTextI18nCategoryRepository.listForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public void add(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        LOG.info("Adding news category: {}", cmsTextI18nCategory);
        cmsTextI18nCategoryRepository.add(sessionFactory.getCurrentSession(), cmsTextI18nCategory);
    }

    @Transactional
    public void delete(CmsTextI18NCategoriesEntity cmsTextI18nCategory) {
        LOG.info("Deleting news category: {}", cmsTextI18nCategory);
        cmsTextI18nCategoryRepository.delete(sessionFactory.getCurrentSession(), cmsTextI18nCategory);
    }

    @Transactional
    public Optional<CmsTextI18NCategoriesEntity> get(long i18nCategory) {
        return cmsTextI18nCategoryRepository.get(sessionFactory.getCurrentSession(), i18nCategory);
    }
}
