package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsletterTemplateStatus {

    NEW("N"), DELETED("D");

    private final String code;

    NewsletterTemplateStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsletterTemplateStatus fromCode(String text) {
        if (text != null) {
            for (NewsletterTemplateStatus enumValue : NewsletterTemplateStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
