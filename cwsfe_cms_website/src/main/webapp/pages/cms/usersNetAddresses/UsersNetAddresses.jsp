<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="UsersNetAddresses"/></h3>
        <table id="netAddresses">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Username"/></th>
                <th scope="col"><spring:message code="NetAddress"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="NetAddressAdding"/></h3>

        <form>
            <div class="row">
                <label for="userName"><spring:message code="Username"/></label>
                <input type="text" id="userName"/>
            </div>
            <div class="row">
                <label for="netAddress"><spring:message code="NetAddress"/></label>
                <input type="text" id="netAddress" data-bind="value: netAddress"/>
            </div>
            <div class="row">
                <input type="button" id="addNetAddressButton" class="button small radius"
                       value="<spring:message code="Add"/>">
                <input type="reset" value="Reset" class="button small radius alert">
            </div>
        </form>

    </jsp:body>
</t:genericPage>
