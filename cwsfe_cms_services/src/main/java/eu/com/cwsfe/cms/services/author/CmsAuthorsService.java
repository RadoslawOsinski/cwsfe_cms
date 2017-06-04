package eu.com.cwsfe.cms.services.author;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.author.CmsAuthorsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsAuthorsService {

    private final ModelMapper modelMapper;
    private final CmsAuthorsRepository cmsAuthorsRepository;

    public CmsAuthorsService(ModelMapper modelMapper, CmsAuthorsRepository cmsAuthorsRepository) {
        this.modelMapper = modelMapper;
        this.cmsAuthorsRepository = cmsAuthorsRepository;
    }

    public CmsAuthorDTO get(long authorId) {
        CmsAuthorsEntity cmsAuthorsEntity = cmsAuthorsRepository.get(authorId);
        return modelMapper.map(cmsAuthorsEntity, CmsAuthorDTO.class);
    }
}
