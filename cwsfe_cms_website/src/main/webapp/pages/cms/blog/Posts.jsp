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
                        <label for="searchPostTextCode"><spring:message code="PostTextCode"/></label>
                        <input type="text" id="searchPostTextCode"/>
                    </div>
                    <div class="row">
                        <input type="submit" value="Submit" onclick="searchBlogPosts();return false;"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="BlogPostManagement"/></h3>
            <table id="blogPostsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Author"/></th>
                    <th scope="col"><spring:message code="PostTextCode"/></th>
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
            
            <form id="addNewBlogPostForm" class="large-5">
                <fieldset>
                    <legend><spring:message code="BlogPostAdding"/></legend>
                    <div class="row">
                        <label for="author"><spring:message code="Author"/></label>
                        <input type="text" id="author" required data-bind="textInput: blogPostsViewModel.author"/>
                        <small class="invisible" data-bind="attr: { 'class': blogPostsViewModel.authorIsRequiredStyle}">
                            <spring:message code="AuthorMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="postTextCode"><spring:message code="PostTextCode"/></label>
                        <input type="text" id="postTextCode" maxlength="100" required data-bind="textInput: blogPostsViewModel.postTextCode"/>
                        <small class="invisible" data-bind="attr: { 'class': blogPostsViewModel.postTextCodeIsRequiredStyle}">
                            <spring:message code="PostTextCodeMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <input type="button" id="addBlogPostButton" class="button small radius primary"
                               value="<spring:message code="Add"/>" data-bind="enable: blogPostsViewModel.addBlogPostFormIsValid">
                        <input type="reset" id="resetAddBlogPost" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>