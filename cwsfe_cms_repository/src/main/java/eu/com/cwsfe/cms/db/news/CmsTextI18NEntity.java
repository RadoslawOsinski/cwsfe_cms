package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = CmsTextI18NEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(t) FROM CmsTextI18NEntity t")
@NamedQuery(name = CmsTextI18NEntity.LIST, query = "SELECT t FROM CmsTextI18NEntity t order by i18NKey, i18NCategory")
@NamedQuery(name = CmsTextI18NEntity.FIND_TRANSLATION, query = "SELECT t.i18NText FROM CmsTextI18NEntity t where " +
    "t.langId in (select l.id from CmsLanguagesEntity l where l.code = :language2LetterCode) and " +
    "t.i18NCategory IN (SELECT tc.ID FROM CmsTextI18NCategoriesEntity tc WHERE tc.category = :category) AND " +
    "t.i18NKey = :key")
@Table(name = "cms_text_i18n")
public class CmsTextI18NEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsTextI18NEntity.countForAjax";
    public static final String LIST = "CmsTextI18NEntity.list";
    public static final String FIND_TRANSLATION = "CmsTextI18NEntity.findTranslation";

    private long id;
    private long langId;
    private long i18NCategory;
    private String i18NKey;
    private String i18NText;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_text_i18n_s")
    @SequenceGenerator(name = "cms_text_i18n_s", sequenceName = "cms_text_i18n_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "lang_id", nullable = false, precision = 0)
    public long getLangId() {
        return langId;
    }

    public void setLangId(long langId) {
        this.langId = langId;
    }

    @Basic
    @Column(name = "i18n_category", nullable = false, precision = 0)
    public long getI18NCategory() {
        return i18NCategory;
    }

    public void setI18NCategory(long i18NCategory) {
        this.i18NCategory = i18NCategory;
    }

    @Basic
    @Column(name = "i18n_key", nullable = false, length = 100)
    public String getI18NKey() {
        return i18NKey;
    }

    public void setI18NKey(String i18NKey) {
        this.i18NKey = i18NKey;
    }

    @Basic
    @Column(name = "i18n_text", nullable = true, length = 500)
    public String getI18NText() {
        return i18NText;
    }

    public void setI18NText(String i18NText) {
        this.i18NText = i18NText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsTextI18NEntity that = (CmsTextI18NEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(langId, that.langId)
            .append(i18NCategory, that.i18NCategory)
            .append(i18NKey, that.i18NKey)
            .append(i18NText, that.i18NText)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(langId)
            .append(i18NCategory)
            .append(i18NKey)
            .append(i18NText)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("langId", langId)
            .append("i18NCategory", i18NCategory)
            .append("i18NKey", i18NKey)
            .append("i18NText", i18NText)
            .toString();
    }
}
