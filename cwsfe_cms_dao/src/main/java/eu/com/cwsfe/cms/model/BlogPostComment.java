package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.domains.BlogPostCommentStatus;

import java.io.Serializable;
import java.util.Date;

public class BlogPostComment implements Serializable {

    private static final long serialVersionUID = -6996303042973394149L;

    private Long id;
    private Long parentCommentId;
    private Long blogPostI18nContentId;
    private String comment;
    private String userName;
    private String email;
    private BlogPostCommentStatus status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BlogPostCommentStatus getStatus() {
        return status;
    }

    public void setStatus(BlogPostCommentStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
