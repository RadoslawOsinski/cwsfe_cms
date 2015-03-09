<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
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
        </div>

        <div class="row">

            <div data-alert data-bind="visible: formAlerts.errorMessages().length > 0" class="alert-box radius alert">
                <ul data-bind='template: { foreach: formAlerts.errorMessages, beforeRemove: formAlerts.hideMessageAnimation, afterAdd: formAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: formAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: formAlerts.warningMessages, beforeRemove: formAlerts.hideMessageAnimation, afterAdd: formAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: formAlerts.infoMessages().length > 0" class="alert-box radius success">
                <ul data-bind='template: { foreach: formAlerts.infoMessages, beforeRemove: formAlerts.hideMessageAnimation, afterAdd: formAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="UsersAdding"/></legend>
                    <label for="userName"><spring:message code="Username"/></label>
                    <input type="text" id="userName" required data-bind="textInput: usersViewModel.userName"/>
                    <small class="invisible" data-bind="attr: { 'class': usersViewModel.userNameIsRequiredStyle}">
                        <spring:message
                                code="UsernameIsRequired"/></small>
                    <label for="passwordHash"><spring:message code="Password"/></label>
                    <input type="password" id="passwordHash" required
                           data-bind="textInput: usersViewModel.passwordHash"/>
                    <small class="invisible" data-bind="attr: { 'class': usersViewModel.passwordIsRequiredStyle}">
                        <spring:message
                                code="PasswordMustBeSet"/></small>
                    <input type="button" id="addUserButton" class="button small radius primary"
                           value="<spring:message code="Add"/>" data-bind="enable: usersViewModel.addUserFormIsValid">
                    <input type="reset" id="resetAddUser" value="Reset" class="button small radius alert">
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
