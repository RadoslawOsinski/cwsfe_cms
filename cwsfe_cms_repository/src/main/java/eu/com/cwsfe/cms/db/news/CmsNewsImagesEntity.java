package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = CmsNewsImagesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(cni) FROM CmsNewsImagesEntity cni WHERE status <> 'DELETED'")
@NamedQuery(name = CmsNewsImagesEntity.SEARCH_BY_AJAX_QUERY, query = "SELECT cni FROM CmsNewsImagesEntity cni WHERE status <> 'DELETED' AND newsId = :newsId ORDER BY created DESC")
@NamedQuery(name = CmsNewsImagesEntity.COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(cni) FROM CmsNewsImagesEntity cni WHERE status <> 'DELETED' AND newsId = :newsId ORDER BY created DESC")
@NamedQuery(name = CmsNewsImagesEntity.LIST_IMAGES_FOR_NEWS_WITHOUT_THUBNAILS, query = "SELECT count(cni) FROM CmsNewsImagesEntity cni WHERE status = 'NEW' AND newsId = :newsId AND title NOT LIKE 'thumbnail_%' ORDER BY created DESC")
@NamedQuery(name = CmsNewsImagesEntity.GET_THUBNAIL_FOR_NEWS, query = "SELECT count(cni) FROM CmsNewsImagesEntity cni WHERE status = 'NEW' AND newsId = :newsId AND title LIKE 'thumbnail_%' ORDER BY created DESC")
@Table(name = "cms_news_images")
public class CmsNewsImagesEntity {

    String query =
        "SELECT " +
            " id, news_id, title, file_name, file_size, width, height," +
            " mime_type, created, last_modified, status, url" +
            " FROM CMS_NEWS_IMAGES " +
            "WHERE news_id = ? AND status = 'N' AND title NOT LIKE 'thumbnail_%'";

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsNewsImagesEntity.getTotalNumberNotDeleted";
    public static final String COUNT_TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsNewsImagesEntity.searchByAjaxCountWithoutContent";
    public static final String SEARCH_BY_AJAX_QUERY = "CmsNewsImagesEntity.searchByAjaxWithoutContent";
    public static final String LIST_IMAGES_FOR_NEWS_WITHOUT_THUBNAILS = "CmsNewsImagesEntity.listImagesForNewsWithoutThumbnails";
    public static final String GET_THUBNAIL_FOR_NEWS = "CmsNewsImagesEntity.getThumbnailForNews";

    private long id;
    private long newsId;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_news_images_s")
    @SequenceGenerator(name = "cms_news_images_s", sequenceName = "cms_news_images_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "news_id", nullable = false, precision = 0)
    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
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

        CmsNewsImagesEntity that = (CmsNewsImagesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(newsId, that.newsId)
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
            .append(newsId)
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
        return new ToStringBuilder(this)
            .append("id", id)
            .append("newsId", newsId)
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
