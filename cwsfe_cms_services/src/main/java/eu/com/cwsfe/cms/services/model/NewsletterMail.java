package eu.com.cwsfe.cms.services.model;

import eu.com.cwsfe.cms.db.newsletter.NewsletterMailStatus;

import java.io.Serializable;

/**
 * @author radek
 */
public class NewsletterMail implements Serializable {

    private static final long serialVersionUID = -7128940958271962040L;

    private Long id;
    private Long recipientGroupId;
    private String name;
    private String subject;
    private String mailContent;
    private NewsletterMailStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipientGroupId() {
        return recipientGroupId;
    }

    public void setRecipientGroupId(Long recipientGroupId) {
        this.recipientGroupId = recipientGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public NewsletterMailStatus getStatus() {
        return status;
    }

    public void setStatus(NewsletterMailStatus status) {
        this.status = status;
    }
}
