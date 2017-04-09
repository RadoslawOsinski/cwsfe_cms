package eu.com.cwsfe.cms.db.users;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Radoslaw Osinski.
 */
public class CmsUserRolesEntityPK implements Serializable {
    private long cmsUserId;
    private long roleId;

    @Column(name = "cms_user_id", nullable = false, precision = 0)
    @Id
    public long getCmsUserId() {
        return cmsUserId;
    }

    public void setCmsUserId(long cmsUserId) {
        this.cmsUserId = cmsUserId;
    }

    @Column(name = "role_id", nullable = false, precision = 0)
    @Id
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

        CmsUserRolesEntityPK that = (CmsUserRolesEntityPK) o;

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
