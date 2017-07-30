package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cms_news")
public class CmsNewsEntity {
    private long id;
    private long authorId;
    private long newsTypeId;
    private long folderId;
    private Timestamp creationDate;
    private String newsCode;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_news_s")
    @SequenceGenerator(name = "cms_news_s", sequenceName = "cms_news_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "author_id", nullable = false, precision = 0)
    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Basic
    @Column(name = "news_type_id", nullable = false, precision = 0)
    public long getNewsTypeId() {
        return newsTypeId;
    }

    public void setNewsTypeId(long newsTypeId) {
        this.newsTypeId = newsTypeId;
    }

    @Basic
    @Column(name = "folder_id", nullable = false, precision = 0)
    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    @Basic
    @Column(name = "creation_date", nullable = false)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "news_code", nullable = false, length = 300)
    public String getNewsCode() {
        return newsCode;
    }

    public void setNewsCode(String newsCode) {
        this.newsCode = newsCode;
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

        CmsNewsEntity that = (CmsNewsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(authorId, that.authorId)
            .append(newsTypeId, that.newsTypeId)
            .append(folderId, that.folderId)
            .append(creationDate, that.creationDate)
            .append(newsCode, that.newsCode)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(authorId)
            .append(newsTypeId)
            .append(folderId)
            .append(creationDate)
            .append(newsCode)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("authorId", authorId)
            .append("newsTypeId", newsTypeId)
            .append("folderId", folderId)
            .append("creationDate", creationDate)
            .append("newsCode", newsCode)
            .append("status", status)
            .toString();
    }
}
