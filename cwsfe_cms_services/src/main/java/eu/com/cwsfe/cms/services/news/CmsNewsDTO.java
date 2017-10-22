package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import eu.com.cwsfe.cms.db.news.CmsNewsEntity;
import eu.com.cwsfe.cms.db.news.CmsNewsImage;

import java.time.ZonedDateTime;
import java.util.List;

public class CmsNewsDTO {

    private Long id;
    private Long authorId;
    private Long newsTypeId;
    private Long newsFolderId;
    private ZonedDateTime creationDate;
    private String newsCode;
    private List<CmsNewsImage> cmsNewsImages;
    private CmsNewsImage thumbnailImage;
    private PublishedHiddenStatus status;

    CmsNewsDTO(CmsNewsEntity cmsNewsEntity) {
        this.setId(cmsNewsEntity.getId());
        this.setAuthorId(cmsNewsEntity.getAuthorId());
        this.setNewsTypeId(cmsNewsEntity.getNewsTypeId());
        this.setNewsFolderId(cmsNewsEntity.getNewsFolderId());
        this.setCreationDate(cmsNewsEntity.getCreationDate());
        this.setNewsCode(cmsNewsEntity.getNewsCode());
        this.setStatus(cmsNewsEntity.getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getNewsTypeId() {
        return newsTypeId;
    }

    public void setNewsTypeId(Long newsTypeId) {
        this.newsTypeId = newsTypeId;
    }

    public Long getNewsFolderId() {
        return newsFolderId;
    }

    public void setNewsFolderId(Long newsFolderId) {
        this.newsFolderId = newsFolderId;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getNewsCode() {
        return newsCode;
    }

    public void setNewsCode(String newsCode) {
        this.newsCode = newsCode;
    }

    public List<CmsNewsImage> getCmsNewsImages() {
        return cmsNewsImages;
    }

    public void setCmsNewsImages(List<CmsNewsImage> cmsNewsImages) {
        this.cmsNewsImages = cmsNewsImages;
    }

    public CmsNewsImage getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(CmsNewsImage thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public PublishedHiddenStatus getStatus() {
        return status;
    }

    public void setStatus(PublishedHiddenStatus status) {
        this.status = status;
    }
}
