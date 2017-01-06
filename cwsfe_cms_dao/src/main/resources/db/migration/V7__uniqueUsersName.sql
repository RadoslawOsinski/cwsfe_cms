ALTER TABLE CMS_USERS
    ADD CONSTRAINT cms_users_username_unique UNIQUE (username);
