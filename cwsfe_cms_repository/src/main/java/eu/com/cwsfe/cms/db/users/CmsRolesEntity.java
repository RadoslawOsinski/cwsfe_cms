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
@NamedQuery(name = CmsRolesEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(r) FROM CmsRolesEntity r")
@NamedQuery(name = CmsRolesEntity.LIST, query = "SELECT r FROM CmsRolesEntity r order by roleName")
@NamedQuery(name = CmsRolesEntity.LIST_FOR_DROP_LIST, query = "SELECT r FROM CmsRolesEntity r where lower(roleName) like lower(:roleName) order by roleName")
@NamedQuery(name = CmsRolesEntity.GET_BY_CODE, query = "SELECT r FROM CmsRolesEntity r where roleCode = :roleCode")
@Table(name = "cms_roles")
public class CmsRolesEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsRolesEntity.countForAjax";
    public static final String LIST = "CmsRolesEntity.list";
    public static final String LIST_FOR_DROP_LIST = "CmsRolesEntity.listForDropList";
    public static final String GET_BY_CODE = "CmsRolesEntity.getByCode";

    private long id;
    private String roleCode;
    private String roleName;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_roles_s")
    @SequenceGenerator(name = "cms_roles_s", sequenceName = "cms_roles_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_code", nullable = false, length = 100)
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Basic
    @Column(name = "role_name", nullable = false, length = 200)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CmsRolesEntity that = (CmsRolesEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(roleCode, that.roleCode)
            .append(roleName, that.roleName)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(roleCode)
            .append(roleName)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("roleCode", roleCode)
            .append("roleName", roleName)
            .toString();
    }
}
