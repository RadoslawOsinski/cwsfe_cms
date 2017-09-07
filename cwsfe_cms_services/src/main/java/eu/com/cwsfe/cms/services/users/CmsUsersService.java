package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsUsersEntity;
import eu.com.cwsfe.cms.db.users.CmsUsersRepository;
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
public class CmsUsersService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsUsersService.class);

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
    public Long countForAjax() {
        return cmsUsersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsUsersEntity> listUsersForDropList(String term, Integer limit) {
        return cmsUsersRepository.listUsersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public Optional<CmsUsersEntity> getByUsername(String username) {
        return cmsUsersRepository.getByUsername(sessionFactory.getCurrentSession(), username);
    }

    @Transactional
    public Long add(CmsUsersEntity cmsUser) {
        LOG.info("Adding user: {}", cmsUser);
        return cmsUsersRepository.add(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void delete(CmsUsersEntity cmsUser) {
        LOG.info("Deleting user: {}", cmsUser);
        cmsUsersRepository.delete(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void lock(CmsUsersEntity cmsUser) {
        LOG.info("Locking user: {}", cmsUser);
        cmsUsersRepository.lock(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public void unlock(CmsUsersEntity cmsUser) {
        LOG.info("Unlocking user: {}", cmsUser);
        cmsUsersRepository.unlock(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public Optional<CmsUsersEntity> get(Long id) {
        return cmsUsersRepository.get(sessionFactory.getCurrentSession(), id);
    }

    @Transactional
    public void updatePostBasicInfo(CmsUsersEntity cmsUser) {
        LOG.info("Updating user: {}", cmsUser);
        cmsUsersRepository.updatePostBasicInfo(sessionFactory.getCurrentSession(), cmsUser);
    }

    @Transactional
    public boolean isActiveUsernameInDatabase(String username) {
        return cmsUsersRepository.isActiveUsernameInDatabase(sessionFactory.getCurrentSession(), username);
    }
}
