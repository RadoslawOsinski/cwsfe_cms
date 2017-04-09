package eu.com.cwsfe.cms.db.blog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cms_blog_post_comments")
public class CmsBlogPostCommentsEntity {
    private long id;
    private Integer parentCommentId;
    private long blogPostI18NContentId;
    private String comment;
    private String userName;
    private String email;
    private String status;
    private Timestamp created;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_blog_post_comments_s")
    @SequenceGenerator(name = "cms_blog_post_comments_s", sequenceName = "cms_blog_post_comments_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "parent_comment_id", nullable = true, precision = 0)
    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    @Basic
    @Column(name = "blog_post_i18n_content_id", nullable = false, precision = 0)
    public long getBlogPostI18NContentId() {
        return blogPostI18NContentId;
    }

    public void setBlogPostI18NContentId(long blogPostI18NContentId) {
        this.blogPostI18NContentId = blogPostI18NContentId;
    }

    @Basic
    @Column(name = "comment", nullable = false, length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "user_name", nullable = false, length = 250)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Basic
    @Column(name = "created", nullable = false)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsBlogPostCommentsEntity that = (CmsBlogPostCommentsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(blogPostI18NContentId, that.blogPostI18NContentId)
            .append(parentCommentId, that.parentCommentId)
            .append(comment, that.comment)
            .append(userName, that.userName)
            .append(email, that.email)
            .append(status, that.status)
            .append(created, that.created)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(parentCommentId)
            .append(blogPostI18NContentId)
            .append(comment)
            .append(userName)
            .append(email)
            .append(status)
            .append(created)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("parentCommentId", parentCommentId)
            .append("blogPostI18NContentId", blogPostI18NContentId)
            .append("comment", comment)
            .append("userName", userName)
            .append("email", email)
            .append("status", status)
            .append("created", created)
            .toString();
    }
}
