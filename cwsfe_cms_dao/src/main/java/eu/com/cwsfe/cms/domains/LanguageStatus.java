package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum LanguageStatus {

    NEW("N"), DELETED("D");

    private final String code;

    LanguageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LanguageStatus fromCode(String text) {
        if (text != null) {
            for (LanguageStatus enumValue : LanguageStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
