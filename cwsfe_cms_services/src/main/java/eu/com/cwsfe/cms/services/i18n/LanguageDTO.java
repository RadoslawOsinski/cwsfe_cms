package eu.com.cwsfe.cms.services.i18n;

import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

public class LanguageDTO {

    private String code;
    private String name;
    private NewDeletedStatus status;

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
