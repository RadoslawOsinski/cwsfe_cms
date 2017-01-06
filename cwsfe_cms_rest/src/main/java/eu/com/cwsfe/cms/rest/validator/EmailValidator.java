package eu.com.cwsfe.cms.rest.validator;

import java.util.regex.Pattern;

/**
 * @author Radoslaw Osinski
 */
public class EmailValidator {

    private static final String EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private EmailValidator() {
        //validator does not need an instance
    }

    public static boolean isValidEmailAddress(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
