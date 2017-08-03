package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsUserRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUserRolesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsUserRolesService {

    private final SessionFactory sessionFactory;
    private final CmsUserRolesRepository cmsUserRolesRepository;

    public CmsUserRolesService(SessionFactory sessionFactory, CmsUserRolesRepository cmsUserRolesRepository) {
        this.sessionFactory = sessionFactory;
        this.cmsUserRolesRepository = cmsUserRolesRepository;
    }

    @Transactional
    public void deleteForUser(long id) {
        cmsUserRolesRepository.deleteForUser(sessionFactory.getCurrentSession(), id);
    }

    @Transactional
    public void add(CmsUserRolesEntity cmsUserRole) {
        cmsUserRolesRepository.add(sessionFactory.getCurrentSession(), cmsUserRole);
    }
}
