package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsletterMailGroupStatus {

    NEW("N"), DELETED("D");

    private final String code;

    NewsletterMailGroupStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsletterMailGroupStatus fromCode(String text) {
        if (text != null) {
            for (NewsletterMailGroupStatus enumValue : NewsletterMailGroupStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
