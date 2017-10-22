package eu.com.cwsfe.cms.web.news;

import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław Osiński
 */
public class NewsI18nDTO {
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private PublishedHiddenStatus status;
    private List<String> errorMessages = new ArrayList<>();

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

    public void setStatus(PublishedHiddenStatus status) {
        this.status = status;
    }

    public PublishedHiddenStatus getStatus() {
        return status;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
