package eu.com.cwsfe.cms.web.author;

/**
 * Created by Radosław Osiński
 */
public class AuthorDTO {

    private long orderNumber;
    private long id;
    private String lastName;
    private String firstName;
    private String googlePlusAuthorLink;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setGooglePlusAuthorLink(String googlePlusAuthorLink) {
        this.googlePlusAuthorLink = googlePlusAuthorLink;
    }

    public String getGooglePlusAuthorLink() {
        return googlePlusAuthorLink;
    }
}
