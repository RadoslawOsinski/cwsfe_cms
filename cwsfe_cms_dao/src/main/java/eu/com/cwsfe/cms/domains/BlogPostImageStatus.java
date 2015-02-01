package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogPostImageStatus {

    NEW("N"), DELETED("D"), PUBLISHED("P");

    private final String code;

    BlogPostImageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogPostImageStatus fromCode(String text) {
        if (text != null) {
            for (BlogPostImageStatus enumValue : BlogPostImageStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
