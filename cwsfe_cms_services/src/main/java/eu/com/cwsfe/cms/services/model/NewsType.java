package eu.com.cwsfe.cms.services.model;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

import java.io.Serializable;

public class NewsType implements Serializable {

    private static final long serialVersionUID = -583912697638536576L;

    private Long id;
    private String type;
    private NewDeletedStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }
}
