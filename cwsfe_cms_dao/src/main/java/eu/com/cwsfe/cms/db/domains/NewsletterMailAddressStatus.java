package eu.com.cwsfe.cms.db.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum NewsletterMailAddressStatus {

    NEW("N"), INACTIVE("I"), ACTIVE("A"), DELETED("D"), ERROR("E");

    private final String code;

    NewsletterMailAddressStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NewsletterMailAddressStatus fromCode(String text) {
        if (text != null) {
            for (NewsletterMailAddressStatus enumValue : NewsletterMailAddressStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
