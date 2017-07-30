package eu.com.cwsfe.cms.services.users;

import eu.com.cwsfe.cms.db.users.CmsUserAllowedNetAddressEntity;
import eu.com.cwsfe.cms.db.users.CmsUserAllowedNetAddressRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsUserAllowedNetAddressService {

    private final CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressRepository;
    private final SessionFactory sessionFactory;

    public CmsUserAllowedNetAddressService(CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressRepository, SessionFactory sessionFactory) {
        this.cmsUserAllowedNetAddressRepository = cmsUserAllowedNetAddressRepository;
        this.sessionFactory = sessionFactory;
    }

    public List<CmsUserAllowedNetAddressEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsUserAllowedNetAddressRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsUserAllowedNetAddressRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public void add(CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        cmsUserAllowedNetAddressRepository.add(sessionFactory.getCurrentSession(), cmsUserAllowedNetAddress);
    }

    public void delete(CmsUserAllowedNetAddressEntity cmsUserAllowedNetAddress) {
        cmsUserAllowedNetAddressRepository.delete(sessionFactory.getCurrentSession(), cmsUserAllowedNetAddress);
    }

    public List<CmsUserAllowedNetAddressEntity> listForUser(long userId) {
        return cmsUserAllowedNetAddressRepository.listForUser(sessionFactory.getCurrentSession(), userId);
    }

    public int countAddressesForUser(long userId) {
        return cmsUserAllowedNetAddressRepository.countAddressesForUser(sessionFactory.getCurrentSession(), userId);
    }
}
