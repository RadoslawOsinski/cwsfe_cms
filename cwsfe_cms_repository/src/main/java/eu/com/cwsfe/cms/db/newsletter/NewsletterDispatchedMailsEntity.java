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
@Table(name = "newsletter_dispatched_mails")
public class NewsletterDispatchedMailsEntity {
    private long id;
    private long newsletterMailId;
    private String email;
    private String error;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletter_dispatched_mails_s")
    @SequenceGenerator(name = "newsletter_dispatched_mails_s", sequenceName = "newsletter_dispatched_mails_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "newsletter_mail_id", nullable = false, precision = 0)
    public long getNewsletterMailId() {
        return newsletterMailId;
    }

    public void setNewsletterMailId(long newsletterMailId) {
        this.newsletterMailId = newsletterMailId;
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
    @Column(name = "error", nullable = true, length = 500)
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

        NewsletterDispatchedMailsEntity that = (NewsletterDispatchedMailsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(newsletterMailId, that.newsletterMailId)
            .append(email, that.email)
            .append(error, that.error)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(newsletterMailId)
            .append(email)
            .append(error)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("newsletterMailId", newsletterMailId)
            .append("email", email)
            .append("error", error)
            .append("status", status)
            .toString();
    }
}
