<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="NewsTypes"/></h3>
        <table id="newsTypesList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="NewsType"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="NewsTypesAdding"/></h3>

        <form>
            <div class="row">
                <label for="type"><spring:message code="NewsType"/></label>

                <input type="text" id="type"/>
            </div>
            <div class="row">
                <input type="button" id="addNewsTypeButton" class="button small radius"
                       value="<spring:message code="Add"/>">
                <input type="reset" value="Reset" class="button small radius alert">
            </div>
        </form>

    </jsp:body>
</t:genericPage>