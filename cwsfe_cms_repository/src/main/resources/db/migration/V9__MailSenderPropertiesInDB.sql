INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_USER_NAME', 'user name', 'user name', 'Mail sender user name'
);
INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_USER_PASSWORD', 'user password', 'user password', 'Mail sender user password'
);
INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_SMTP_AUTH', 'true', 'true', 'Mail sender SMTP AUTH'
);
INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_SMTP_STARTTLS_ENABLE', 'true', 'true', 'Mail sender SMTP STARTTLS enabled'
);
INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_SMTP_HOST', 'smtp.gmail.com', 'smtp.gmail.com', 'Mail sender SMTP host'
);
INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'MAIL_SMTP_PORT', '587', '587', 'Mail sender SMTP port'
);