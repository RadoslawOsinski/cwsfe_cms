<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <h3><spring:message code="Translations"/></h3>
            <table id="cmsTextI18nList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Language"/></th>
                    <th scope="col"><spring:message code="Category"/></th>
                    <th scope="col"><spring:message code="Key"/></th>
                    <th scope="col"><spring:message code="Text"/></th>
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
                    <legend><spring:message code="TranslationAdding"/></legend>
                    <div class="row">
                        <label for="searchLanguage"><spring:message code="Language"/></label>
                        <input type="text" id="searchLanguage" required
                               data-bind="textInput: translationViewModel.searchLanguage"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': translationViewModel.searchLanguageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="searchCategory"><spring:message code="Category"/></label>
                        <input type="text" id="searchCategory" required
                               data-bind="textInput: translationViewModel.searchCategory"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': translationViewModel.searchCategoryIsRequiredStyle}">
                            <spring:message code="CategoryMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="key"><spring:message code="Key"/></label>
                        <input type="text" id="key" required data-bind="textInput: translationViewModel.key"/>
                        <small class="invisible" data-bind="attr: { 'class': translationViewModel.keyIsRequiredStyle}">
                            <spring:message code="KeyMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="text"><spring:message code="Text"/></label>
                        <input type="text" id="text" required data-bind="textInput: translationViewModel.text"/>
                        <small class="invisible" data-bind="attr: { 'class': translationViewModel.textIsRequiredStyle}">
                            <spring:message code="TextI18nMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addCmsTextI18nButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: translationViewModel.addTranslationFormIsValid">
                        <input type="reset" id="resetAddTranslation" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
