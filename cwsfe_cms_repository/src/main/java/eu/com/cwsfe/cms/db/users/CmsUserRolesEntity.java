package eu.com.cwsfe.cms.db.users;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = CmsUserRolesEntity.LIST_FOR_USER, query = "SELECT ur FROM CmsUserRolesEntity ur where cmsUserId = :userId order by ur.roleMapping.roleName")
@NamedQuery(name = CmsUserRolesEntity.LIST_ROLES_FOR_USER, query = "SELECT ur.roleMapping FROM CmsUserRolesEntity ur where ur.cmsUserId = :userId order by ur.roleMapping.roleName")
@NamedQuery(name = CmsUserRolesEntity.DELETE_USER_ROLES, query = "delete FROM CmsUserRolesEntity ur where ur.cmsUserId = :userId")
@Table(name = "cms_user_roles")
@IdClass(CmsUserRolesEntityPK.class)
public class CmsUserRolesEntity {

    public static final String LIST_FOR_USER = "CmsUserRolesEntity.listForUser";
    public static final String LIST_ROLES_FOR_USER = "CmsUserRolesEntity.listUserRoles";
    public static final String DELETE_USER_ROLES = "CmsUserRolesEntity.deleteUserRoles";

    private long cmsUserId;
    private long roleId;

    private CmsRolesEntity roleMapping;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
    public CmsRolesEntity getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(CmsRolesEntity roleMapping) {
        this.roleMapping = roleMapping;
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
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("cmsUserId", cmsUserId)
            .append("roleId", roleId)
            .toString();
    }
}
