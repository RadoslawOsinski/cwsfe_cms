package eu.com.cwsfe.cms.web.mail;

import eu.com.cwsfe.cms.db.parameters.CmsGlobalParamsEntity;
import eu.com.cwsfe.cms.services.parameters.CmsGlobalParamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Radoslaw Osinski
 */
@Service
public class CmsMailSender extends JavaMailSenderImpl implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(CmsMailSender.class);

    private final CmsGlobalParamsService cmsGlobalParamsService;

    @Autowired
    public CmsMailSender(CmsGlobalParamsService cmsGlobalParamsService) {
        this.cmsGlobalParamsService = cmsGlobalParamsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initMailSessionFromDataBase();
    }

    private void initMailSessionFromDataBase() {
        Optional<CmsGlobalParamsEntity> mailUserName = cmsGlobalParamsService.getByCode("MAIL_USER_NAME");
        Optional<CmsGlobalParamsEntity> mailUserPassword = cmsGlobalParamsService.getByCode("MAIL_USER_PASSWORD");
        Optional<CmsGlobalParamsEntity> mailSmtpAuth = cmsGlobalParamsService.getByCode("MAIL_SMTP_AUTH");
        Optional<CmsGlobalParamsEntity> mailSmtpStarttlsEnable = cmsGlobalParamsService.getByCode("MAIL_SMTP_STARTTLS_ENABLE");
        Optional<CmsGlobalParamsEntity> mailSmtpHost = cmsGlobalParamsService.getByCode("MAIL_SMTP_HOST");
        Optional<CmsGlobalParamsEntity> mailSmtpPort = cmsGlobalParamsService.getByCode("MAIL_SMTP_PORT");
        if (!mailUserName.isPresent() || !mailUserPassword.isPresent() || !mailSmtpAuth.isPresent() ||
            !mailSmtpStarttlsEnable.isPresent() || !mailSmtpHost.isPresent() || !mailSmtpPort.isPresent()) {
            LOG.error("Missing configuration MAIL_USER_NAME, MAIL_USER_PASSWORD, MAIL_SMTP_AUTH, MAIL_SMTP_STARTTLS_ENABLE, MAIL_SMTP_HOST, MAIL_SMTP_PORT");
        } else {
            final String username = mailUserName.get().getValue();
            final String password = mailUserPassword.get().getValue();
            Properties props = new Properties();
            props.put("mail.smtp.auth", mailSmtpAuth.get().getValue());
            props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable.get().getValue());
            props.put("mail.smtp.host", mailSmtpHost.get().getValue());
            props.put("mail.smtp.port", mailSmtpPort.get().getValue());

            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

            setSession(session);
        }
    }

}
