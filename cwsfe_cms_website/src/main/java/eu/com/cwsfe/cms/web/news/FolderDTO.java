package eu.com.cwsfe.cms.web.news;

/**
 * Created by Radosław Osiński
 */
public class FolderDTO {

    private long orderNumber;
    private long id;
    private String folderName;
    private Integer folderOrderNumber;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderOrderNumber(Integer folderOrderNumber) {
        this.folderOrderNumber = folderOrderNumber;
    }

    public Integer getFolderOrderNumber() {
        return folderOrderNumber;
    }
}
