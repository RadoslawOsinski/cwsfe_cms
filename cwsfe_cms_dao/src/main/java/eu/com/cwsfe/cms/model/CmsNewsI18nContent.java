package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.domains.CmsNewsI18nContentStatus;

import java.io.Serializable;

public class CmsNewsI18nContent implements Serializable {

    private static final long serialVersionUID = -204179112645839853L;

    private Long id;
    private Long newsId;
    private Long languageId;
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private CmsNewsI18nContentStatus status;

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

    public CmsNewsI18nContentStatus getStatus() {
        return status;
    }

    public void setStatus(CmsNewsI18nContentStatus status) {
        this.status = status;
    }
}
