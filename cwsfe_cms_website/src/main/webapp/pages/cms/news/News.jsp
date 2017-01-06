<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="Search"/></legend>
                    <div class="row">
                        <label for="searchAuthor"><spring:message code="Author"/></label>
                        <input type="hidden" id="searchAuthorId"/>
                        <input type="text" id="searchAuthor"/>
                    </div>
                    <div class="row">
                        <label for="searchNewsCode"><spring:message code="NewsCode"/></label>
                        <input type="text" id="searchNewsCode"/>
                    </div>
                    <div class="row">
                        <input type="submit" value="Submit" onclick="searchNews();return false;"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="CmsNewsManagement"/></h3>
            <table id="newsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Author"/></th>
                    <th scope="col"><spring:message code="NewsCode"/></th>
                    <th scope="col"><spring:message code="CreationDate"/></th>
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

            <form id="addNewNewsForm" class="large-5">
                <fieldset>
                    <legend><spring:message code="CmsNewsAdding"/></legend>
                    <div class="row">
                        <label for="author"><spring:message code="Author"/></label>
                        <input type="text" id="author" required data-bind="textInput: newsViewModel.author"/>
                        <small class="invisible" data-bind="attr: { 'class': newsViewModel.authorIsRequiredStyle}">
                            <spring:message code="AuthorMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsType"><spring:message code="NewsType"/></label>
                        <input type="text" id="newsType" value="" required
                               data-bind="textInput: newsViewModel.newsType"/>
                        <small class="invisible" data-bind="attr: { 'class': newsViewModel.newsTypeIsRequiredStyle}">
                            <spring:message code="NewsTypeMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsFolder"><spring:message code="NewsFolder"/></label>
                        <input type="text" id="newsFolder" required data-bind="textInput: newsViewModel.newsFolder"/>
                        <small class="invisible" data-bind="attr: { 'class': newsViewModel.newsFolderIsRequiredStyle}">
                            <spring:message code="FolderMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsCode"><spring:message code="NewsCode"/></label>
                        <input type="text" id="newsCode" maxlength="100" required
                               data-bind="textInput: newsViewModel.newsCode"/>
                        <small class="invisible" data-bind="attr: { 'class': newsViewModel.newsCodeIsRequiredStyle}">
                            <spring:message code="NewsCodeMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsButton" class="button small radius primary"
                               value="<spring:message code="Add"/>"
                               data-bind="enable: newsViewModel.addNewsFormIsValid">
                        <input type="reset" id="resetAddNews" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
