package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class CmsUserRole implements Serializable {

    private static final long serialVersionUID = 1426778150448717778L;

    private Long cmsUserId;
    private Long roleId;

    public CmsUserRole(Long cmsUserId, Long roleId) {
        this.cmsUserId = cmsUserId;
        this.roleId = roleId;
    }

    public Long getCmsUserId() {
        return cmsUserId;
    }

    public void setCmsUserId(Long cmsUserId) {
        this.cmsUserId = cmsUserId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
