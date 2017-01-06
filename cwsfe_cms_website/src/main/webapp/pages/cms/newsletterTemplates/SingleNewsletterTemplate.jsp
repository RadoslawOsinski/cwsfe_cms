<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <div data-alert data-bind="visible: updateNewsletterTemplateAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: updateNewsletterTemplateAlerts.errorMessages, beforeRemove: updateNewsletterTemplateAlerts.hideMessageAnimation, afterAdd: updateNewsletterTemplateAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: updateNewsletterTemplateAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: updateNewsletterTemplateAlerts.warningMessages, beforeRemove: updateNewsletterTemplateAlerts.hideMessageAnimation, afterAdd: updateNewsletterTemplateAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: updateNewsletterTemplateAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: updateNewsletterTemplateAlerts.infoMessages, beforeRemove: updateNewsletterTemplateAlerts.hideMessageAnimation, afterAdd: updateNewsletterTemplateAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form id="editNewsletterTemplateForm" autocomplete="off">
                <fieldset>
                    <legend><spring:message code="NewsletterTemplateEdit"/></legend>
                    <input type="hidden" name="id" id="newsletterTemplateId" value="${newsletterTemplate.id}"/>

                    <div class="row">
                        <label for="language"><spring:message code="Language2LetterCode"/></label>
                        <input type="hidden" id="languageId" name="languageId"
                               value="${newsletterTemplate.languageId}"/>
                        <input type="text" id="language" name="language" value="${newsletterTemplateLanguageCode}"
                               required data-bind="textInput: newsletterTemplateViewModel.language"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newsletterTemplateViewModel.languageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterTemplateName"><spring:message code="Name"/></label>

                        <input type="text" id="newsletterTemplateName" name="name"
                               maxlength="100"
                               value="${newsletterTemplate.name}" required
                               data-bind="textInput: newsletterTemplateViewModel.newsletterTemplateName"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newsletterTemplateViewModel.newsletterTemplateNameIsRequiredStyle}">
                            <spring:message code="NewsletterTemplateNameMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterTemplateSubject"><spring:message code="Subject"/></label>

                        <input type="text" id="newsletterTemplateSubject" name="subject"
                               maxlength="100"
                               value="${newsletterTemplate.subject}" required
                               data-bind="textInput: newsletterTemplateViewModel.newsletterTemplateSubject"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newsletterTemplateViewModel.newsletterTemplateSubjectIsRequiredStyle}">
                            <spring:message code="SubjectMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterTemplateContent"><spring:message code="Content"/></label>

                        <textarea id="newsletterTemplateContent" name="content"
                                  class="huge w-icon medium"
                                  cols="30"
                                  rows="15">${newsletterTemplate.content}</textarea>
                    </div>
                    <div class="row">
                        <input type="button" id="updateNewsletterTemplate" class="button small radius primary"
                               value="<spring:message code="Save"/>"
                               data-bind="enable: newsletterTemplateViewModel.updateNewsletterTemplateFormIsValid"/>
                        <input type="reset" id="resetNewsletterTemplate" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">

            <div data-alert data-bind="visible: newsletterTemplateTestAlerts.errorMessages().length > 0"
                 class="alert-box radius alert">
                <ul data-bind='template: { foreach: newsletterTemplateTestAlerts.errorMessages, beforeRemove: newsletterTemplateTestAlerts.hideMessageAnimation, afterAdd: newsletterTemplateTestAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newsletterTemplateTestAlerts.warningMessages().length > 0"
                 class="alert-box radius warning">
                <ul data-bind='template: { foreach: newsletterTemplateTestAlerts.warningMessages, beforeRemove: newsletterTemplateTestAlerts.hideMessageAnimation, afterAdd: newsletterTemplateTestAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>
            <div data-alert data-bind="visible: newsletterTemplateTestAlerts.infoMessages().length > 0"
                 class="alert-box radius success">
                <ul data-bind='template: { foreach: newsletterTemplateTestAlerts.infoMessages, beforeRemove: newsletterTemplateTestAlerts.hideMessageAnimation, afterAdd: newsletterTemplateTestAlerts.showMessageAnimation }'>
                    <li data-bind='text: msg'></li>
                </ul>
            </div>

            <form id="newsletterTemplateTestSendForm">
                <fieldset>
                    <legend><spring:message code="NewsletterTemplateTestSend"/></legend>
                    <div class="row">
                        <label for="testEmail"><spring:message code="Email"/></label>
                        <input type="email" id="testEmail" maxlength="350" required
                               data-bind="textInput: newsletterTemplateViewModel.testEmail"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newsletterTemplateViewModel.testEmailIsRequiredStyle}">
                            <spring:message code="EmailIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="newsletterTemplateTestSendButton"
                               value="<spring:message code="TestSend"/>"
                               class="button small radius secondary"
                               data-bind="enable: newsletterTemplateViewModel.newsletterTemplateTestSendFormIsValid">
                        <input type="reset" id="resetNewsletterTemplateTest" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
