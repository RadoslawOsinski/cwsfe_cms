package eu.com.cwsfe.cms.web.i18n;

/**
 * Created by Radosław Osiński
 */
public class CmsTextI18NCategoryDTO {

    private int orderNumber;
    private String category;
    private long id;
    private String status;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
