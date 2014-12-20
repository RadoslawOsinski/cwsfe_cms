package eu.com.cwsfe.cms.model;

import java.io.Serializable;

/**
 * @author radek
 */
public class NewsletterMailGroup implements Serializable {

    private static final long serialVersionUID = -4523302257775395052L;

    private Long id;
    private Long languageId;
    private String name;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
