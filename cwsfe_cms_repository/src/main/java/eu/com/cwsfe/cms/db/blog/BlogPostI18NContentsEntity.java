package eu.com.cwsfe.cms.db.blog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = BlogPostI18NContentsEntity.GET_BY_LANGUAGE_FOR_POST, query = "SELECT count(bpic) FROM BlogPostI18NContentsEntity bpic WHERE languageId = :languageId AND postId = :postId")
@Table(name = "blog_post_i18n_contents")
public class BlogPostI18NContentsEntity {

    public static final String GET_BY_LANGUAGE_FOR_POST = "BlogPostI18NContentsEntity.getByLanguageForPost";

    private long id;
    private long postId;
    private long languageId;
    private String postTitle;
    private String postShortcut;
    private String postDescription;
    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_post_i18n_contents_s")
    @SequenceGenerator(name = "blog_post_i18n_contents_s", sequenceName = "blog_post_i18n_contents_s")
    @Column(name = "id", nullable = false, precision = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "post_id", nullable = false, precision = 0)
    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "language_id", nullable = false, precision = 0)
    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    @Basic
    @Column(name = "post_title", nullable = false, length = 100)
    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    @Basic
    @Column(name = "post_shortcut", nullable = true, length = -1)
    public String getPostShortcut() {
        return postShortcut;
    }

    public void setPostShortcut(String postShortcut) {
        this.postShortcut = postShortcut;
    }

    @Basic
    @Column(name = "post_description", nullable = true, length = -1)
    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    @Basic
    @Column(name = "status", nullable = false, length = -1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BlogPostI18NContentsEntity that = (BlogPostI18NContentsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(postId, that.postId)
            .append(languageId, that.languageId)
            .append(postTitle, that.postTitle)
            .append(postShortcut, that.postShortcut)
            .append(postDescription, that.postDescription)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(postId)
            .append(languageId)
            .append(postTitle)
            .append(postShortcut)
            .append(postDescription)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("postId", postId)
            .append("languageId", languageId)
            .append("postTitle", postTitle)
            .append("postShortcut", postShortcut)
            .append("postDescription", postDescription)
            .append("status", status)
            .toString();
    }
}
