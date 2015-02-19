<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
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
        </div>

        <div class="row">
            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="NetAddressAdding"/></legend>
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
                </fieldset>
            </form>
        </div>
    </jsp:body>
</t:genericPage>
