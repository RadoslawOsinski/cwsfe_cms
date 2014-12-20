package eu.com.cwsfe.cms.model;

import java.io.Serializable;
import java.util.Date;

public class BlogPostComment implements Serializable {

    private static final long serialVersionUID = -6996303042973394149L;

    private Long id;
    private Long parentCommentId;
    private Long blogPostI18nContentId;
    private String comment;
    private String username;
    private String email;
    private String status;	//'N'ew, 'P'ublished, 'B'locked
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Long getBlogPostI18nContentId() {
        return blogPostI18nContentId;
    }

    public void setBlogPostI18nContentId(Long blogPostI18nContentId) {
        this.blogPostI18nContentId = blogPostI18nContentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
