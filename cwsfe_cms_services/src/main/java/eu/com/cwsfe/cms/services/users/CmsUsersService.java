package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsUsersService {

    private final SessionFactory sessionFactory;
    private final CmsUsersRepository cmsUsersRepository;

    public CmsUsersService(SessionFactory sessionFactory, CmsUsersRepository cmsUsersRepository) {
        this.sessionFactory = sessionFactory;
        this.cmsUsersRepository = cmsUsersRepository;
    }

    public List<CmsUsersEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsUsersRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsUsersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsUsersEntity> listUsersForDropList(String term, Integer limit) {
        return cmsUsersRepository.listUsersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public CmsUsersEntity getByUsername(String username) {
        return cmsUsersRepository.getByUsername(sessionFactory.getCurrentSession(), username);
    }

    public Long add(CmsUsersEntity cmsUser) {
        return cmsUsersRepository.add(sessionFactory.getCurrentSession(), cmsUser);
    }

    public void delete(CmsUsersEntity cmsUser) {
        cmsUsersRepository.delete(sessionFactory.getCurrentSession(), cmsUser);
    }

    public void lock(CmsUsersEntity cmsUser) {
        cmsUsersRepository.lock(sessionFactory.getCurrentSession(), cmsUser);
    }

    public void unlock(CmsUsersEntity cmsUser) {
        cmsUsersRepository.unlock(sessionFactory.getCurrentSession(), cmsUser);
    }

    public CmsUsersEntity get(Long id) {
        return cmsUsersRepository.get(sessionFactory.getCurrentSession(), id);
    }

    public void updatePostBasicInfo(CmsUsersEntity cmsUser) {
        cmsUsersRepository.updatePostBasicInfo(sessionFactory.getCurrentSession(), cmsUser);
    }

    public boolean isActiveUsernameInDatabase(String username) {
        return cmsUsersRepository.isActiveUsernameInDatabase(sessionFactory.getCurrentSession(), username);
    }
}
