package eu.com.cwsfe.cms.db.news;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsNewsImageStatus {

    NEW("N"), DELETED("D"), PUBLISHED("P");

    private final String code;

    CmsNewsImageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsNewsImageStatus fromCode(String text) {
        if (text != null) {
            for (CmsNewsImageStatus enumValue : CmsNewsImageStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
