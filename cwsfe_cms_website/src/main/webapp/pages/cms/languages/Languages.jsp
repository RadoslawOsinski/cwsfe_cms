<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
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
        </div>

        <div class="row">
            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="LanguagesAdding"/></legend>
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
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>