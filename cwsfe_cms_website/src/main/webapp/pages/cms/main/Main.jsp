<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="Posts"/></h3>

        <h3><spring:message code="NewComments"/></h3>
        <table id="blogPostCommentsList">
            <thead>
            <tr>
                <th>#</th>
                <th scope="col"><spring:message code="Username"/></th>
                <th scope="col"><spring:message code="Comment"/></th>
                <th scope="col"><spring:message code="CreationDate"/></th>
                <th scope="col"><spring:message code="Status"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

    </jsp:body>
</t:genericPage>
