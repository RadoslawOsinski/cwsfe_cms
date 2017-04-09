package eu.com.cwsfe.cms.db.blog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Radoslaw Osinski.
 */
public class BlogPostKeywordsEntityPK implements Serializable {

    private static final long serialVersionUID = -805264310457975354L;

    private long blogPostId;
    private long blogKeywordId;

    @Column(name = "blog_post_id", nullable = false, precision = 0)
    @Id
    public long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(long blogPostId) {
        this.blogPostId = blogPostId;
    }

    @Column(name = "blog_keyword_id", nullable = false, precision = 0)
    @Id
    public long getBlogKeywordId() {
        return blogKeywordId;
    }

    public void setBlogKeywordId(long blogKeywordId) {
        this.blogKeywordId = blogKeywordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BlogPostKeywordsEntityPK that = (BlogPostKeywordsEntityPK) o;

        return new EqualsBuilder()
            .append(blogPostId, that.blogPostId)
            .append(blogKeywordId, that.blogKeywordId)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(blogPostId)
            .append(blogKeywordId)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("blogPostId", blogPostId)
            .append("blogKeywordId", blogKeywordId)
            .toString();
    }
}
