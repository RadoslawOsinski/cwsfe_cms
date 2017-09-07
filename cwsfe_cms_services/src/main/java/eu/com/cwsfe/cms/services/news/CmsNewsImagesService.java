package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesRepository;
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
public class CmsNewsImagesService {

    private static final Logger LOG = LoggerFactory.getLogger(CmsNewsImagesService.class);

    private final CmsNewsImagesRepository cmsNewsImagesRepository;
    private final SessionFactory sessionFactory;
    private final ModelMapper modelMapper;

    public CmsNewsImagesService(CmsNewsImagesRepository cmsNewsImagesRepository, SessionFactory sessionFactory, ModelMapper modelMapper) {
        this.cmsNewsImagesRepository = cmsNewsImagesRepository;
        this.sessionFactory = sessionFactory;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<CmsNewsImagesEntity> searchByAjaxWithoutContent(int iDisplayStart, int iDisplayLength, Long newsId) {
        return cmsNewsImagesRepository.searchByAjaxWithoutContent(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength, newsId);
    }

    @Transactional
    public Long searchByAjaxCountWithoutContent(Long newsId) {
        return cmsNewsImagesRepository.searchByAjaxCountWithoutContent(sessionFactory.getCurrentSession(), newsId);
    }

    @Transactional
    public Long getTotalNumberNotDeleted() {
        return cmsNewsImagesRepository.getTotalNumberNotDeleted(sessionFactory.getCurrentSession());
    }

    @Transactional
    public long add(CmsNewsImagesEntity cmsNewsImage) {
        LOG.info("Adding news image: {}", cmsNewsImage);
        return cmsNewsImagesRepository.add(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void updateUrl(CmsNewsImagesEntity cmsNewsImage) {
        LOG.info("Updating news image url: {}", cmsNewsImage);
        cmsNewsImagesRepository.updateUrl(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void delete(CmsNewsImagesEntity cmsNewsImage) {
        LOG.info("Deleting news image: {}", cmsNewsImage);
        cmsNewsImagesRepository.delete(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    public Optional<CmsNewsImageDTO> getThumbnailForNews(long newsId) {
        Optional<CmsNewsImagesEntity> thumbnailForNews = cmsNewsImagesRepository.getThumbnailForNews(sessionFactory.getCurrentSession(), newsId);
        return thumbnailForNews.map(cmsNewsImagesEntity -> modelMapper.map(cmsNewsImagesEntity, CmsNewsImageDTO.class));
    }

    public List<CmsNewsImageDTO> listImagesForNewsWithoutThumbnails(long newsId) {
        List<CmsNewsImagesEntity> cmsNewsImagesEntities = cmsNewsImagesRepository.listImagesForNewsWithoutThumbnails(sessionFactory.getCurrentSession(), newsId);
        return cmsNewsImagesEntities.stream().map(cmsNewsImagesEntity -> modelMapper.map(cmsNewsImagesEntity, CmsNewsImageDTO.class)).collect(Collectors.toList());
    }
}
