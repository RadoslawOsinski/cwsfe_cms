<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <h3><spring:message code="Authors"/></h3>
            <table id="authorsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="LastName"/></th>
                    <th scope="col"><spring:message code="FirstName"/></th>
                    <th scope="col"><spring:message code="GooglePlusAuthorLink"/></th>
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
                    <legend><spring:message code="AuthorsAdding"/></legend>
                    <div class="row">
                        <label for="firstName"><spring:message code="FirstName"/></label>
                        <input type="text" id="firstName"/>
                    </div>
                    <div class="row">
                        <label for="lastName"><spring:message code="LastName"/></label>
                        <input type="text" id="lastName"/>
                    </div>
                    <div class="row">
                        <label for="googlePlusAuthorLink"><spring:message code="GooglePlusAuthorLink"/></label>
                        <input type="text" id="googlePlusAuthorLink"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addAuthorButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>