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
@NamedQuery(name = CmsUsersEntity.IS_USER_ACTIVE, query = "SELECT count(u) FROM CmsUsersEntity u where userName = :userName and status in ('NEW')")
@NamedQuery(name = CmsUsersEntity.LIST, query = "SELECT u FROM CmsUsersEntity u where status = 'NEW' order by userName")
@NamedQuery(name = CmsUsersEntity.GET_BY_USER_NAME, query = "SELECT u FROM CmsUsersEntity u where userName = :userName")
@NamedQuery(name = CmsUsersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(u) FROM CmsUsersEntity u where status <> 'DELETED'")
@NamedQuery(name = CmsUsersEntity.LIST_FOR_DROP_LIST, query = "SELECT u FROM CmsUsersEntity u where status = 'NEW' AND lower(userName) LIKE lower(:userName) order by userName")
@Table(name = "cms_users")
public class CmsUsersEntity {

    public static final String IS_USER_ACTIVE = "CmsUsersEntity.isActiveUsernameInDatabase";
    public static final String GET_BY_USER_NAME = "CmsUsersEntity.getByUsername";
    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsUsersEntity.countForAjax";
    public static final String LIST = "CmsUsersEntity.list";
    public static final String LIST_FOR_DROP_LIST = "CmsUsersEntity.listUsersForDropList";

    private long id;
    private String userName;
    private String passwordHash;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_users_s")
    @SequenceGenerator(name = "cms_users_s", sequenceName = "cms_users_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    @Basic
    @Column(name = "password_hash", nullable = false, length = 1024)
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

        CmsUsersEntity that = (CmsUsersEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(userName, that.userName)
            .append(passwordHash, that.passwordHash)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(userName)
            .append(passwordHash)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("username", userName)
            .append("passwordHash", passwordHash)
            .append("status", status)
            .toString();
    }
}
