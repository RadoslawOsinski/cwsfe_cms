package eu.com.cwsfe.cms.web.mail;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
import eu.com.cwsfe.cms.web.configuration.IntegrationTestsDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Radoslaw Osinski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestsDataSource.class, CmsMailSender.class, CmsGlobalParamsDAO.class})
@IfProfileValue(name = "test-groups", values = {"integration_tests_local"})
public class CmsMailSenderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsMailSenderTest.class);

    @Autowired
    CmsMailSender cmsMailSender;

    @Test
    public void testSendingMails() {
        MimeMessage mimeMessage = cmsMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent("Just test content!", "text/html");
            helper.setSubject("Just test subject");
            helper.setReplyTo("Radoslaw.Osinski@cwsfe.pl");
            helper.setTo("Radoslaw.Osinski@cwsfe.pl");
            cmsMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Message sending problem", e);
        }
    }
}