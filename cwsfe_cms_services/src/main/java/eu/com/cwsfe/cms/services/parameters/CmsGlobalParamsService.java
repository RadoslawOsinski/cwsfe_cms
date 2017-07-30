package eu.com.cwsfe.cms.services.parameters;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsGlobalParamsService {

    private final SessionFactory sessionFactory;
    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    public CmsGlobalParamsService(SessionFactory sessionFactory, CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.sessionFactory = sessionFactory;
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    public CmsGlobalParamsEntity getByCode(String code) {
        return cmsGlobalParamsRepository.getByCode(sessionFactory.getCurrentSession(), code);
    }

    public void update(CmsGlobalParamsEntity param) {
        cmsGlobalParamsRepository.update(sessionFactory.getCurrentSession(), param);
    }
}
