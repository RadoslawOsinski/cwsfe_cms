package eu.com.cwsfe.cms.db.blog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = BlogPostImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(bpi) FROM BlogPostImagesEntity bpi WHERE status <> 'DELETED'")
@NamedQuery(name = BlogPostImagesEntity.SEARCH_BY_AJAX_QUERY, query = "SELECT bpi FROM BlogPostImagesEntity bpi WHERE status <> 'DELETED' AND blogPostId = :postId ORDER BY created DESC")
@NamedQuery(name = BlogPostImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(bpi) FROM BlogPostImagesEntity bpi WHERE status <> 'DELETED' AND blogPostId = :postId ORDER BY created DESC")
@Table(name = "blog_post_images")
public class BlogPostImagesEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "BlogPostImagesEntity.getTotalNumberNotDeleted";
    public static final String SEARCH_BY_AJAX_QUERY = "BlogPostImagesEntity.searchByAjaxWithoutContent";
    public static final String COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY = "BlogPostImagesEntity.searchByAjaxCountWithoutContent";

    private long id;
    private long blogPostId;
    private String title;
    private String fileName;
    private long fileSize;
    private Integer width;
    private Integer height;
    private String mimeType;
    private Timestamp created;
    private String status;
    private Timestamp lastModified;
    private String url;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_post_images_s")
    @SequenceGenerator(name = "blog_post_images_s", sequenceName = "blog_post_images_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "blog_post_id", nullable = false, precision = 0)
    public long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(long blogPostId) {
        this.blogPostId = blogPostId;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "file_name", nullable = false, length = 250)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file_size", nullable = false, precision = 0)
    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Basic
    @Column(name = "width", nullable = true, precision = 0)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Basic
    @Column(name = "height", nullable = true, precision = 0)
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "mime_type", nullable = true, length = 100)
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Basic
    @Column(name = "created", nullable = false)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Basic
    @Column(name = "status", nullable = false, length = -1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "last_modified", nullable = false)
    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 1000)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BlogPostImagesEntity that = (BlogPostImagesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(blogPostId, that.blogPostId)
            .append(fileSize, that.fileSize)
            .append(title, that.title)
            .append(fileName, that.fileName)
            .append(width, that.width)
            .append(height, that.height)
            .append(mimeType, that.mimeType)
            .append(created, that.created)
            .append(status, that.status)
            .append(lastModified, that.lastModified)
            .append(url, that.url)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(blogPostId)
            .append(title)
            .append(fileName)
            .append(fileSize)
            .append(width)
            .append(height)
            .append(mimeType)
            .append(created)
            .append(status)
            .append(lastModified)
            .append(url)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("blogPostId", blogPostId)
            .append("title", title)
            .append("fileName", fileName)
            .append("fileSize", fileSize)
            .append("width", width)
            .append("height", height)
            .append("mimeType", mimeType)
            .append("created", created)
            .append("status", status)
            .append("lastModified", lastModified)
            .append("url", url)
            .toString();
    }
}
