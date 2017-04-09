package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsNewsStatus {

    NEW("N"), DELETED("D"), HIDDEN("H"), PUBLISHED("P");

    private final String code;

    CmsNewsStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsNewsStatus fromCode(String text) {
        if (text != null) {
            for (CmsNewsStatus enumValue : CmsNewsStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
