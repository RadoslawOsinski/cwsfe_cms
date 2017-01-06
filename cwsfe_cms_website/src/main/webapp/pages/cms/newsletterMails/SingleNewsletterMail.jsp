<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>
        <div class="row">

            <div data-alert data-bind="visible: updateNewsletterMailAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: updateNewsletterMailAlerts.errorMessages, beforeRemove: updateNewsletterMailAlerts.hideMessageAnimation, afterAdd: updateNewsletterMailAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: updateNewsletterMailAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: updateNewsletterMailAlerts.warningMessages, beforeRemove: updateNewsletterMailAlerts.hideMessageAnimation, afterAdd: updateNewsletterMailAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: updateNewsletterMailAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: updateNewsletterMailAlerts.infoMessages, beforeRemove: updateNewsletterMailAlerts.hideMessageAnimation, afterAdd: updateNewsletterMailAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <spring:url value="/newsletterMails/updateNewsletterMail" var="updateNewsletterMailUrl"
                        htmlEscape="true"/>
            <form method="post" action="${updateNewsletterMailUrl}" autocomplete="off">
                <fieldset>
                    <legend><spring:message code="NewsletterMailEdit"/></legend>
                    <input type="hidden" name="id" id="newsletterMailId" value="${newsletterMail.id}"/>

                    <div class="row">
                        <label for="recipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="hidden" id="recipientGroupId" name="recipientGroupId"
                               value="${newsletterMail.recipientGroupId}"/>
                        <input type="text" id="recipientGroup"
                               value="${newsletterMailGroupName}" required
                               data-bind="textInput: singleNewsletterMailViewModel.newsletterMailGroupName"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailViewModel.newsletterMailGroupNameIsRequiredStyle}">
                            <spring:message code="NewsletterMailGroupMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterName"><spring:message code="Name"/></label>
                        <input type="text" id="newsletterName" name="name"
                               maxlength="100"
                               value="${newsletterMail.name}" required
                               data-bind="textInput: singleNewsletterMailViewModel.name"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailViewModel.nameIsRequiredStyle}">
                            <spring:message code="NewsletterMailNameMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterSubject"><spring:message code="Subject"/></label>
                        <input type="text" id="newsletterSubject" name="subject"
                               maxlength="100"
                               value="${newsletterMail.subject}" required
                               data-bind="textInput: singleNewsletterMailViewModel.subject"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailViewModel.subjectIsRequiredStyle}">
                            <spring:message code="SubjectMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterContent"><spring:message code="Content"/></label>
                        <textarea id="newsletterContent" name="mailContent"
                                  cols="30"
                                  rows="15">${newsletterMail.mailContent}</textarea>
                    </div>
                    <div class="row">
                        <input type="submit" name="requestHandler" value="<spring:message code="Save"/>"
                               class="button small radius primary"
                               data-bind="enable: singleNewsletterMailViewModel.updateNewsletterMailFormIsValid"/>
                        <input type="button" id="confirmSendButton" value="<spring:message code="Send"/>"
                               class="button small radius secondary"/>
                        <input type="reset" id="resetUpdateNewsletterMail" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">

            <div data-alert data-bind="visible: newsletterTestSendAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: newsletterTestSendAlerts.errorMessages, beforeRemove: newsletterTestSendAlerts.hideMessageAnimation, afterAdd: newsletterTestSendAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newsletterTestSendAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: newsletterTestSendAlerts.warningMessages, beforeRemove: newsletterTestSendAlerts.hideMessageAnimation, afterAdd: newsletterTestSendAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newsletterTestSendAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: newsletterTestSendAlerts.infoMessages, beforeRemove: newsletterTestSendAlerts.hideMessageAnimation, afterAdd: newsletterTestSendAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form id="newsletterTestSendForm">
                <fieldset>
                    <legend><spring:message code="NewsletterTestSend"/></legend>
                    <div class="row">
                        <label for="testEmail"><spring:message code="Email"/></label>
                        <input type="email" id="testEmail" maxlength="350" required
                               data-bind="textInput: singleNewsletterMailViewModel.testEmail"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': singleNewsletterMailViewModel.testEmailIsRequiredStyle}">
                            <spring:message code="EmailIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="newsletterTestSendButton" value="<spring:message code="TestSend"/>"
                               class="button small radius primary"
                               data-bind="enable: singleNewsletterMailViewModel.newsletterTestSendFormIsValid">
                        <input type="reset" id="resetNewsletterTestSend" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div id="confirmSendNewsletterModal" class="reveal-modal" data-reveal>
            <p><spring:message code="DoYouReallyWantToSendNewsletter"/></p>
            <input type="button" id="confirmSendNewsletterButton" value="<spring:message code="Send"/>"
                   class="button small radius primary">
            <input type="button" id="cancelSendNewsletterButton" value="<spring:message code="Cancel"/>"
                   class="button small radius alert">
            <a class="close-reveal-modal">&#215;</a>
        </div>

    </jsp:body>
</t:genericPage>
