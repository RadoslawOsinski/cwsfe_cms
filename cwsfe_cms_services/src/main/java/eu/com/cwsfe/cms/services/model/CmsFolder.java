package eu.com.cwsfe.cms.services.model;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

import java.io.Serializable;

public class CmsFolder implements Serializable {

    private static final long serialVersionUID = 2963591362503975898L;

    private Long id;
    private Long parentId;
    private String folderName;
    private Long orderNumber;
    private NewDeletedStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }
}
