package eu.com.cwsfe.cms.web.mail;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * @author Radoslaw Osinski
 */
@Service
public class CmsMailSender extends JavaMailSenderImpl {

    private final CmsGlobalParamsRepository cmsGlobalParamsRepository;

    @Autowired
    public CmsMailSender(CmsGlobalParamsRepository cmsGlobalParamsRepository) {
        this.cmsGlobalParamsRepository = cmsGlobalParamsRepository;
    }

    @PostConstruct
    private void initMailSessionFromDataBase() {
        final String username = cmsGlobalParamsRepository.getByCode("MAIL_USER_NAME").getValue();
        final String password = cmsGlobalParamsRepository.getByCode("MAIL_USER_PASSWORD").getValue();
        Properties props = new Properties();
        props.put("mail.smtp.auth", cmsGlobalParamsRepository.getByCode("MAIL_SMTP_AUTH").getValue());
        props.put("mail.smtp.starttls.enable", cmsGlobalParamsRepository.getByCode("MAIL_SMTP_STARTTLS_ENABLE").getValue());
        props.put("mail.smtp.host", cmsGlobalParamsRepository.getByCode("MAIL_SMTP_HOST").getValue());
        props.put("mail.smtp.port", cmsGlobalParamsRepository.getByCode("MAIL_SMTP_PORT").getValue());

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        setSession(session);
    }

}
