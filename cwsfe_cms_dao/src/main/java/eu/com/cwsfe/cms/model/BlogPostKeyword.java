package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class BlogPostKeyword implements Serializable {

    private static final long serialVersionUID = -6427929718160154162L;

    private Long blogPostId;
    private Long blogKeywordId;

    public BlogPostKeyword(Long blogPostId, Long blogKeywordId) {
        this.blogPostId = blogPostId;
        this.blogKeywordId = blogKeywordId;
    }

    public Long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(Long blogPostId) {
        this.blogPostId = blogPostId;
    }

    public Long getBlogKeywordId() {
        return blogKeywordId;
    }

    public void setBlogKeywordId(Long blogKeywordId) {
        this.blogKeywordId = blogKeywordId;
    }
}
