package eu.com.cwsfe.cms.db.news;

import eu.com.cwsfe.cms.db.author.CmsAuthorsEntity;
import eu.com.cwsfe.cms.db.common.PublishedHiddenStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@NamedQuery(name = CmsNewsEntity.GET_TOTAL_NUMBER_NOT_DELETED, query = "SELECT count(n) FROM CmsNewsEntity n WHERE n.status <> :status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cms_news")
public class CmsNewsEntity {

    public static final String GET_TOTAL_NUMBER_NOT_DELETED = "CmsNewsEntity.GET_TOTAL_NUMBER_NOT_DELETED";

    private long id;
    private long authorId;
    private long newsTypeId;
    private long newsFolderId;
    private ZonedDateTime creationDate;
    private String newsCode;
    private PublishedHiddenStatus status;

    private CmsAuthorsEntity authorMapping;
    private CmsNewsTypesEntity type;
    private CmsFoldersEntity folder;

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
    public long getNewsFolderId() {
        return newsFolderId;
    }

    public void setNewsFolderId(long folderId) {
        this.newsFolderId = folderId;
    }

    @Basic
    @Column(name = "creation_date", nullable = false)
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
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
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public PublishedHiddenStatus getStatus() {
        return status;
    }

    public void setStatus(PublishedHiddenStatus status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, insertable = false, updatable = false)
    public CmsAuthorsEntity getAuthorMapping() {
        return authorMapping;
    }

    public void setAuthorMapping(CmsAuthorsEntity authorMapping) {
        this.authorMapping = authorMapping;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_type_id", nullable = false, insertable = false, updatable = false)
    public CmsNewsTypesEntity getType() {
        return type;
    }

    public void setType(CmsNewsTypesEntity type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false, insertable = false, updatable = false)
    public CmsFoldersEntity getFolder() {
        return folder;
    }

    public void setFolder(CmsFoldersEntity folder) {
        this.folder = folder;
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
            .append(newsFolderId, that.newsFolderId)
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
            .append(newsFolderId)
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
            .append("newsFolderId", newsFolderId)
            .append("creationDate", creationDate)
            .append("newsCode", newsCode)
            .append("status", status)
            .toString();
    }
}
