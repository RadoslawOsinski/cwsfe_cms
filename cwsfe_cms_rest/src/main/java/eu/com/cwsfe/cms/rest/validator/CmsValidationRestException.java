package eu.com.cwsfe.cms.rest.validator;

/**
 * Created by Radosław Osiński
 */
public class CmsValidationRestException extends RuntimeException {

    private static final long serialVersionUID = 6303365867525895999L;

    public CmsValidationRestException(String message) {
        super(message);
    }
}
