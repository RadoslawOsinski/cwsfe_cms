package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsTextI18nCategoryStatus {

    NEW("N"), DELETED("D");

    private final String code;

    CmsTextI18nCategoryStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsTextI18nCategoryStatus fromCode(String text) {
        if (text != null) {
            for (CmsTextI18nCategoryStatus enumValue : CmsTextI18nCategoryStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
