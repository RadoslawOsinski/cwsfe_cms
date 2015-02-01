package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogPostCommentStatus {

    NEW("N"), BLOCKED("B"), PUBLISHED("P"), DELETED("D"), SPAM("S");

    private final String code;

    BlogPostCommentStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogPostCommentStatus fromCode(String text) {
        if (text != null) {
            for (BlogPostCommentStatus enumValue : BlogPostCommentStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
