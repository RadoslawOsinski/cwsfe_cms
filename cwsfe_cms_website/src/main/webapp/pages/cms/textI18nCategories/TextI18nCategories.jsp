<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <h3><spring:message code="TranslationCategories"/></h3>
            <table id="cmsTextI18nCategoriesList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Category"/></th>
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
                    <legend><spring:message code="TranslationCategoryAdding"/></legend>
                    <div class="row">
                        <label for="category"><spring:message code="Category"/></label>
                        <input type="text" id="category" required data-bind="textInput: categoryViewModel.category"/>
                        <small class="invisible"
                               data-bind="attr: { 'class': categoryViewModel.categoryIsRequiredStyle}">
                            <spring:message code="CategoryIsRequired"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addCmsTextI18nCategoryButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: categoryViewModel.addCategoryFormIsValid">
                        <input type="reset" id="resetAddCategory" value="Reset" class="button small radius alert"/>
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
