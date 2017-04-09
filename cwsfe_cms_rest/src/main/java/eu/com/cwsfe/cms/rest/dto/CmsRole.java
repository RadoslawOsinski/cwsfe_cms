package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class CmsRole implements Serializable {

    private static final long serialVersionUID = -7252497588396359557L;

    private Long id;
    private String roleName;
    private String roleCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
