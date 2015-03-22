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
                        <label for="searchName"><spring:message code="NewsletterMailGroupName"/></label>
                        <input type="text" id="searchName"/>
                    </div>
                    <div class="row">
                        <label for="searchLanguage"><spring:message code="Language2LetterCode"/></label>
                        <input type="hidden" id="searchLanguageId" value=""/>
                        <input type="text" id="searchLanguage"/>
                    </div>
                    <div class="row">
                        <input type="submit" value="Submit" onclick="searchNewsletterMailGroup();return false;"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="NewsletterMailGroupsManagement"/></h3>
            <table id="newsletterMailGroupsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="NewsletterMailGroupName"/></th>
                    <th scope="col"><spring:message code="Language"/></th>
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

            <form id="addNewNewsletterMailGroupForm">
                <fieldset>
                    <legend><spring:message code="NewsletterMailGroupAdding"/></legend>
                    <div class="row">
                        <label for="language"><spring:message code="Language2LetterCode"/></label>
                        <input type="text" id="language" required data-bind="textInput: newsletterMailGroupViewModel.language"/>
                        <small class="invisible" data-bind="attr: { 'class': newsletterMailGroupViewModel.languageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsletterMailGroupName"><spring:message
                                code="NewsletterMailGroupName"/></label>
                        <input type="text" id="newsletterMailGroupName" maxlength="100" required data-bind="textInput: newsletterMailGroupViewModel.newsletterMailGroupName"/>
                        <small class="invisible" data-bind="attr: { 'class': newsletterMailGroupViewModel.newsletterMailGroupNameIsRequiredStyle}">
                            <spring:message code="NewsletterMailGroupMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsletterMailGroupButton" class="button small radius primary"
                               value="<spring:message code="Add"/>" data-bind="enable: newsletterMailGroupViewModel.addNewsletterMailGroupFormIsValid">
                        <input type="reset" id="resetAddNewsletterMailGroup" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>
    </jsp:body>
</t:genericPage>