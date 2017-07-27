package eu.com.cwsfe.cms.db.newsletter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "newsletter_mail")
public class NewsletterMailEntity {
    private long id;
    private long recipientGroupId;
    private String name;
    private String subject;
    private String mailContent;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletter_mail_s")
    @SequenceGenerator(name = "newsletter_mail_s", sequenceName = "newsletter_mail_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "recipient_group_id", nullable = false, precision = 0)
    public long getRecipientGroupId() {
        return recipientGroupId;
    }

    public void setRecipientGroupId(long recipientGroupId) {
        this.recipientGroupId = recipientGroupId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "subject", nullable = false, length = 300)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "mail_content", nullable = false, length = -1)
    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
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

        NewsletterMailEntity that = (NewsletterMailEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(recipientGroupId, that.recipientGroupId)
            .append(name, that.name)
            .append(subject, that.subject)
            .append(mailContent, that.mailContent)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(recipientGroupId)
            .append(name)
            .append(subject)
            .append(mailContent)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("recipientGroupId", recipientGroupId)
            .append("name", name)
            .append("subject", subject)
            .append("mailContent", mailContent)
            .append("status", status)
            .toString();
    }
}
