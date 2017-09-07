package eu.com.cwsfe.cms.services.parameters;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsGlobalParamsService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsGlobalParamsService.class);

    private final SessionFactory sessionFactory;
    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    public CmsGlobalParamsService(SessionFactory sessionFactory, CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.sessionFactory = sessionFactory;
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    @Transactional
    public Optional<CmsGlobalParamsEntity> getByCode(String code) {
        return cmsGlobalParamsRepository.getByCode(sessionFactory.getCurrentSession(), code);
    }

    @Transactional
    public void update(CmsGlobalParamsEntity param) {
        LOG.info("Updating global param: {}", param);
        cmsGlobalParamsRepository.update(sessionFactory.getCurrentSession(), param);
    }
}
