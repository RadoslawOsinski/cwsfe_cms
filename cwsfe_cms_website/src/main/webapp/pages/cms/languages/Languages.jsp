<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="Languages"/></h3>
        <table id="LanguagesList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Code"/></th>
                <th scope="col"><spring:message code="Name"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="LanguagesAdding"/></h3>

        <form>
            <div class="row">
                <label for="code"><spring:message code="Code"/></label>
                <input type="text" id="code"/>
            </div>
            <div class="row">
                <label for="name"><spring:message code="Name"/></label>
                <input type="text" id="name"/>
            </div>
            <div class="row">
                <input type="button" id="addLanguageButton" class="button small radius"
                       value="<spring:message code="Add"/>">
                <input type="reset" value="Reset" class="button small radius alert">
            </div>
        </form>

    </jsp:body>
</t:genericPage>