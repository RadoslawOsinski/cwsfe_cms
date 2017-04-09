package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.blog.BlogPostImageStatus;

public class BlogPostImage extends CmsImage {

    private static final long serialVersionUID = -153934545387666806L;

    private Long blogPostId;
    private BlogPostImageStatus status;

    public Long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(Long blogPostId) {
        this.blogPostId = blogPostId;
    }

    public BlogPostImageStatus getStatus() {
        return status;
    }

    public void setStatus(BlogPostImageStatus status) {
        this.status = status;
    }

}

