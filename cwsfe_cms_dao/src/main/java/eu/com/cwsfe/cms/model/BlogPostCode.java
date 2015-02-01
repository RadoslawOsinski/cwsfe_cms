package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.domains.BlogPostCodeStatus;

import java.io.Serializable;

public class BlogPostCode implements Serializable {

    private static final long serialVersionUID = 8208367212489388702L;

    private String codeId;
    private Long blogPostId;
    private String code;
    private BlogPostCodeStatus status;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public Long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(Long blogPostId) {
        this.blogPostId = blogPostId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BlogPostCodeStatus getStatus() {
        return status;
    }

    public void setStatus(BlogPostCodeStatus status) {
        this.status = status;
    }
}

