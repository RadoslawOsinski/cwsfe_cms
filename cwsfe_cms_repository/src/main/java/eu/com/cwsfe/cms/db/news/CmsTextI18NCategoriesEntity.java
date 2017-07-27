package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
@NamedQuery(name = CmsTextI18NCategoriesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(tic) FROM CmsTextI18NCategoriesEntity tic")
@NamedQuery(name = CmsTextI18NCategoriesEntity.LIST, query = "SELECT tic FROM CmsTextI18NCategoriesEntity tic order by category")
@NamedQuery(name = CmsTextI18NCategoriesEntity.LIST_FOR_DROP_LIST, query = "SELECT tic FROM CmsTextI18NCategoriesEntity tic where status = 'NEW' AND lower(category) LIKE lower(:category) order by category")
@Table(name = "cms_text_i18n_categories")
public class CmsTextI18NCategoriesEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsTextI18NCategoriesEntity.countForAjax";
    public static final String LIST = "CmsTextI18NCategoriesEntity.list";
    public static final String LIST_FOR_DROP_LIST = "CmsTextI18NCategoriesEntity.listForDropList";

    private long id;
    private String category;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_text_i18n_categories_s")
    @SequenceGenerator(name = "cms_text_i18n_categories_s", sequenceName = "cms_text_i18n_categories_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "category", nullable = false, length = 100)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

        CmsTextI18NCategoriesEntity that = (CmsTextI18NCategoriesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(category, that.category)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(category)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("category", category)
            .append("status", status)
            .toString();
    }
}
