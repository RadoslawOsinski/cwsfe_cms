package eu.com.cwsfe.cms.web.i18n;

/**
 * Created by Radosław Osiński
 */
public class LanguageDTO {

    private long orderNumber;
    private String name;
    private String code;
    private long id;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
