package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogPostI18nContentStatus {

    NEW("N"), HIDDEN("H"), DELETED("D"), PUBLISHED("P");

    private final String code;

    BlogPostI18nContentStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogPostI18nContentStatus fromCode(String text) {
        if (text != null) {
            for (BlogPostI18nContentStatus enumValue : BlogPostI18nContentStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
