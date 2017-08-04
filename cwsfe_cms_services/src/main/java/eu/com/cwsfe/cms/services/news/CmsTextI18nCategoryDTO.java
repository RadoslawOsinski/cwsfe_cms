package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

public class CmsTextI18nCategoryDTO {

    private Long id;
    private String category;
    private NewDeletedStatus status;

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

    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }
}
