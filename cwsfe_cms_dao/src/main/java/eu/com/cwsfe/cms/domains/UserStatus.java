package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum UserStatus {

    NEW("N", "New"), LOCKED("L", "Locked"), DELETED("D", "Deleted");

    private final String code;
    private final String label;

    UserStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
