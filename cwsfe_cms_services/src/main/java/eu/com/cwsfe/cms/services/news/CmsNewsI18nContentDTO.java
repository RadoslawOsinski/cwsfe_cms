package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import eu.com.cwsfe.cms.db.news.CmsNewsI18NContentsEntity;

public class CmsNewsI18nContentDTO {

    private Long id;
    private Long newsId;
    private Long languageId;
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private PublishedHiddenStatus status;

    CmsNewsI18nContentDTO(CmsNewsI18NContentsEntity cmsNewsI18NContent) {
        this.id = cmsNewsI18NContent.getId();
        this.newsId = cmsNewsI18NContent.getNewsId();
        this.languageId = cmsNewsI18NContent.getLanguageId();
        this.newsTitle = cmsNewsI18NContent.getNewsTitle();
        this.newsShortcut = cmsNewsI18NContent.getNewsShortcut();
        this.newsDescription = cmsNewsI18NContent.getNewsDescription();
        this.status = cmsNewsI18NContent.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsShortcut() {
        return newsShortcut;
    }

    public void setNewsShortcut(String newsShortcut) {
        this.newsShortcut = newsShortcut;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public PublishedHiddenStatus getStatus() {
        return status;
    }

    public void setStatus(PublishedHiddenStatus status) {
        this.status = status;
    }
}
