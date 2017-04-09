package eu.com.cwsfe.cms.db.newsletter;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsletterDispatchedMailStatus {

    SEND("S"), ERROR("E");

    private final String code;

    NewsletterDispatchedMailStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsletterDispatchedMailStatus fromCode(String text) {
        if (text != null) {
            for (NewsletterDispatchedMailStatus enumValue : NewsletterDispatchedMailStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
