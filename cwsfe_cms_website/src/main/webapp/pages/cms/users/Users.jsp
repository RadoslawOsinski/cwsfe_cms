<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="Users"/></h3>
        <table id="usersList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Username"/></th>
                <th scope="col"><spring:message code="Status"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="UsersAdding"/></h3>

        <form>
            <div class="row">
                <label for="userName"><spring:message code="Username"/></label>
                <input type="text" id="userName"/>
            </div>
            <div class="row">
                <label for="passwordHash"><spring:message code="Password"/></label>
                <input type="password" id="passwordHash"/>
            </div>
            <div class="row">
                <input type="button" id="addUserButton" class="button small radius"
                       value="<spring:message code="Add"/>">
                <input type="reset" value="Reset" class="button small radius alert">
            </div>
        </form>

    </jsp:body>
</t:genericPage>
