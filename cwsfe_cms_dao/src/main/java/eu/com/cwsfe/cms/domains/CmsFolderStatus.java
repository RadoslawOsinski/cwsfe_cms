package eu.com.cwsfe.cms.domains;

/**
 * Created by Radoslaw Osinski.
 */
public enum CmsFolderStatus {

    NEW("N"), DELETED("D");

    private final String code;

    CmsFolderStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CmsFolderStatus fromCode(String text) {
        if (text != null) {
            for (CmsFolderStatus enumValue : CmsFolderStatus.values()) {
                if (enumValue.code.equals(text)) {
                    return enumValue;
                }
            }
        }
        return null;
    }
}
