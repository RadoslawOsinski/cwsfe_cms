//package eu.com.cwsfe.cms.services.newsletter;
//
//import eu.com.cwsfe.cms.db.newsletter.NewsletterMailAddressStatus;
//
//import java.io.Serializable;
//
///**
// * @author radek
// */
//public class NewsletterMailAddressDTO implements Serializable {
//
//    private static final long serialVersionUID = 7652725822161692802L;
//
//    private Long id;
//    private Long mailGroupId;
//    private String email;
//    private NewsletterMailAddressStatus status;
//    /**
//     * random text for confirming email
//     */
//    private String confirmString;
//    /**
//     * random text for un subscribing email
//     */
//    private String unSubscribeString;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getMailGroupId() {
//        return mailGroupId;
//    }
//
//    public void setMailGroupId(Long mailGroupId) {
//        this.mailGroupId = mailGroupId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public NewsletterMailAddressStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(NewsletterMailAddressStatus status) {
//        this.status = status;
//    }
//
//    public String getConfirmString() {
//        return confirmString;
//    }
//
//    public void setConfirmString(String confirmString) {
//        this.confirmString = confirmString;
//    }
//
//    public String getUnSubscribeString() {
//        return unSubscribeString;
//    }
//
//    public void setUnSubscribeString(String unSubscribeString) {
//        this.unSubscribeString = unSubscribeString;
//    }
//}
