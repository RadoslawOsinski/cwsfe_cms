package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsRolesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsRolesService {

    private final SessionFactory sessionFactory;
    private final CmsRolesRepository cmsRolesRepository;

    public CmsRolesService(SessionFactory sessionFactory, CmsRolesRepository cmsRolesRepository) {
        this.sessionFactory = sessionFactory;
        this.cmsRolesRepository = cmsRolesRepository;
    }

    @Transactional
    public List<CmsRolesEntity> list() {
        return cmsRolesRepository.list(sessionFactory.getCurrentSession());
    }

    @Transactional
    public Optional<CmsRolesEntity> getByCode(String code) {
        return cmsRolesRepository.getByCode(sessionFactory.getCurrentSession(), code);
    }

    @Transactional
    public List<CmsRolesEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsRolesRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return cmsRolesRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsRolesEntity> listRolesForDropList(String term, Integer limit) {
        return cmsRolesRepository.listRolesForDropList(sessionFactory.getCurrentSession(), term, limit);
    }
}
