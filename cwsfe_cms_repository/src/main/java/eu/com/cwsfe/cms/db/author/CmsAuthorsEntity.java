package eu.com.cwsfe.cms.db.author;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;


/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = CmsAuthorsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(a) FROM CmsAuthorsEntity a WHERE status <> 'DELETED'")
@NamedQuery(name = CmsAuthorsEntity.LIST, query = "SELECT a FROM CmsAuthorsEntity a WHERE status = :status ORDER BY last_name, first_name")
@Table(name = "cms_authors")
public class CmsAuthorsEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsAuthorsEntity.countForAjax";
    public static final String LIST = "CmsAuthorsEntity.list";

    private long id;
    private String firstName;
    private String lastName;
    private String googlePlusAuthorLink;
    private NewDeletedStatus status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_authors_s")
    @SequenceGenerator(name = "cms_authors_s", sequenceName = "cms_authors_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "google_plus_author_link", nullable = true, length = 500)
    public String getGooglePlusAuthorLink() {
        return googlePlusAuthorLink;
    }

    public void setGooglePlusAuthorLink(String googlePlusAuthorLink) {
        this.googlePlusAuthorLink = googlePlusAuthorLink;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsAuthorsEntity that = (CmsAuthorsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(firstName, that.firstName)
            .append(lastName, that.lastName)
            .append(googlePlusAuthorLink, that.googlePlusAuthorLink)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(firstName)
            .append(lastName)
            .append(googlePlusAuthorLink)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("firstName", firstName)
            .append("lastName", lastName)
            .append("googlePlusAuthorLink", googlePlusAuthorLink)
            .append("status", status)
            .toString();
    }
}
