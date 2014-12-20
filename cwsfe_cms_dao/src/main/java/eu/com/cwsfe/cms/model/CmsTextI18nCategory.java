package eu.com.cwsfe.cms.model;

import java.io.Serializable;

/**
 * @author radek
 */
public class CmsTextI18nCategory implements Serializable {

    private static final long serialVersionUID = 5414191324758345253L;

    private Long id;
    private String category;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
