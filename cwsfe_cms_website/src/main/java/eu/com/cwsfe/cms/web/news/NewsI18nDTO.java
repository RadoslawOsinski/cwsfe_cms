package eu.com.cwsfe.cms.web.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radosław Osiński
 */
public class NewsI18nDTO {
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private String status;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
