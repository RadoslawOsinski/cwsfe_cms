package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class BlogPostCode implements Serializable {

    private static final long serialVersionUID = 8208367212489388702L;

    private String codeId;
    private Long blogPostId;
    private String code;
    private String status;	//'D'eleted, 'N'ew

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

