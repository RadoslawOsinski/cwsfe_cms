package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsNewsI18nContentStatus {

    NEW("N"), DELETED("D"), HIDDEN("H"), PUBLISHED("P");

    private final String code;

    CmsNewsI18nContentStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsNewsI18nContentStatus fromCode(String text) {
        if (text != null) {
            for (CmsNewsI18nContentStatus enumValue : CmsNewsI18nContentStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
