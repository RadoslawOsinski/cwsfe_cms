package eu.com.cwsfe.cms.web.news;

/**
 * Created by Radosław Osiński
 */
public class CmsNewsI18NContentDTO {
    private Long newsId;
    private Long languageId;
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private String status;

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsShortcut(String newsShortcut) {
        this.newsShortcut = newsShortcut;
    }

    public String getNewsShortcut() {
        return newsShortcut;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
