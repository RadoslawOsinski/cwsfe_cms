package eu.com.cwsfe.cms.db.newsletter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "newsletter_mail_addresses")
public class NewsletterMailAddressesEntity {
    private long id;
    private long mailGroupId;
    private String confirmString;
    private String unSubscribeString;
    private String email;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletter_mail_addresses_s")
    @SequenceGenerator(name = "newsletter_mail_addresses_s", sequenceName = "newsletter_mail_addresses_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "mail_group_id", nullable = false, precision = 0)
    public long getMailGroupId() {
        return mailGroupId;
    }

    public void setMailGroupId(long mailGroupId) {
        this.mailGroupId = mailGroupId;
    }

    @Basic
    @Column(name = "confirm_string", nullable = false, length = 36)
    public String getConfirmString() {
        return confirmString;
    }

    public void setConfirmString(String confirmString) {
        this.confirmString = confirmString;
    }

    @Basic
    @Column(name = "un_subscribe_string", nullable = false, length = 36)
    public String getUnSubscribeString() {
        return unSubscribeString;
    }

    public void setUnSubscribeString(String unSubscribeString) {
        this.unSubscribeString = unSubscribeString;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 300)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "status", nullable = false, length = -1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NewsletterMailAddressesEntity that = (NewsletterMailAddressesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(mailGroupId, that.mailGroupId)
            .append(confirmString, that.confirmString)
            .append(unSubscribeString, that.unSubscribeString)
            .append(email, that.email)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(mailGroupId)
            .append(confirmString)
            .append(unSubscribeString)
            .append(email)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("mailGroupId", mailGroupId)
            .append("confirmString", confirmString)
            .append("unSubscribeString", unSubscribeString)
            .append("email", email)
            .append("status", status)
            .toString();
    }
}
