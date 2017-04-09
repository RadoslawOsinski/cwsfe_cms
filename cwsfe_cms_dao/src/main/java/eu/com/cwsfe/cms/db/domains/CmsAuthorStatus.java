package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsAuthorStatus {

    NEW("N"), DELETED("D");

    private final String code;

    CmsAuthorStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsAuthorStatus fromCode(String text) {
        if (text != null) {
            for (CmsAuthorStatus enumValue : CmsAuthorStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
