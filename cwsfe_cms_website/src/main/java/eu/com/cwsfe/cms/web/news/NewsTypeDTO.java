package eu.com.cwsfe.cms.web.news;

/**
 * Created by Radosław Osiński
 */
public class NewsTypeDTO {

    private long orderNumber;
    private long id;
    private String type;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
