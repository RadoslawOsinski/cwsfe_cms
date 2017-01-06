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
                    <legend><spring:message code="NetAddressAdding"/></legend>
                    <div class="row">
                        <label for="userName"><spring:message code="Username"/></label>
                        <input type="text" id="userName" required/>
                        <small class="invisible"
                               data-bind="attr: { 'class': userNetAddressesModel.userNameIsRequiredStyle}">
                            <spring:message code="UsernameIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="netAddress"><spring:message code="NetAddress"/></label>
                        <input type="text" id="netAddress" required
                               data-bind="value: userNetAddressesModel.netAddress"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': userNetAddressesModel.netAddressIsRequiredStyle}">
                            <spring:message code="NetAddressIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNetAddressButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: userNetAddressesModel.addUserNetAddressFormIsValid"/>
                        <input type="reset" id="resetAddNetAddressButton" value="Reset"
                               class="button small radius alert"/>
                    </div>
                </fieldset>
            </form>
        </div>
    </jsp:body>
</t:genericPage>
