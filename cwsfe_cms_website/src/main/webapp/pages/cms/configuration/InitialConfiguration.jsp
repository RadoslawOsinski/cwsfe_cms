<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <meta name="description" content="CWSFE CMS- Complete Working Solution For Everyone CMS"/>
    <meta name="author" content="Radosław Osiński">
    <meta name="robots" content="none"/>
    <%--none=noindex, nofollow--%>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>CWSFE CMS</title>

    <link href="${pageContext.request.contextPath}/resources-cwsfe-cms/favicon.png" rel="shortcut icon"
          type="image/x-icon"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/LoginPage-min.css"
          type="text/css"/>

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/jquery-2.0.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/jqueryui/jquery-ui-1.10.3.custom.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/Login.js"></script>

</head>

<body id="index" class="home">
<div id="loading-block"></div>

<section id="login-container">
    <div id="login_header"></div>

    <h3><spring:message code="CmsConfiguration" text="CmsConfiguration"/></h3>
    <spring:url value="/configuration/addAdminUser" var="addAdminUserUrl" htmlEscape="true"/>
    <form class="fixed" method="post" action="${addAdminUserUrl}" autocomplete="off"
          id="addBlogPostImageForm">
        <div class="row">
            <label for="userName"><spring:message code="Username" text="user"/></label>

            <input type="text" name="userName" id="userName"/>
        </div>
        <div class="row">
            <label for="password"><spring:message code="Password" text="Password"/></label>

            <input type="password" name="password" id="password"
                    />
        </div>
        <div class="row">
            <input type="submit" value="<spring:message code="Add" text="Add"/>" class="button small radius"/>
            <input type="reset" value="Reset" class="button small radius alert">
        </div>
    </form>

</section>

</body>
</html>
