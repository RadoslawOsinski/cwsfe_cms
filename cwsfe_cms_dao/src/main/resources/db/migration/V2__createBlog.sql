CREATE TABLE BLOG_POSTS (
  id                 NUMERIC(6, 0) PRIMARY KEY,
  post_author_id     NUMERIC(6, 0) NOT NULL REFERENCES CMS_AUTHORS (id),
  post_text_code     VARCHAR(100)  NOT NULL,
  post_creation_date TIMESTAMP,
  status             CHAR(1)       NOT NULL
);
CREATE SEQUENCE BLOG_POSTS_S START 1 CACHE 1;
CREATE INDEX BLOG_POSTS_POST_AUTHOR_ID_INDEX ON BLOG_POSTS (post_author_id);

CREATE TABLE BLOG_POST_I18N_CONTENTS (
  id               NUMERIC(6, 0) PRIMARY KEY,
  post_id          NUMERIC(6, 0) NOT NULL REFERENCES BLOG_POSTS (id),
  language_id      NUMERIC(3, 0) NOT NULL REFERENCES cms_languages (id),
  post_title       VARCHAR(100)  NOT NULL,
  post_shortcut    TEXT,
  post_description TEXT,
  status           CHAR(1)       NOT NULL
);
CREATE SEQUENCE BLOG_POST_I18N_CONTENTS_S START 1 CACHE 1;
CREATE INDEX BLOG_POST_I18N_CONTENTS_POST_ID_INDEX ON BLOG_POST_I18N_CONTENTS (post_id);
CREATE INDEX BLOG_POST_I18N_CONTENTS_LANGUAGE_ID_INDEX ON BLOG_POST_I18N_CONTENTS (language_id);

CREATE TABLE BLOG_KEYWORDS (
  id           NUMERIC(6, 0) PRIMARY KEY,
  keyword_name VARCHAR(100) NOT NULL UNIQUE,
  status       CHAR(1)      NOT NULL
);
CREATE SEQUENCE BLOG_KEYWORDS_S START 1 CACHE 1;

CREATE TABLE BLOG_POST_KEYWORDS (
  blog_post_id    NUMERIC(6, 0) NOT NULL REFERENCES BLOG_POSTS (id),
  blog_keyword_id NUMERIC(6, 0) NOT NULL REFERENCES BLOG_KEYWORDS (id),
  PRIMARY KEY (blog_post_id, blog_keyword_id)
);

CREATE TABLE BLOG_POST_IMAGES (
  id           NUMERIC(12, 0) PRIMARY KEY,
  blog_post_id NUMERIC(6, 0)  NOT NULL REFERENCES BLOG_POSTS (id),
  title        VARCHAR(100)   NOT NULL,
  file_name    VARCHAR(250)   NOT NULL,
  file_size    NUMERIC(12, 0) NOT NULL,
  width        NUMERIC(6, 0),
  height       NUMERIC(6, 0),
  mime_type    VARCHAR(100),
  content      BYTEA          NOT NULL,
  created      TIMESTAMP      NOT NULL,
  status       CHAR(1)        NOT NULL
);
CREATE INDEX BLOG_POST_IMAGES_POST_ID_INDEX ON BLOG_POSTS (id);
CREATE SEQUENCE BLOG_POST_IMAGES_S START 1 CACHE 1;

CREATE TABLE BLOG_POST_CODE (
  code_id      VARCHAR(100) PRIMARY KEY,
  blog_post_id NUMERIC(6, 0) NOT NULL REFERENCES BLOG_POSTS (id),
  code         TEXT          NOT NULL,
  status       CHAR(1)       NOT NULL
);
CREATE INDEX BLOG_POST_CODE_POST_ID_INDEX ON BLOG_POSTS (id);

CREATE TABLE CMS_BLOG_POST_COMMENTS (
  id                        NUMERIC(6, 0) PRIMARY KEY,
  parent_comment_id         NUMERIC(6, 0) REFERENCES CMS_BLOG_POST_COMMENTS (id),
  blog_post_i18n_content_id NUMERIC(6, 0) NOT NULL REFERENCES BLOG_POST_I18N_CONTENTS (id),
  comment                   TEXT          NOT NULL,
  user_name                 VARCHAR(250)  NOT NULL,
  email                     VARCHAR(300)  NOT NULL,
  status                    CHAR(1)       NOT NULL,
  created                   TIMESTAMP     NOT NULL
);
CREATE INDEX CMS_BLOG_POST_COMMENTS_parent_comment_id_INDEX ON CMS_BLOG_POST_COMMENTS (parent_comment_id);
CREATE INDEX CMS_BLOG_POST_COMMENTS_blog_post_i18n_content_id_INDEX ON CMS_BLOG_POST_COMMENTS (blog_post_i18n_content_id);
CREATE SEQUENCE CMS_BLOG_POST_COMMENTS_S START 1 CACHE 1;
