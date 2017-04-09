package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "cms_news_i18n_contents")
public class CmsNewsI18NContentsEntity {
    private long id;
    private long newsId;
    private long languageId;
    private String newsTitle;
    private String newsShortcut;
    private String newsDescription;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_news_i18n_contents_s")
    @SequenceGenerator(name = "cms_news_i18n_contents_s", sequenceName = "cms_news_i18n_contents_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "news_id", nullable = false, precision = 0)
    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
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
    @Column(name = "news_title", nullable = false, length = 100)
    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    @Basic
    @Column(name = "news_shortcut", nullable = true, length = -1)
    public String getNewsShortcut() {
        return newsShortcut;
    }

    public void setNewsShortcut(String newsShortcut) {
        this.newsShortcut = newsShortcut;
    }

    @Basic
    @Column(name = "news_description", nullable = true, length = -1)
    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
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

        CmsNewsI18NContentsEntity that = (CmsNewsI18NContentsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(newsId, that.newsId)
            .append(languageId, that.languageId)
            .append(newsTitle, that.newsTitle)
            .append(newsShortcut, that.newsShortcut)
            .append(newsDescription, that.newsDescription)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(newsId)
            .append(languageId)
            .append(newsTitle)
            .append(newsShortcut)
            .append(newsDescription)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("newsId", newsId)
            .append("languageId", languageId)
            .append("newsTitle", newsTitle)
            .append("newsShortcut", newsShortcut)
            .append("newsDescription", newsDescription)
            .append("status", status)
            .toString();
    }
}
