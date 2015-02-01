package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum UserStatus {

    NEW("N"), LOCKED("L"), DELETED("D");

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UserStatus fromCode(String text) {
        if (text != null) {
            for (UserStatus enumValue : UserStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
