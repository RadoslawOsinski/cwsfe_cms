package eu.com.cwsfe.cms.db.users;

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
@Table(name = "cms_user_roles")
@IdClass(CmsUserRolesEntityPK.class)
public class CmsUserRolesEntity {
    private long cmsUserId;
    private long roleId;

    @Id
    @Column(name = "cms_user_id", nullable = false, precision = 0)
    public long getCmsUserId() {
        return cmsUserId;
    }

    public void setCmsUserId(long cmsUserId) {
        this.cmsUserId = cmsUserId;
    }

    @Id
    @Column(name = "role_id", nullable = false, precision = 0)
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsUserRolesEntity that = (CmsUserRolesEntity) o;

        return new EqualsBuilder()
            .append(cmsUserId, that.cmsUserId)
            .append(roleId, that.roleId)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(cmsUserId)
            .append(roleId)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("cmsUserId", cmsUserId)
            .append("roleId", roleId)
            .toString();
    }
}
