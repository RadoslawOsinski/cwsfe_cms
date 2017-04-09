package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogPostStatus {

    NEW("N"), HIDDEN("H"), PUBLISHED("P"), DELETED("D");

    private final String code;

    BlogPostStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogPostStatus fromCode(String text) {
        if (text != null) {
            for (BlogPostStatus enumValue : BlogPostStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
