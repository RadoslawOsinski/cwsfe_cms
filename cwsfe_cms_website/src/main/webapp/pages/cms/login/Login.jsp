<%--@elvariable id="mainSiteUrl" type="java.lang.String"--%>
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
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/foundation/foundation.min.css"
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

    <spring:url value="/cwsfe_cms_security_check" var="loginCheckUrl" htmlEscape="true"/>
    <form accept-charset="UTF-8" action="${loginCheckUrl}" method="post">
        <div class="passport-left">
            <h1>CMS</h1>
            <img src="${pageContext.request.contextPath}/resources-cwsfe-cms/CWSFE_logo.png" alt="CWSFE logo">
        </div>
        <div class="passport-right">
            <div id="emailLogin">
                <h5 class="hide-for-small"><spring:message code="LoggingIn"/></h5>
                <input autocapitalize="off" autocorrect="off" name="userName" placeholder="Username" type="text"/>
                <input id="password" name="password" placeholder="Password" type="password"/>

                <%--<label>--%>
                <%--<input checked="checked" id="remember_me" name="remember_me" type="checkbox" value="remember_me"/>--%>
                <%--Remember Me--%>
                <%--</label>--%>

                <button type="submit"><spring:message code="SignIn"/> &raquo;</button>
                <%--<p><a href="#"><spring:message code="ForgotYourPassword"/></a></p>--%>

                <p><spring:message code="TroubleLoggingIn"/>
                    <a href="mailto:info@cwsfe.pl"><spring:message code="ContactSupport"/> &rarr;</a><br/>
                    <a href="${mainSiteUrl}"><spring:message code="BackToMainWebsite" text="BackToMainWebsite"/></a>
                </p>
            </div>
        </div>

    </form>
</section>

</body>
</html>
