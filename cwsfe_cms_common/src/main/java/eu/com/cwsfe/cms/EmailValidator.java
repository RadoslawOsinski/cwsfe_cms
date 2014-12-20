package eu.com.cwsfe.cms;

import java.util.regex.Pattern;

/**
 * @author Radoslaw Osinski
 */
public class EmailValidator {

    private EmailValidator() {
    }

    private static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmailAddress(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}


