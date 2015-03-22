<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <form>
                <fieldset>
                    <legend><spring:message code="Search"/></legend>
                    <div class="row">
                        <label for="searchName"><spring:message code="Name"/></label>
                        <input type="text" id="searchName"/>
                    </div>
                    <div class="row">
                        <label for="searchRecipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="hidden" id="searchRecipientGroupId" value=""/>
                        <input type="text" id="searchRecipientGroup"/>
                    </div>
                    <div class="row">
                        <input type="submit" value="Submit" id="searchNewsletterMailButton"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="NewsletterMailsManagement"/></h3>
            <table id="newsletterMailsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="RecipientGroup"/></th>
                    <th scope="col"><spring:message code="Name"/></th>
                    <th scope="col"><spring:message code="Subject"/></th>
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

            <form id="addNewNewsletterMailForm">
                <fieldset>
                    <legend><spring:message code="NewsletterMailAdding"/></legend>
                    <div class="row">
                        <label for="recipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="text" id="recipientGroup" required data-bind="textInput: newsletterMailsViewModel.recipientGroup"/>
                        <small class="invisible" data-bind="attr: { 'class': newsletterMailsViewModel.recipientGroupIsRequiredStyle}">
                            <spring:message code="RecipientGroupMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterMailName"><spring:message code="Name"/></label>
                        <input type="text" id="newsletterMailName" maxlength="100" required data-bind="textInput: newsletterMailsViewModel.newsletterMailName"/>
                        <small class="invisible" data-bind="attr: { 'class': newsletterMailsViewModel.newsletterMailNameIsRequiredStyle}">
                            <spring:message code="NewsletterMailGroupMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterMailSubject"><spring:message code="Subject"/></label>
                        <input type="text" id="newsletterMailSubject" maxlength="100" required data-bind="textInput: newsletterMailsViewModel.newsletterMailSubject"/>
                        <small class="invisible" data-bind="attr: { 'class': newsletterMailsViewModel.newsletterMailSubjectIsRequiredStyle}">
                            <spring:message code="SubjectMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsletterMailButton" class="button small radius primary"
                               value="<spring:message code="Add"/>" data-bind="enable: newsletterMailsViewModel.addNewsletterMailFormIsValid">
                        <input type="reset" id="resetAddNewsletterMail" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
