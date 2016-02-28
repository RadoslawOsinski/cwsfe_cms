package eu.com.cwsfe.cms.web.mail;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Radoslaw Osinski
 */
@RestController
public class MailRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailRestController.class);

    @Lazy
    @Autowired
    private JavaMailSender cmsMailSender;

    @Autowired
    private JWTDecoratorService jwtDecoratorService;

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @RequestMapping(value = "/rest/sendEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public void getTranslation(
            @RequestParam(value = "requestJWT") String requestJWT
    ) {
        LOGGER.info("Send email via CMS request JWT: " + requestJWT);
        SendEmailRequest sendEmailRequest = jwtDecoratorService.getSendEmailRequest(requestJWT);
        sendEmail(sendEmailRequest.getReplayToEmail(), sendEmailRequest.getEmailText());
    }

    private void sendEmail(String replayToEmail, String emailText) {
        LOGGER.info("Sending email from {} with text: {}", replayToEmail, emailText);
        MimeMessage mimeMessage = cmsMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(cmsGlobalParamsDAO.getByCode("MAIL_USER_NAME").getValue());
            mimeMessage.setContent(emailText, "text/html");
            helper.setSubject("[CMS] Contact mail from " + replayToEmail);
            helper.setReplyTo(replayToEmail);
        } catch (MessagingException e) {
            LOGGER.error("Problem with sending message", e);
        }
        cmsMailSender.send(mimeMessage);
    }

}
