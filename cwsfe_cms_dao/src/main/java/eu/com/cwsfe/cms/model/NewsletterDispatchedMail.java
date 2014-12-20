package eu.com.cwsfe.cms.model;

import java.io.Serializable;

/**
 * @author radek
 */
public class NewsletterDispatchedMail implements Serializable {

    private static final long serialVersionUID = -2006522447467707798L;

    private Long id;
    private Long newsletterMailId;
    private String email;
    private String error;
    private String status;  //'S'ended, 'E'rror

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsletterMailId() {
        return newsletterMailId;
    }

    public void setNewsletterMailId(Long newsletterMailId) {
        this.newsletterMailId = newsletterMailId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
