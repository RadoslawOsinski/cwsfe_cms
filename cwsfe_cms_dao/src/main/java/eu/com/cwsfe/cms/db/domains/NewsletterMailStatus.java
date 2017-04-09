package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsletterMailStatus {

    NEW("N"), PREPARING("P"), SEND("S"), DELETED("D");

    private final String code;

    NewsletterMailStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsletterMailStatus fromCode(String text) {
        if (text != null) {
            for (NewsletterMailStatus enumValue : NewsletterMailStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
