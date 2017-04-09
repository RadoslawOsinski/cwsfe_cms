package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.domains.CmsFolderStatus;

import java.io.Serializable;

public class CmsFolder implements Serializable {

    private static final long serialVersionUID = 2963591362503975898L;

    private Long id;
    private Long parentId;
    private String folderName;
    private Long orderNumber;
    private CmsFolderStatus status;

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

    public CmsFolderStatus getStatus() {
        return status;
    }

    public void setStatus(CmsFolderStatus status) {
        this.status = status;
    }
}
