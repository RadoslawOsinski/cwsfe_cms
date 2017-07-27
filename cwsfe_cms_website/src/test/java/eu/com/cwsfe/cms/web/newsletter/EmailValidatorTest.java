package eu.com.cwsfe.cms.web.newsletter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radoslaw Osinski.
 */
@RunWith(value = Parameterized.class)
public class EmailValidatorTest {

    private String emailId;
    private boolean expected;

    public EmailValidatorTest(String emailId, boolean expected) {
        this.emailId = emailId;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: isValid({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"mary@testdomain.com", true},
                {"mary.smith@testdomain.com", true},
                {"mary_smith123@testdomain.com", true},
                {"mary@testdomaindotcom", false},
                {"mary-smith@testdomain", false},
                {"testdomain.com", false},
                {"Radoslaw.Osinski@gmail.com", true}
            }
        );
    }

    @Test
    public void testIsValidEmailAddress() throws Exception {
        assertEquals(expected, EmailValidator.isValidEmailAddress(emailId));
    }
}
