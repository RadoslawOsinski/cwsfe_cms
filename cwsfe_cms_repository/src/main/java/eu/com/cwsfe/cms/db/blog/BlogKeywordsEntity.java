package eu.com.cwsfe.cms.db.blog;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;


/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Table(name = "blog_keywords")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = BlogKeywordsEntity.COUNT_FOR_AJAX_N, query = "SELECT count(bk) FROM BlogKeywordsEntity bk where bk.status <> 'DELETED'")
@NamedQuery(name = BlogKeywordsEntity.LIST_N, query = "SELECT bk FROM BlogKeywordsEntity bk where bk.status = 'NEW' ORDER BY bk.keywordName asc")
public class BlogKeywordsEntity {

    public static final String COUNT_FOR_AJAX_N = "BlogKeywordsEntity.countForAjax";
    public static final String LIST_N = "BlogKeywordsEntity.list";

    private Long id;
    private String keywordName;
    private NewDeletedStatus status;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_keywords_s")
    @SequenceGenerator(name = "blog_keywords_s", sequenceName = "blog_keywords_s")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "keyword_name", nullable = false, length = 100)
    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    @Basic
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogKeywordsEntity that = (BlogKeywordsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(keywordName, that.keywordName)
            .append(status, that.status)
            .isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(keywordName)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("keywordName", keywordName)
            .append("status", status)
            .toString();
    }

}
