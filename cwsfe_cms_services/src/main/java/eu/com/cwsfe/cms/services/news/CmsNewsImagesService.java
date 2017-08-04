package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsNewsImagesEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImagesRepository;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
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
        return cmsNewsImagesRepository.add(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void updateUrl(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImagesRepository.updateUrl(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    @Transactional
    public void delete(CmsNewsImagesEntity cmsNewsImage) {
        cmsNewsImagesRepository.delete(sessionFactory.getCurrentSession(), cmsNewsImage);
    }

    public CmsNewsImageDTO getThumbnailForNews(long newsId) {
        Optional<CmsNewsImagesEntity> thumbnailForNews = cmsNewsImagesRepository.getThumbnailForNews(sessionFactory.getCurrentSession(), newsId);
        return modelMapper.map(thumbnailForNews.get(), CmsNewsImageDTO.class);
    }

    public List<CmsNewsImageDTO> listImagesForNewsWithoutThumbnails(long newsId) {
        List<CmsNewsImagesEntity> cmsNewsImagesEntities = cmsNewsImagesRepository.listImagesForNewsWithoutThumbnails(sessionFactory.getCurrentSession(), newsId);
        return cmsNewsImagesEntities.stream().map(cmsNewsImagesEntity -> modelMapper.map(cmsNewsImagesEntity, CmsNewsImageDTO.class)).collect(Collectors.toList());
    }
}
