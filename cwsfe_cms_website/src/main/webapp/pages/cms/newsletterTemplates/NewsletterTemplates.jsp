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
                        <label for="searchLanguage"><spring:message code="Language2LetterCode"/></label>
                        <input type="hidden" id="searchLanguageId" value=""/>

                        <input type="text" id="searchLanguage"/>
                    </div>
                    <div class="row">
                        <input type="button" value="Submit" id="searchNewsletterTemplateButton"
                               class="button small radius primary">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="NewsletterTemplatesManagement"/></h3>
            <table id="newsletterTemplatesList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Language"/></th>
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

            <form id="addNewNewsletterTemplateForm">
                <fieldset>
                    <legend><spring:message code="NewsletterTemplateAdding"/></legend>
                    <div class="row">
                        <label for="language"><spring:message code="Language2LetterCode"/></label>
                        <input type="text" id="language" required
                               data-bind="textInput: newNewsletterTemplateViewModel.language"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newNewsletterTemplateViewModel.languageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterTemplateName"><spring:message code="Name"/></label>
                        <input type="text" id="newsletterTemplateName" maxlength="100" required
                               data-bind="textInput: newNewsletterTemplateViewModel.newsletterTemplateName"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': newNewsletterTemplateViewModel.newsletterTemplateNameIsRequiredStyle}">
                            <spring:message code="NewsletterTemplateNameMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsletterTemplateButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: newNewsletterTemplateViewModel.newNewsletterTemplateFormIsValid">
                        <input type="reset" id="resetAddNewNewsletterTemplate" value="Reset"
                               class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
