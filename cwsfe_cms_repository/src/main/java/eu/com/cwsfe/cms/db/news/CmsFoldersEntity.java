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
@NamedQuery(name = CmsFoldersEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(f) FROM CmsFoldersEntity f WHERE status <> 'DELETED'")
@NamedQuery(name = CmsFoldersEntity.LIST, query = "SELECT f FROM CmsFoldersEntity f WHERE status = 'NEW' ORDER BY order_number")
@NamedQuery(name = CmsFoldersEntity.LIST_FOLDERS_FOR_DROP_LIST, query = "SELECT f FROM CmsFoldersEntity f WHERE status = 'NEW' and lower(folderName) LIKE lower(:folderName) ORDER BY orderNumber")
@NamedQuery(name = CmsFoldersEntity.GET_BY_FOLDER_NAME, query = "SELECT f FROM CmsFoldersEntity f WHERE folderName = :folderName")
@Table(name = "cms_folders")
public class CmsFoldersEntity {

    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "CmsFoldersEntity.countForAjax";
    public static final String LIST = "CmsFoldersEntity.list";
    public static final String LIST_FOLDERS_FOR_DROP_LIST = "CmsFoldersEntity.listFoldersForDropList";
    public static final String GET_BY_FOLDER_NAME = "CmsFoldersEntity.GET_BY_FOLDER_NAME";

    private long id;
    private Integer parentId;
    private String folderName;
    private Integer orderNumber;
    private String status;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms_folders_s")
    @SequenceGenerator(name = "cms_folders_s", sequenceName = "cms_folders_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "parent_id", nullable = true, precision = 0)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "folder_name", nullable = false, length = 100)
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Basic
    @Column(name = "order_number", nullable = true, precision = 0)
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

        CmsFoldersEntity that = (CmsFoldersEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(parentId, that.parentId)
            .append(folderName, that.folderName)
            .append(orderNumber, that.orderNumber)
            .append(status, that.status)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(parentId)
            .append(folderName)
            .append(orderNumber)
            .append(status)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("id", id)
            .append("parentId", parentId)
            .append("folderName", folderName)
            .append("orderNumber", orderNumber)
            .append("status", status)
            .toString();
    }
}
