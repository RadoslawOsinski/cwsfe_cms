package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class CmsAuthor implements Serializable {

    private static final long serialVersionUID = 9134829946664389430L;

    private Long id;
    private String firstName;
    private String lastName;
    private String googlePlusAuthorLink;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGooglePlusAuthorLink() {
        return googlePlusAuthorLink;
    }

    public void setGooglePlusAuthorLink(String googlePlusAuthorLink) {
        this.googlePlusAuthorLink = googlePlusAuthorLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
