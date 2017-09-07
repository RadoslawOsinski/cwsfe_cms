package eu.com.cwsfe.cms.services.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.author.CmsAuthorsRepository;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
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
public class CmsAuthorsService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsAuthorsService.class);

    private final ModelMapper modelMapper;
    private final CmsAuthorsRepository cmsAuthorsRepository;
    private final SessionFactory sessionFactory;

    public CmsAuthorsService(ModelMapper modelMapper, CmsAuthorsRepository cmsAuthorsRepository, SessionFactory sessionFactory) {
        this.modelMapper = modelMapper;
        this.cmsAuthorsRepository = cmsAuthorsRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public Optional<CmsAuthorDTO> get(long authorId) {
        Optional<CmsAuthorsEntity> cmsAuthorsEntity = cmsAuthorsRepository.get(sessionFactory.getCurrentSession(), authorId);
        if (cmsAuthorsEntity.isPresent()) {
            return Optional.of(modelMapper.map(cmsAuthorsEntity, CmsAuthorDTO.class));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public List<CmsAuthorsEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsAuthorsRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return cmsAuthorsRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsAuthorsEntity> listAuthorsForDropList(String term, Integer limit) {
        return cmsAuthorsRepository.listAuthorsForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public Long add(CmsAuthorsEntity cmsAuthor) {
        LOG.info("Adding author: {}", cmsAuthor);
        return cmsAuthorsRepository.add(sessionFactory.getCurrentSession(), cmsAuthor);
    }

    @Transactional
    public void delete(CmsAuthorsEntity cmsAuthor) {
        LOG.info("Deleting author: {}", cmsAuthor);
        cmsAuthorsRepository.delete(sessionFactory.getCurrentSession(), cmsAuthor);
    }
}
