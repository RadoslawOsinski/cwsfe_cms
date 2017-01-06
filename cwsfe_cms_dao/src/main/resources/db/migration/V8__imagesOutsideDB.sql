alter table BLOG_POST_IMAGES add column last_modified TIMESTAMP NOT NULL DEFAULT now();
alter table CMS_NEWS_IMAGES add column last_modified TIMESTAMP NOT NULL DEFAULT now();
update BLOG_POST_IMAGES set last_modified = created;
update CMS_NEWS_IMAGES set last_modified = created;

INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'CWSFE_CMS_BLOG_IMAGES_PATH', '/opt/cwsfe_cms/blogImages', '/opt/cwsfe_cms/blogImages', 'Path to images for blog'
);

INSERT INTO CMS_GLOBAL_PARAMS (ID, CODE, DEFAULT_VALUE, VALUE, DESCRIPTION) VALUES (
  nextval('CMS_GLOBAL_PARAMS_S'), 'CWSFE_CMS_NEWS_IMAGES_PATH', '/opt/cwsfe_cms/newsImages', '/opt/cwsfe_cms/newsImages', 'Path to images for news'
);

alter table BLOG_POST_IMAGES drop column CONTENT;
alter table CMS_NEWS_IMAGES drop column CONTENT;
