<%--@elvariable id="mainJavaScript" type="java.lang.String"--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Generic CMS page template" pageEncoding="UTF-8" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="UTF-8">
    <meta name="description" content="CWSFE CMS"/>
    <meta name="author" content="Radosław Osiński">
    <meta name="robots" content="none"/>
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
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/cms/dataTables.foundation.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/cms/shared.css"
          type="text/css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources-cwsfe-cms/css/cms/colors.css"
          type="text/css"/>

    <script data-main="${mainJavaScript}"
            src="${pageContext.request.contextPath}/resources-cwsfe-cms/js/requirejs/require.js"
            type="application/javascript"></script>
    <script type="application/javascript">
        var $contextPath = '${pageContext.request.contextPath}';
        require.config({
            paths: {
                jquery: $contextPath + '/resources-cwsfe-cms/js/jquery/jquery-2.1.1.min',
                jqueryUi: $contextPath + '/resources-cwsfe-cms/js/jqueryui/jquery-ui.min',
                foundation: $contextPath + '/resources-cwsfe-cms/js/foundation/foundation',
                foundationTabs: $contextPath + '/resources-cwsfe-cms/js/foundation/foundation.tab',
                foundationOffCanvas: $contextPath + '/resources-cwsfe-cms/js/foundation/foundation.offcanvas',
                foundationReveal: $contextPath + '/resources-cwsfe-cms/js/foundation/foundation.reveal',
                knockout: $contextPath + '/resources-cwsfe-cms/js/knockout/knockout-3.3.0',
                cmsLayout: $contextPath + '/resources-cwsfe-cms/js/cms/layout/CmsLayout',
                dataTable: $contextPath + '/resources-cwsfe-cms/js/datatables/jquery.dataTables.min',
                dataTableFoundation: $contextPath + '/resources-cwsfe-cms/js/datatables/dataTables.foundation',
                formAlerts: $contextPath + '/resources-cwsfe-cms/js/cms/layout/FormAlerts'
            },
            shim: {
                'jqueryUi': ['jquery'],
                'dataTable': ['jquery'],
                'foundation': ['jquery'],
                'foundationTabs': ['foundation'],
                'foundationOffCanvas': ['foundation'],
                'foundationReveal': ['foundation'],
                'dataTableFoundation': ['dataTable']
            }
        });
    </script>

</head>
<body>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"/>
<input type="hidden" id="currentPageUrl" value="${pageContext.request.requestURL}"/>
<input type="hidden" id="currentPageUri" value="${pageContext.request.requestURI}"/>

<header>
    <nav class="top-bar hide-for-small">
        <ul class="title-area">
            <li class="name">
                <spring:url value="/Main" var="mainUrl" htmlEscape="true"/>
                <%--<figure id="logo"><a href="${mainUrl}" class="logo" tabindex="-1"></a></figure>--%>
                <h1><img src="${pageContext.request.contextPath}/resources-cwsfe-cms/CWSFE_logo_40x39.png"
                         alt="CWSFE logo"/><span>CWSFE CMS</span>
                </h1>
            </li>
        </ul>

        <section class="top-bar-section">
            <ul class="right">
                <li class="divider"></li>
                <li>
                    <a href="#" class="">Messages</a>
                </li>
                <li class="divider"></li>
                <%--<li class="has-dropdown not-click">--%>
                <%--<a href="http://foundation.zurb.com/learn/features.html" class="">Settings</a>--%>
                <%--<ul class="dropdown">--%>
                <%--<li class="title back js-generated"><h5><a href="#">« Back</a></h5></li>--%>
                <%--<li><a class="parent-link js-generated" href="http://foundation.zurb.com/learn/features.html">Learn</a></li>--%>
                <%--<li><a href="http://foundation.zurb.com/learn/about.html">About</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<a href="http://foundation.zurb.com/business/services.html" class="">Messages</a>--%>
                <%--</li>--%>
                <spring:url value="/logout" var="logoutUrl" htmlEscape="true"/>
                <li class="download"><a href="${logoutUrl}" class="small blue nice button src-download"
                                        tabindex="-1"><spring:message code="Logout"/></a></li>
                <li class="has-dropdown username not-click">
                </li>
            </ul>
        </section>
    </nav>
</header>

