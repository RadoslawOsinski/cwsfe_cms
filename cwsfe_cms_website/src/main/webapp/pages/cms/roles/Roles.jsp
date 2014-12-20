<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="box">
            <h3><spring:message code="Roles"/></h3>
            <table id="rolesList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="RoleCode"/></th>
                    <th scope="col"><spring:message code="RoleName"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

    </jsp:body>
</t:genericPage>
