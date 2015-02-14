<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <meta name="description" content="CWSFE CMS - Complete Working Solution For Everyone CMS"/>
    <meta name="author" content="Radosław Osiński">
    <meta name="robots" content="none"/>
    <%--none=noindex, nofollow--%>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>CWSFE CMS</title>

    <link href="${pageContext.request.contextPath}/resources-cwsfe-cms/favicon.ico" rel="shortcut icon"
          type="image/x-icon"/>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/foundation/normalize.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/foundation/foundation.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/cms/shared.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/cms/colors.css"
          type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/login/login.css"
          type="text/css"/>

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/jquery/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/foundation/foundation.min.js"></script>
    <script>
        $(document).foundation();
    </script>

</head>
<body>
<section id="passport" class="social-login-disabled">

    <spring:url value="/configuration/addAdminUser" var="addAdminUserUrl" htmlEscape="true"/>
    <form accept-charset="UTF-8" action="${addAdminUserUrl}" method="post" autocomplete="off">
        <div class="passport-left">
            <img src="${pageContext.request.contextPath}/resources-cwsfe-cms/CWSFE_logo.png" alt="CWSFE logo" height="294" width="300"/>
        </div>
        <div class="passport-right">
            <div id="emailLogin">
                <h5 class="hide-for-small"><spring:message code="CmsConfiguration" text="CmsConfiguration"/></h5>
                <input autocapitalize="off" autocorrect="off" name="userName" placeholder="Username" type="text" required/>
                <input id="password" name="password" placeholder="Password" type="password" required/>

                <button type="submit"><spring:message code="Add" text="Add"/> &raquo;</button>

                <a href="${mainSiteUrl}"><spring:message code="BackToMainWebsite" text="BackToMainWebsite"/></a>
            </div>
        </div>
    </form>

</section>

</body>
</html>
