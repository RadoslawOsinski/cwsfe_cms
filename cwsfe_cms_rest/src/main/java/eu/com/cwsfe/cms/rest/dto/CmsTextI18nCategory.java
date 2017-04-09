package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.domains.CmsTextI18nCategoryStatus;

import java.io.Serializable;

/**
 * @author radek
 */
public class CmsTextI18nCategory implements Serializable {

    private static final long serialVersionUID = 5414191324758345253L;

    private Long id;
    private String category;
    private CmsTextI18nCategoryStatus status;

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

    public CmsTextI18nCategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CmsTextI18nCategoryStatus status) {
        this.status = status;
    }
}
