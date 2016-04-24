cwsfe_cms
=========
Just another CMS.
[![Build Status](https://travis-ci.org/RadoslawOsinski/cwsfe_cms.svg?branch=master)](https://travis-ci.org/RadoslawOsinski/cwsfe_cms)
[![Stack Share](http://img.shields.io/badge/tech-stack-0690fa.svg?style=flat)](http://stackshare.io/RadoslawOsinski/cwsfe-cms)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/82efd10afc724499bf1a98bd0ffe4a39)](https://www.codacy.com/app/radoslaw-osinski/cwsfe_cms)

[Changelog](CHANGELOG.md)

Demo page - Under construction
---

---
Source compilation:
- cd cwsfe_cms_website/; npm install; node_modules/gulp/bin/gulp.js release
- gradlew build

Developer guide
---
[Configuration](/cwsfe_cms_website/env_configuration/standalone/configure.txt)

Used technologies - 100% open source
---
- Spring
- Flyway
- Postgres
- JDK 8
- Wildfly 8, Tomcat 8 or Amazon cloud
- Foundation for website theme
- JavaScript: JQuery, Knockout, RequireJs, DataTables
- Tested with selenium, mockito, junit and spring-test
- Sonar for improving code quality.
- Code delivery by jenkins (TeamCity was too expensive)
