package eu.com.cwsfe.cms.services.model;

import java.io.Serializable;

public class Breadcrumb implements Serializable {

    private static final long serialVersionUID = -4442521382899438621L;

    private Long id;
    private String url;
    private String text;

    public Breadcrumb(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
