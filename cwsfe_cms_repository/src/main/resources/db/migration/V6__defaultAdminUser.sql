insert into CMS_USERS (ID, USERNAME, PASSWORD_HASH, STATUS) VALUES (NEXTVAL('CMS_USERS_S'), 'CWSFE_CMS_ADMIN', '$2a$10$DvUv.Tit8LGupsjxgoy02.K27jdAMDh0mTwZm1K3mGU3ylQ3zPJ4W', 'N');
insert into CMS_USER_ROLES(CMS_USER_ID, ROLE_ID) VALUES (
  (select id from CMS_USERS where username = 'CWSFE_CMS_ADMIN'),
  (select id from CMS_ROLES where ROLE_CODE = 'ROLE_CWSFE_CMS_ADMIN')
);
