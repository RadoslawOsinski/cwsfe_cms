package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum BlogKeywordStatus {

    NEW("N"), DELETED("D");

    private final String code;

    BlogKeywordStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlogKeywordStatus fromCode(String text) {
        if (text != null) {
            for (BlogKeywordStatus enumValue : BlogKeywordStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }

}
