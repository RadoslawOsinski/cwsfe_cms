package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUserRolesEntity;
import eu.com.cwsfe.cms.db.users.CmsUserRolesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public List<CmsRolesEntity> listUserRoles(long userId) {
        return cmsUserRolesRepository.listUserRoles(sessionFactory.getCurrentSession(), userId);
    }

    @Transactional
    public void updateUserRoles(long userId, String[] userRoles) {
        cmsUserRolesRepository.deleteForUser(sessionFactory.getCurrentSession(), userId);
        if (userRoles != null) {
            for (String userRole : userRoles) {
                CmsUserRolesEntity cmsUserRole = new CmsUserRolesEntity();
                cmsUserRole.setCmsUserId(userId);
                cmsUserRole.setRoleId(Long.parseLong(userRole));
                cmsUserRolesRepository.add(sessionFactory.getCurrentSession(), cmsUserRole);
            }
        }
    }
}
