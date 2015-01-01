CREATE TABLE CMS_USER_ALLOWED_NET_ADDRESS (
  ID      NUMERIC(3, 0) NOT NULL PRIMARY KEY,
  USER_ID NUMERIC(10)   NOT NULL REFERENCES CMS_USERS (ID),
  INET_ADDRESS    INET          NOT NULL
);
CREATE SEQUENCE CMS_USER_ALLOWED_NET_ADDRESS_S START 1 CACHE 1;