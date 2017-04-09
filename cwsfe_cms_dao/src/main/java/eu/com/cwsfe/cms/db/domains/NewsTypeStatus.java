package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsTypeStatus {

    NEW("N"), DELETED("D");

    private final String code;

    NewsTypeStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsTypeStatus fromCode(String text) {
        if (text != null) {
            for (NewsTypeStatus enumValue : NewsTypeStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
