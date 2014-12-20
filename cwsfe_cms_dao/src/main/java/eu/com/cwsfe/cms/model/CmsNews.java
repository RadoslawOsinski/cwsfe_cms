package eu.com.cwsfe.cms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CmsNews implements Serializable {

    private static final long serialVersionUID = -2843397946242434202L;

    private Long id;
    private Long authorId;
    private Long newsTypeId;
    private Long newsFolderId;
    private Date creationDate;
    private String newsCode;
    private List<CmsNewsImage> cmsNewsImages;
    private Map<String, CmsNewsI18nContent> cmsNewsI18nContents;
    private CmsNewsImage thumbnailImage;
    private String status;	//'D'eleted, 'H'idden, 'P'ublished

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    public Map<String, CmsNewsI18nContent> getCmsNewsI18nContents() {
        return cmsNewsI18nContents;
    }

    public void setCmsNewsI18nContents(Map<String, CmsNewsI18nContent> cmsNewsI18nContents) {
        this.cmsNewsI18nContents = cmsNewsI18nContents;
    }

    public CmsNewsImage getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(CmsNewsImage thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
