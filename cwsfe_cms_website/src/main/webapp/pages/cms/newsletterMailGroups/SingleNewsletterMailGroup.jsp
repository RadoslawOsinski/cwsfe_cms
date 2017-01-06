<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">

            <div data-alert data-bind="visible: editMailGroupAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: editMailGroupAlerts.errorMessages, beforeRemove: editMailGroupAlerts.hideMessageAnimation, afterAdd: editMailGroupAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: editMailGroupAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: editMailGroupAlerts.warningMessages, beforeRemove: editMailGroupAlerts.hideMessageAnimation, afterAdd: editMailGroupAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: editMailGroupAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: editMailGroupAlerts.infoMessages, beforeRemove: editMailGroupAlerts.hideMessageAnimation, afterAdd: editMailGroupAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form id="editMailGroupForm">
                <fieldset>
                    <legend><spring:message code="MailGroupEdit"/></legend>
                    <input type="hidden" id="mailGroupId" name="mailGroupId" value="${newsletterMailGroup.id}"/>

                    <div class="row">
                        <label for="newsletterMailGroupName"><spring:message
                            code="NewsletterMailGroupName"/></label>
                        <input type="text" id="newsletterMailGroupName" maxlength="100"
                               value="${newsletterMailGroup.name}" required
                               data-bind="textInput: singleNewsletterMailGroupViewModel.newsletterMailGroupName"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailGroupViewModel.newsletterMailGroupNameIsRequiredStyle}">
                            <spring:message code="NewsletterMailGroupNameMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="language"><spring:message code="Language2LetterCode"/></label>
                        <input type="hidden" id="languageId" value="${newsletterMailGroup.languageId}"/>
                        <input type="text" id="language" value="${newsletterMailGroupLanguageCode}" required
                               data-bind="textInput: singleNewsletterMailGroupViewModel.language"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailGroupViewModel.languageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="saveNewsletterMailGroupButton" class="button small radius primary"
                               value="<spring:message code="Save"/>"
                               data-bind="enable: singleNewsletterMailGroupViewModel.saveNewsletterMailGroupFormIsValid">
                        <input type="reset" id="resetSaveNewsletterMailGroupFormIsValid" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <form>
                <fieldset>
                    <legend><spring:message code="Search"/></legend>
                    <div class="row">
                        <label for="searchMail"><spring:message code="Mail"/></label>
                        <input type="text" id="searchMail"/>
                    </div>
                    <div class="row">
                        <input type="button" value="<spring:message code="Search"/>"
                               id="searchMailInNewsletterMailGroupButton"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="NewsletterMailAddressesManagement"/></h3>
            <table id="newsletterMailAddressesList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Email"/></th>
                    <th scope="col"><spring:message code="Status"/></th>
                    <th scope="col"><spring:message code="Actions"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="row">

            <div data-alert data-bind="visible: newMailAddressAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: newMailAddressAlerts.errorMessages, beforeRemove: newMailAddressAlerts.hideMessageAnimation, afterAdd: newMailAddressAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newMailAddressAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: newMailAddressAlerts.warningMessages, beforeRemove: newMailAddressAlerts.hideMessageAnimation, afterAdd: newMailAddressAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newMailAddressAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: newMailAddressAlerts.infoMessages, beforeRemove: newMailAddressAlerts.hideMessageAnimation, afterAdd: newMailAddressAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form id="addNewMailAddressForm">
                <fieldset>
                    <legend><spring:message code="MailAddressAdding"/></legend>
                    <div class="row">
                        <label for="newsletterMailAddress"><spring:message code="Email"/></label>
                        <input type="email" id="newsletterMailAddress" maxlength="350" required
                               data-bind="textInput: singleNewsletterMailGroupViewModel.newsletterMailAddress"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailGroupViewModel.newsletterMailAddressIsRequiredStyle}">
                            <spring:message code="NewsletterMailAddressMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsletterMailAddressButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: singleNewsletterMailGroupViewModel.addNewsletterMailAddressFormIsValid">
                        <input type="reset" id="resetAddNewsletterMailAddress" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
