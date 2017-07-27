package eu.com.cwsfe.cms.db.i18n;

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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = CmsLanguagesEntity.COUNT_FOR_AJAX_N, query = "SELECT count(l) FROM CmsLanguagesEntity l")
@NamedQuery(name = CmsLanguagesEntity.LIST, query = "SELECT l FROM CmsLanguagesEntity l where status = 'NEW' order by code")
@NamedQuery(name = CmsLanguagesEntity.LIST_FOR_DROP_LIST, query = "SELECT l FROM CmsLanguagesEntity l where status = 'NEW' and (lower(code) like lower(:code) or lower(name) like lower(:name)) order by code, name")
@NamedQuery(name = CmsLanguagesEntity.GET_BY_CODE, query = "SELECT l FROM CmsLanguagesEntity l where code = :code")
@NamedQuery(name = CmsLanguagesEntity.GET_BY_CODE_IGNORE_CASE, query = "SELECT l FROM CmsLanguagesEntity l where lower(code) = lower(:code)")
@Table(name = "cms_languages")
public class CmsLanguagesEntity {

    public static final String COUNT_FOR_AJAX_N = "CmsLanguagesEntity.countForAjax";
    public static final String LIST = "CmsLanguages.list";
    public static final String LIST_FOR_DROP_LIST = "CmsLanguages.listForDroplist";
    public static final String GET_BY_CODE = "CmsLanguages.getByCode";
    public static final String GET_BY_CODE_IGNORE_CASE = "CmsLanguages.getByCodeIgnoreCase";

    private long id;
    private String code;
    private String status;
    private String name;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_languages_s")
    @SequenceGenerator(name = "cms_languages_s", sequenceName = "cms_languages_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", nullable = false, length = 2)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "status", nullable = false, length = -1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 75)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsLanguagesEntity that = (CmsLanguagesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(code, that.code)
            .append(status, that.status)
            .append(name, that.name)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(code)
            .append(status)
            .append(name)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("code", code)
            .append("status", status)
            .append("name", name)
            .toString();
    }
}
