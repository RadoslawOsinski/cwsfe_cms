package eu.com.cwsfe.cms.db.blog;

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
@Table(name = "blog_posts")
public class BlogPostsEntity {
    private long id;
    private long postAuthorId;
    private String postTextCode;
    private Timestamp postCreationDate;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_posts_s")
    @SequenceGenerator(name = "blog_posts_s", sequenceName = "blog_posts_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "post_author_id", nullable = false, precision = 0)
    public long getPostAuthorId() {
        return postAuthorId;
    }

    public void setPostAuthorId(long postAuthorId) {
        this.postAuthorId = postAuthorId;
    }

    @Basic
    @Column(name = "post_text_code", nullable = false, length = 100)
    public String getPostTextCode() {
        return postTextCode;
    }

    public void setPostTextCode(String postTextCode) {
        this.postTextCode = postTextCode;
    }

    @Basic
    @Column(name = "post_creation_date", nullable = true)
    public Timestamp getPostCreationDate() {
        return postCreationDate;
    }

    public void setPostCreationDate(Timestamp postCreationDate) {
        this.postCreationDate = postCreationDate;
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

        BlogPostsEntity that = (BlogPostsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(postAuthorId, that.postAuthorId)
            .append(postTextCode, that.postTextCode)
            .append(postCreationDate, that.postCreationDate)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(postAuthorId)
            .append(postTextCode)
            .append(postCreationDate)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("postAuthorId", postAuthorId)
            .append("postTextCode", postTextCode)
            .append("postCreationDate", postCreationDate)
            .append("status", status)
            .toString();
    }
}
