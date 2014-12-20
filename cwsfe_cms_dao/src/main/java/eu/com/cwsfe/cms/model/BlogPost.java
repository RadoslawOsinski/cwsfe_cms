package eu.com.cwsfe.cms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BlogPost implements Serializable {

    private static final long serialVersionUID = 5430133688827751872L;

    private Long id;
    private Long postAuthorId;
    private String postTextCode;
    private Date postCreationDate;
    private CmsAuthor cmsAuthor;
    private List<BlogKeyword> blogKeywords;
    private Map<String, BlogPostI18nContent> blogPostI18nContent;
    private String status;	//'D'eleted, 'H'idden, 'P'ublished

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostAuthorId() {
        return postAuthorId;
    }

    public void setPostAuthorId(Long postAuthorId) {
        this.postAuthorId = postAuthorId;
    }

    public String getPostTextCode() {
        return postTextCode;
    }

    public void setPostTextCode(String postTextCode) {
        this.postTextCode = postTextCode;
    }

    public Date getPostCreationDate() {
        return postCreationDate;
    }

    public void setPostCreationDate(Date postCreationDate) {
        this.postCreationDate = postCreationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CmsAuthor getCmsAuthor() {
        return cmsAuthor;
    }

    public void setCmsAuthor(CmsAuthor cmsAuthor) {
        this.cmsAuthor = cmsAuthor;
    }

    public List<BlogKeyword> getBlogKeywords() {
        return blogKeywords;
    }

    public void setBlogKeywords(List<BlogKeyword> blogKeywords) {
        this.blogKeywords = blogKeywords;
    }

    public Map<String, BlogPostI18nContent> getBlogPostI18nContent() {
        return blogPostI18nContent;
    }

    public void setBlogPostI18nContent(Map<String, BlogPostI18nContent> blogPostI18nContent) {
        this.blogPostI18nContent = blogPostI18nContent;
    }
}
