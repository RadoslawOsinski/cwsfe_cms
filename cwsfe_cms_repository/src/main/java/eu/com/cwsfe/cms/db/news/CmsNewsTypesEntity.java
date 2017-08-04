package eu.com.cwsfe.cms.db.news;

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
@NamedQuery(name = CmsNewsTypesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(nt) FROM CmsNewsTypesEntity nt WHERE status <> 'DELETED'")
@NamedQuery(name = CmsNewsTypesEntity.LIST, query = "SELECT nt FROM CmsNewsTypesEntity nt WHERE status = 'NEW' order by type")
@NamedQuery(name = CmsNewsTypesEntity.LIST_FOR_DROP_LIST, query = "SELECT nt FROM CmsNewsTypesEntity nt WHERE status = 'NEW' and lower(type) LIKE lower(:type) order by type")
@NamedQuery(name = CmsNewsTypesEntity.GET_BY_TYPE, query = "SELECT nt FROM CmsNewsTypesEntity nt WHERE type = :type")
@Table(name = "cms_news_types")
public class CmsNewsTypesEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsNewsTypesEntity.countForAjax";
    public static final String LIST = "CmsNewsTypesEntity.list";
    public static final String LIST_FOR_DROP_LIST = "CmsNewsTypesEntity.listNewsTypesForDropList";
    public static final String GET_BY_TYPE = "CmsNewsTypesEntity.getByType";

    private long id;
    private String type;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_news_types_s")
    @SequenceGenerator(name = "cms_news_types_s", sequenceName = "cms_news_types_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "status", nullable = false)
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

        CmsNewsTypesEntity that = (CmsNewsTypesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(type, that.type)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(type)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("type", type)
            .append("status", status)
            .toString();
    }
}
