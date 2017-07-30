package eu.com.cwsfe.cms.services.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.author.CmsAuthorsRepository;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public CmsAuthorDTO get(long authorId) {
        CmsAuthorsEntity cmsAuthorsEntity = cmsAuthorsRepository.get(sessionFactory.getCurrentSession(), authorId);
        return modelMapper.map(cmsAuthorsEntity, CmsAuthorDTO.class);
    }

    public List<CmsAuthorsEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsAuthorsRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsAuthorsRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsAuthorsEntity> listAuthorsForDropList(String term, Integer limit) {
        return cmsAuthorsRepository.listAuthorsForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public void add(CmsAuthorsEntity cmsAuthor) {
        cmsAuthorsRepository.add(sessionFactory.getCurrentSession(), cmsAuthor);
    }

    public void delete(CmsAuthorsEntity cmsAuthor) {
        cmsAuthorsRepository.delete(sessionFactory.getCurrentSession(), cmsAuthor);
    }
}
