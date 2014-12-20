package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class CmsNewsI18nContent implements Serializable {

    private static final long serialVersionUID = -204179112645839853L;

    private Long id;
    private Long newsId;
    private Long languageId;
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private String status;    //'D'eleted, 'H'idden, 'P'ublished

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
