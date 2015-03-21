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
                    <legend><spring:message code="AuthorsAdding"/></legend>
                    <div class="row">
                        <label for="firstName"><spring:message code="FirstName"/></label>
                        <input type="text" id="firstName" required data-bind="textInput: authorsViewModel.firstName"/>
                        <small class="invisible" data-bind="attr: { 'class': authorsViewModel.firstNameIsRequiredStyle}">
                            <spring:message code="FirstNameIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="lastName"><spring:message code="LastName"/></label>
                        <input type="text" id="lastName" required data-bind="textInput: authorsViewModel.lastName"/>
                        <small class="invisible" data-bind="attr: { 'class': authorsViewModel.lastNameIsRequiredStyle}">
                            <spring:message code="LastNameIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="googlePlusAuthorLink"><spring:message code="GooglePlusAuthorLink"/></label>
                        <input type="text" id="googlePlusAuthorLink"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addAuthorButton" class="button small radius primary"
                               value="<spring:message code="Add"/>" data-bind="enable: authorsViewModel.addAuthorFormIsValid"/>
                        <input type="reset" id="resetAuthorButton" value="Reset" class="button small radius alert"/>
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>