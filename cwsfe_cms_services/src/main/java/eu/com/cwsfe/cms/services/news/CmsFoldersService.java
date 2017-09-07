package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsFoldersEntity;
import eu.com.cwsfe.cms.db.news.CmsFoldersRepository;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsFoldersService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsFoldersService.class);

    private final CmsFoldersRepository cmsFoldersRepository;
    private final SessionFactory sessionFactory;
    private final ModelMapper modelMapper;

    public CmsFoldersService(SessionFactory sessionFactory, CmsFoldersRepository cmsFoldersRepository, ModelMapper modelMapper) {
        this.cmsFoldersRepository = cmsFoldersRepository;
        this.sessionFactory = sessionFactory;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<CmsFoldersEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsFoldersRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public Long countForAjax() {
        return cmsFoldersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsFoldersEntity> listFoldersForDropList(String term, Integer limit) {
        return cmsFoldersRepository.listFoldersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public void add(CmsFoldersEntity cmsFolder) {
        LOG.info("Adding folder: {}", cmsFolder);
        cmsFoldersRepository.add(sessionFactory.getCurrentSession(), cmsFolder);
    }

    @Transactional
    public void delete(CmsFoldersEntity cmsFolder) {
        LOG.info("Deleting folder: {}", cmsFolder);
        cmsFoldersRepository.delete(sessionFactory.getCurrentSession(), cmsFolder);
    }

    @Transactional
    public Optional<CmsFoldersEntity> getByFolderName(String folderName) {
        return cmsFoldersRepository.getByFolderName(sessionFactory.getCurrentSession(), folderName);
    }

    @Transactional
    public Optional<CmsFoldersEntity> get(long folderId) {
        return cmsFoldersRepository.get(sessionFactory.getCurrentSession(), folderId);
    }

    @Transactional
    public List<CmsFolderDTO> list() {
        List<CmsFoldersEntity> list = cmsFoldersRepository.list(sessionFactory.getCurrentSession());
        return list.stream().map(cmsFoldersEntity -> modelMapper.map(cmsFoldersEntity, CmsFolderDTO.class)).collect(Collectors.toList());
    }
}
