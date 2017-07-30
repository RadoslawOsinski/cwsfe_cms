package eu.com.cwsfe.cms.db.blog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "blog_post_keywords")
@IdClass(BlogPostKeywordsEntityPK.class)
public class BlogPostKeywordsEntity {
    private long blogPostId;
    private long blogKeywordId;

    @Id
    @Column(name = "blog_post_id", nullable = false, precision = 0)
    public long getBlogPostId() {
        return blogPostId;
    }

    public void setBlogPostId(long blogPostId) {
        this.blogPostId = blogPostId;
    }

    @Id
    @Column(name = "blog_keyword_id", nullable = false, precision = 0)
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

        BlogPostKeywordsEntity that = (BlogPostKeywordsEntity) o;

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
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("blogPostId", blogPostId)
            .append("blogKeywordId", blogKeywordId)
            .toString();
    }
}
