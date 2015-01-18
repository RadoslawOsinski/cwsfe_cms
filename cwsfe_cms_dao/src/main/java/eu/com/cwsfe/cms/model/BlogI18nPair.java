package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class BlogI18nPair implements Serializable {

    private static final long serialVersionUID = 1036970556866188319L;

    public BlogI18nPair(BlogPost blogPost, BlogPostI18nContent blogPostI18nContent) {
        this.blogPost = blogPost;
        this.blogPostI18nContent = blogPostI18nContent;
    }

    private BlogPost blogPost;

    private BlogPostI18nContent blogPostI18nContent;

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    public BlogPostI18nContent getBlogPostI18nContent() {
        return blogPostI18nContent;
    }

    public void setBlogPostI18nContent(BlogPostI18nContent blogPostI18nContent) {
        this.blogPostI18nContent = blogPostI18nContent;
    }
}
