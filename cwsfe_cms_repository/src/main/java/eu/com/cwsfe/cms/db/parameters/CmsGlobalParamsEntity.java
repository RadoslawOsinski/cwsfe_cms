package eu.com.cwsfe.cms.db.parameters;

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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQuery(name = CmsGlobalParamsEntity.GET_BY_CODE, query = "SELECT gp FROM CmsGlobalParamsEntity gp WHERE code = :code")
@NamedQuery(name = CmsGlobalParamsEntity.LIST_FOLDERS_FOR_DROP_LIST, query = "SELECT gp FROM CmsGlobalParamsEntity gp WHERE lower(CODE) LIKE lower(:code) ORDER BY CODE")
@NamedQuery(name = CmsGlobalParamsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(gp) FROM CmsGlobalParamsEntity gp")
@NamedQuery(name = CmsGlobalParamsEntity.LIST, query = "SELECT gp FROM CmsGlobalParamsEntity gp ORDER BY CODE")
@Table(name = "cms_global_params")
public class CmsGlobalParamsEntity {

    public static final String GET_BY_CODE = "CmsGlobalParamsEntity.getByCode";
    public static final String LIST_FOLDERS_FOR_DROP_LIST = "CmsGlobalParamsEntity.listForDropList";
    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsGlobalParamsEntity.countForAjax";
    public static final String LIST = "CmsGlobalParamsEntity.list";

    private long id;
    private String code;
    private String defaultValue;
    private String value;
    private String description;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_global_params_s")
    @SequenceGenerator(name = "cms_global_params_s", sequenceName = "cms_global_params_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", nullable = false, length = 200)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "default_value", nullable = false, length = 400)
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Basic
    @Column(name = "value", nullable = false, length = 400)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsGlobalParamsEntity that = (CmsGlobalParamsEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(code, that.code)
            .append(defaultValue, that.defaultValue)
            .append(value, that.value)
            .append(description, that.description)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(code)
            .append(defaultValue)
            .append(value)
            .append(description)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("code", code)
            .append("defaultValue", defaultValue)
            .append("value", value)
            .append("description", description)
            .toString();
    }
}