<div class="large-12">
    <div class="large-2 columns background-nav">
        <spring:url value="/folders" var="foldersUrl" htmlEscape="true"/>
        <spring:url value="/news" var="newsUrl" htmlEscape="true"/>
        <spring:url value="/newsTypes" var="newsTypesUrl" htmlEscape="true"/>
        <spring:url value="/languages" var="languagesUrl" htmlEscape="true"/>
        <spring:url value="/cmsTextI18n" var="cmsTextI18nUrl" htmlEscape="true"/>
        <spring:url value="/cmsTextI18nCategories" var="cmsTextI18nCategoriesUrl" htmlEscape="true"/>
        <spring:url value="/authors" var="authorsUrl" htmlEscape="true"/>
        <spring:url value="/users" var="usersUrl" htmlEscape="true"/>
        <spring:url value="/usersNetAddresses" var="usersNetAddressesUrl" htmlEscape="true"/>
        <spring:url value="/roles" var="rolesUrl" htmlEscape="true"/>
        <spring:url value="/monitoring/generalInformation" var="monitoringGeneralInformationUrl"
                    htmlEscape="true"/>

        <ul class="side-nav" role=menubar>
            <li class="withSubMenu"><a id="menuNewsSubMenu" href="#" tabindex="-1"><spring:message code="News"/></a>
                <ul class="subMenu">
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/news/')}"> class="active"</c:if>><a
                        href="${newsUrl}" tabindex="-1"><spring:message code="CmsNewsManagement"/></a></li>
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/newsTypes/NewsTypes')}"> class="active"</c:if>>
                        <a id="menuNewsTypesLink" href="${newsTypesUrl}" tabindex="-1"><spring:message
                            code="NewsTypes"/></a></li>
                    <li role="menuitem"<c:if
                        test="${pageContext.request.requestURI.contains('cms/folders/Folders')}"> class="active"</c:if>>
                        <a id="menuFoldersLink" href="${foldersUrl}" tabindex="-1"><spring:message code="Folders"/></a>
                    </li>
                </ul>
            </li>
            <li role="menuitem" <c:if
                test="${pageContext.request.requestURI.contains('cms/languages/Languages')}"> class="active"</c:if>>
                <a id="menuLanguagesLink" href="${languagesUrl}" tabindex="-1"><spring:message code="Languages"/></a>
            </li>
            <li class="withSubMenu"><a id="menuTransaltionsSubMenu" href="#" tabindex="-1"><spring:message
                code="Translations"/></a>
                <ul class="subMenu">
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/textI18n/TextI18n')}"> class="active"</c:if>>
                        <a id="menuTextTranslationsLink"
                           href="${cmsTextI18nUrl}" tabindex="-1"><spring:message
                            code="TextTranslations"/></a>
                    </li>
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/textI18nCategories/TextI18nCategories')}"> class="active"</c:if>>
                        <a id="menuTranslationCategoriesLink" href="${cmsTextI18nCategoriesUrl}"
                           tabindex="-1"><spring:message
                            code="TranslationCategories"/></a>
                    </li>
                </ul>
            </li>
            <li role="menuitem" <c:if
                test="${pageContext.request.requestURI.contains('cms/authors/Authors')}"> class="active"</c:if>>
                <a id="menuAuthorsLink"
                   href="${authorsUrl}" tabindex="-1"><spring:message code="Authors"/></a></li>
            <li class="withSubMenu"><a id="menuUsersSubMenu" href="#" tabindex="-1"><spring:message code="Users"/></a>
                <ul class="subMenu">
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/users/Users')}"> class="active"</c:if>>
                        <a id="menuUsersLink" href="${usersUrl}" tabindex="-1"><spring:message code="Users"/></a></li>
                    <li role="menuitem" <c:if
                        test="${pageContext.request.requestURI.contains('cms/users/UsersNetAddresses')}"> class="active"</c:if>>
                        <a id="menuUsersNetAddressesLink" href="${usersNetAddressesUrl}" tabindex="-1"><spring:message
                            code="UsersNetAddresses"/></a>
                    </li>
                </ul>
            </li>
            <li role="menuitem" <c:if
                test="${pageContext.request.requestURI.contains('cms/roles/Roles')}"> class="active"</c:if>>
                <a id="menuRolesLink"
                   href="${rolesUrl}" tabindex="-1"><spring:message code="Roles"/></a></li>
            <li role="menuitem" <c:if
                test="${pageContext.request.requestURI.contains('cms/monitoring/GeneralInformation')}"> class="active"</c:if>>
                <a id="menuMonitoringLink" href="${monitoringGeneralInformationUrl}" tabindex="-1"><spring:message
                    code="Monitoring"/></a></li>
        </ul>
    </div>

    <div class="large-10 columns">
        <div class="row">
            <ul class="breadcrumbs">
                <li><a href="${mainUrl}">Home</a></li>
                <%--@elvariable id="breadcrumbs" type="java.util.List<Breadcrumb>"--%>
                <c:forEach var="breadcrumb" items="${breadcrumbs}">
                    <li><a href="${breadcrumb.url}" tabindex="-1">${breadcrumb.text}</a></li>
                </c:forEach>
                <%--<li class="unavailable"><a href="#">Gene Splicing</a></li>--%>
                <%--<li class="current"><a href="#">Cloning</a></li>--%>
            </ul>
        </div>
        <jsp:doBody/>
    </div>

</div>

<footer id="pageFooter" class="large-12 columns wrap-footer">
    <div class="row">
        <spring:message code="PageLoadedIn"/>&nbsp;<span id="loadTimeValue"></span>&nbsp;ms
        <p>&copy; Powered by <a href="http://cwsfe.eu" target="_blank" tabindex="-1">CWSFE</a>.</p>
    </div>
</footer>

</body>
</html>
