package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

public class CmsFolderDTO {

    private Long parentFolderName;
    private String folderName;
    private Long orderNumber;
    private NewDeletedStatus status;

    public Long getParentFolderName() {
        return parentFolderName;
    }

    public void setParentFolderName(Long parentFolderName) {
        this.parentFolderName = parentFolderName;
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
