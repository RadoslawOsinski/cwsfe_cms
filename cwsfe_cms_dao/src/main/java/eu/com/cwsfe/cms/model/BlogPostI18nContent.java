package eu.com.cwsfe.cms.model;

import java.io.Serializable;
import java.util.List;

public class BlogPostI18nContent implements Serializable {

    private static final long serialVersionUID = 5476213492537968681L;

    private Long id;
    private Long postId;
    private Long languageId;
    private String postTitle;
    private String postShortcut;
    private String postDescription;
    private List<BlogPostComment> blogPostComments;
    private String status;	//'D'eleted, 'H'idden, 'P'ublished

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostShortcut() {
        return postShortcut;
    }

    public void setPostShortcut(String postShortcut) {
        this.postShortcut = postShortcut;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BlogPostComment> getBlogPostComments() {
        return blogPostComments;
    }

    public void setBlogPostComments(List<BlogPostComment> blogPostComments) {
        this.blogPostComments = blogPostComments;
    }

}
