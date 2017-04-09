package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.domains.NewsTypeStatus;

import java.io.Serializable;

public class NewsType implements Serializable {

    private static final long serialVersionUID = -583912697638536576L;

    private Long id;
    private String type;
    private NewsTypeStatus status;

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

    public NewsTypeStatus getStatus() {
        return status;
    }

    public void setStatus(NewsTypeStatus status) {
        this.status = status;
    }
}
