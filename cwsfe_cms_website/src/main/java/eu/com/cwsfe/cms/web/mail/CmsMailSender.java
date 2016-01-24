package eu.com.cwsfe.cms.web.mail;

import eu.com.cwsfe.cms.dao.CmsGlobalParamsDAO;
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

    @Autowired
    private CmsGlobalParamsDAO cmsGlobalParamsDAO;

    @PostConstruct
    private void initMailSessionFromDataBase() {
        final String username = cmsGlobalParamsDAO.getByCode("MAIL_USER_NAME").getValue();
        final String password = cmsGlobalParamsDAO.getByCode("MAIL_USER_PASSWORD").getValue();
        Properties props = new Properties();
        props.put("mail.smtp.auth", cmsGlobalParamsDAO.getByCode("MAIL_SMTP_AUTH").getValue());
        props.put("mail.smtp.starttls.enable", cmsGlobalParamsDAO.getByCode("MAIL_SMTP_STARTTLS_ENABLE").getValue());
        props.put("mail.smtp.host", cmsGlobalParamsDAO.getByCode("MAIL_SMTP_HOST").getValue());
        props.put("mail.smtp.port", cmsGlobalParamsDAO.getByCode("MAIL_SMTP_PORT").getValue());

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        setSession(session);
    }

}
