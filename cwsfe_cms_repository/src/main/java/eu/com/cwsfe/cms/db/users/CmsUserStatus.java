package eu.com.cwsfe.cms.db.users;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsUserStatus {

    NEW("N"), DELETED("D"), LOCKED("L");

    private final String code;

    CmsUserStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsUserStatus fromCode(String text) {
        if (text != null) {
            for (CmsUserStatus enumValue : CmsUserStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
