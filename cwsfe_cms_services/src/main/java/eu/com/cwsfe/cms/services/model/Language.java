package eu.com.cwsfe.cms.services.model;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

import java.io.Serializable;

public class Language implements Serializable {

    private static final long serialVersionUID = -5722775896833359784L;

    private Long id;
    private String code;
    private String name;
    private NewDeletedStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }
}
