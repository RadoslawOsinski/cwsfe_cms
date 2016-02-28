package eu.com.cwsfe.cms.web.mail;

/**
 * @author Radoslaw Osinski
 */
class SendEmailRequest {

    private String replayToEmail;
    private String emailText;

    SendEmailRequest(String replayToEmail, String emailText) {
        this.replayToEmail = replayToEmail;
        this.emailText = emailText;
    }

    String getReplayToEmail() {
        return replayToEmail;
    }

    String getEmailText() {
        return emailText;
    }
}
