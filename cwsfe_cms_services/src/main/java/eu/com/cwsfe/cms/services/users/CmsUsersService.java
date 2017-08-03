package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<CmsUsersEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsUsersRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public int countForAjax() {
        return cmsUsersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsUsersEntity> listUsersForDropList(String term, Integer limit) {
        return cmsUsersRepository.listUsersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public CmsUsersEntity getByUsername(String username) {
        return cmsUsersRepository.getByUsername(sessionFactory.getCurrentSession(), username);
    }

    @Transactional
    public Long add(CmsUsersEntity cmsUser) {
        return cmsUsersRepository.add(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void delete(CmsUsersEntity cmsUser) {
        cmsUsersRepository.delete(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void lock(CmsUsersEntity cmsUser) {
        cmsUsersRepository.lock(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void unlock(CmsUsersEntity cmsUser) {
        cmsUsersRepository.unlock(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public CmsUsersEntity get(Long id) {
        return cmsUsersRepository.get(sessionFactory.getCurrentSession(), id);
    }

    @Transactional
    public void updatePostBasicInfo(CmsUsersEntity cmsUser) {
        cmsUsersRepository.updatePostBasicInfo(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public boolean isActiveUsernameInDatabase(String username) {
        return cmsUsersRepository.isActiveUsernameInDatabase(sessionFactory.getCurrentSession(), username);
    }
}
