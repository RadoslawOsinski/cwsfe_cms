package eu.com.cwsfe.cms.db.users;

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
@NamedQuery(name = CmsUserAllowedNetAddressEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(uana) FROM CmsUserAllowedNetAddressEntity uana")
@NamedQuery(name = CmsUserAllowedNetAddressEntity.LIST, query = "SELECT uana FROM CmsUserAllowedNetAddressEntity uana ORDER BY inetAddress")
@NamedQuery(name = CmsUserAllowedNetAddressEntity.COUNT_ADDRESSES_FOR_USER, query = "SELECT count(uana) FROM CmsUserAllowedNetAddressEntity uana where userId = :userId")
@NamedQuery(name = CmsUserAllowedNetAddressEntity.LIST_FOR_USER, query = "SELECT uana FROM CmsUserAllowedNetAddressEntity uana where userId = :userId ORDER BY inetAddress")
@Table(name = "cms_user_allowed_net_address")
public class CmsUserAllowedNetAddressEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsUserAllowedNetAddressEntity.countForAjax";
    public static final String LIST = "CmsUserAllowedNetAddressEntity.list";
    public static final String COUNT_ADDRESSES_FOR_USER = "CmsUserAllowedNetAddressEntity.countAddressesForUser";
    public static final String LIST_FOR_USER = "CmsUserAllowedNetAddressEntity.listForUser";

    private long id;
    private long userId;
    private String inetAddress;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_user_allowed_net_address_s")
    @SequenceGenerator(name = "cms_user_allowed_net_address_s", sequenceName = "cms_user_allowed_net_address_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = false, precision = 0)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "inet_address", nullable = false)
    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsUserAllowedNetAddressEntity that = (CmsUserAllowedNetAddressEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(userId, that.userId)
            .append(inetAddress, that.inetAddress)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(userId)
            .append(inetAddress)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("userId", userId)
            .append("inetAddress", inetAddress)
            .toString();
    }
}
