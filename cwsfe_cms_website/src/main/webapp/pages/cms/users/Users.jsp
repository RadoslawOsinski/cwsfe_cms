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

        <form>
            <fieldset>
                <legend><spring:message code="UsersAdding"/></legend>
                <div class="large-4">
                    <label for="userName"><spring:message code="Username"/></label>
                    <input type="text" id="userName" required data-bind="textInput: userName"/>
                    <small class="invisible" data-bind="attr: { 'class': userNameIsRequiredStyle}"><spring:message
                            code="UsernameIsRequired"/></small>
                </div>
                <div class="large-4">
                    <label for="passwordHash"><spring:message code="Password"/></label>
                    <input type="password" id="passwordHash" required data-bind="textInput: passwordHash"/>
                    <small class="invisible" data-bind="attr: { 'class': passwordIsRequiredStyle}"><spring:message
                            code="PasswordMustBeSet"/></small>
                </div>
                <div class="large-4">
                    <input type="button" id="addUserButton" class="button small radius primary"
                           value="<spring:message code="Add"/>" data-bind="enable: addUserFormIsValid">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

    </jsp:body>
</t:genericPage>
