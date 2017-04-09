package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogPostCodeStatus {

    NEW("N"), DELETED("D");

    private final String code;

    BlogPostCodeStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogPostCodeStatus fromCode(String text) {
        if (text != null) {
            for (BlogPostCodeStatus enumValue : BlogPostCodeStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
