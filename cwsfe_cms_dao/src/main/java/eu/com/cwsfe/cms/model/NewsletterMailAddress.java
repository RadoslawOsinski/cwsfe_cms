package eu.com.cwsfe.cms.model;

import java.io.Serializable;

/**
 * @author radek
 */
public class NewsletterMailAddress implements Serializable {

    private static final long serialVersionUID = 7652725822161692802L;

    public static final String STATUS_ACTIVE = "A";
    public static final String STATUS_INACTIVE = "I";
    public static final String STATUS_DELETED = "D";
    public static final String STATUS_ERROR = "E";

    private Long id;
    private Long mailGroupId;
    private String email;
    private String status;  //'N'ew, 'I'nactive, 'A'ctive, 'D'eleted, 'E'rror
    private String confirmString;    //random text for confirming email
    private String unSubscribeString;    //random text for un subscribing email

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMailGroupId() {
        return mailGroupId;
    }

    public void setMailGroupId(Long mailGroupId) {
        this.mailGroupId = mailGroupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmString() {
        return confirmString;
    }

    public void setConfirmString(String confirmString) {
        this.confirmString = confirmString;
    }

    public String getUnSubscribeString() {
        return unSubscribeString;
    }

    public void setUnSubscribeString(String unSubscribeString) {
        this.unSubscribeString = unSubscribeString;
    }
}
