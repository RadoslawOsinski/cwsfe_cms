//package eu.com.cwsfe.cms.services.users;
//
//import eu.com.cwsfe.cms.db.users.CmsUserStatus;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Created by Radoslaw Osinski.
// */
//public class CmsUserDTO implements Serializable {
//
//    private static final long serialVersionUID = 2768898072697297284L;
//
//    private Long id;
//    private String userName;
//    private String password;
//    private String passwordHash;
//    private CmsUserStatus status;
//    private List<CmsRole> userRoles;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPasswordHash() {
//        return passwordHash;
//    }
//
//    public void setPasswordHash(String passwordHash) {
//        this.passwordHash = passwordHash;
//    }
//
//    public CmsUserStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(CmsUserStatus status) {
//        this.status = status;
//    }
//
//    public List<CmsRole> getUserRoles() {
//        return userRoles;
//    }
//
//    public void setUserRoles(List<CmsRole> userRoles) {
//        this.userRoles = userRoles;
//    }
//}
