package eu.com.cwsfe.cms.services.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.author.CmsAuthorsRepository;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsAuthorsService {

    private final ModelMapper modelMapper;
    private final CmsAuthorsRepository cmsAuthorsRepository;
    private final SessionFactory sessionFactory;

    public CmsAuthorsService(ModelMapper modelMapper, CmsAuthorsRepository cmsAuthorsRepository, SessionFactory sessionFactory) {
        this.modelMapper = modelMapper;
        this.cmsAuthorsRepository = cmsAuthorsRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public CmsAuthorDTO get(long authorId) {
        CmsAuthorsEntity cmsAuthorsEntity = cmsAuthorsRepository.get(sessionFactory.getCurrentSession(), authorId);
        return modelMapper.map(cmsAuthorsEntity, CmsAuthorDTO.class);
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
        return cmsAuthorsRepository.add(sessionFactory.getCurrentSession(), cmsAuthor);
    }

    @Transactional
    public void delete(CmsAuthorsEntity cmsAuthor) {
        cmsAuthorsRepository.delete(sessionFactory.getCurrentSession(), cmsAuthor);
    }
}
