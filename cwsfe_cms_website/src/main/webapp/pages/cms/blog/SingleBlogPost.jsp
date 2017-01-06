<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>

        <ul class="tabs" data-tab role="tablist">
            <li class="tab-title active" role="presentation">
                <a href="#tabBasicInfo" role="tab" tabindex="0" aria-selected="true" aria-controls="tabBasicInfo">
                    <spring:message code="BasicInfo"/>
                </a>
            </li>
            <li class="tab-title" role="presentation">
                <a href="#tabI18nContent" role="tab" tabindex="0" aria-selected="false" aria-controls="tabI18nContent">
                    <spring:message code="Text"/>
                </a>
            </li>
            <li class="tab-title" role="presentation">
                <a href="#tabImages" role="tab" tabindex="0" aria-selected="false" aria-controls="tabImages">
                    <spring:message code="Images"/>
                </a>
            </li>
            <li class="tab-title" role="presentation">
                <a href="#tabComments" role="tab" tabindex="0" aria-selected="false" aria-controls="tabComments">
                    <spring:message code="Comments"/>
                </a>
            </li>
            <li class="tab-title" role="presentation">
                <a href="#tabCategories" role="tab" tabindex="0" aria-selected="false" aria-controls="tabCategories">
                    <spring:message code="Categories"/>
                </a>
            </li>
            <li class="tab-title" role="presentation">
                <a href="#tabCodeFragments" role="tab" tabindex="0" aria-selected="false"
                   aria-controls="tabCodeFragments">
                    <spring:message code="CodeFragments"/>
                </a>
            </li>
        </ul>

        <input type="hidden" id="blogPostId" name="blogPost.id" value="${blogPost.id}">

        <div class="tabs-content">

            <section aria-hidden="false" class="content active" id="tabBasicInfo">

                <div data-alert data-bind="visible: basicInfoAlerts.errorMessages().length > 0"
                     class="alert-box radius alert">
                    <ul data-bind='template: { foreach: basicInfoAlerts.errorMessages, beforeRemove: basicInfoAlerts.hideMessageAnimation, afterAdd: basicInfoAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: basicInfoAlerts.warningMessages().length > 0"
                     class="alert-box radius warning">
                    <ul data-bind='template: { foreach: basicInfoAlerts.warningMessages, beforeRemove: basicInfoAlerts.hideMessageAnimation, afterAdd: basicInfoAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: basicInfoAlerts.infoMessages().length > 0"
                     class="alert-box radius success">
                    <ul data-bind='template: { foreach: basicInfoAlerts.infoMessages, beforeRemove: basicInfoAlerts.hideMessageAnimation, afterAdd: basicInfoAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>

                <form id="newsForm" class="large-5">
                    <fieldset>
                        <legend><spring:message code="BlogPostEdit"/></legend>
                        <div class="row">
                            <label for="author"><spring:message code="Author"/></label>
                            <input type="hidden" id="postAuthorId" value="${blogPost.postAuthorId}"/>
                            <input type="text" id="author"
                                   value="${cmsAuthor.lastName} ${cmsAuthor.firstName}"
                                   disabled/>
                        </div>
                        <div class="row">
                            <label for="postTextCode"><spring:message code="PostTextCode"/></label>
                            <input type="text" id="postTextCode" value="${blogPost.postTextCode}" required
                                   data-bind="textInput: postTextCode"/>
                            <small class="invisible" data-bind="attr: { 'class': postTextCodeIsRequiredStyle}">
                                <spring:message code="PostTextCodeMustBeSet"/>
                            </small>
                        </div>
                        <div class="row">
                            <label for="status"><spring:message code="Status"/></label>
                            <select id="status" required data-bind="textInput: status">
                                <option value="HIDDEN"<c:if
                                    test="${blogPost.status.name().equals('HIDDEN')}"> selected</c:if>>
                                    <spring:message code="Hidden"/></option>
                                <option value="PUBLISHED"<c:if
                                    test="${blogPost.status.name().equals('PUBLISHED')}"> selected</c:if>>
                                    <spring:message code="Published"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <label for="postCreationDate"><spring:message code="CreationDate"/></label>
                            <input type="text" id="postCreationDate" maxlength="16" value="${blogPost.postCreationDate}"
                                   disabled/>
                        </div>
                        <div class="row">
                            <input type="button" id="saveBlogPostButton" class="button small radius primary"
                                   value="<spring:message code="Save"/>" data-bind="enable: editPostFormIsValid">
                            <input type="reset" id="resetBasicInfoForm" value="Revert"
                                   class="button small radius alert"/>
                        </div>
                    </fieldset>
                </form>
            </section>

            <section aria-hidden="true" class="content" id="tabI18nContent">
                <div data-alert data-bind="visible: i18nContentAlerts.errorMessages().length > 0"
                     class="alert-box radius alert">
                    <ul data-bind='template: { foreach: i18nContentAlerts.errorMessages, beforeRemove: i18nContentAlerts.hideMessageAnimation, afterAdd: i18nContentAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: i18nContentAlerts.warningMessages().length > 0"
                     class="alert-box radius warning">
                    <ul data-bind='template: { foreach: i18nContentAlerts.warningMessages, beforeRemove: i18nContentAlerts.hideMessageAnimation, afterAdd: i18nContentAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: i18nContentAlerts.infoMessages().length > 0"
                     class="alert-box radius success">
                    <ul data-bind='template: { foreach: i18nContentAlerts.infoMessages, beforeRemove: i18nContentAlerts.hideMessageAnimation, afterAdd: i18nContentAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>

                <form>

                    <div class="row">
                        <label for="i18nLanguage"><spring:message code="Language"/></label>
                        <input type="text" id="i18nLanguage" required/>
                        <small class="invisible" data-bind="attr: { 'class': i18nLanguageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>
                    <div data-bind="visible: isPostI18nVisible">
                        <div class="row">
                            <label for="postTitle"><spring:message code="Title"/></label>
                            <input type="text" id="postTitle" required data-bind="value: postTitle"
                                   maxlength="100"/>
                            <small class="invisible" data-bind="attr: { 'class': postTitleIsRequiredStyle}">
                                <spring:message code="TitleMustBeSet"/>
                            </small>
                        </div>
                        <div class="row">
                            <label for="postShortcut"><spring:message code="Shortcut"/></label>
                            <textarea id="postShortcut" cols="30" rows="15" data-bind="value: postShortcut"></textarea>
                        </div>
                        <div class="row">
                            <label for="postDescription"><spring:message code="Description"/></label>
                            <textarea id="postDescription" cols="30" rows="15"
                                      data-bind="value: postDescription"></textarea>
                        </div>
                        <div class="row">
                            <label for="blogPostI18nContentStatus"><spring:message code="Status"/></label>
                            <select id="blogPostI18nContentStatus" data-bind="value: i18nDataStatus" required>
                                <option value="HIDDEN"><spring:message code="Hidden"/></option>
                                <option value="PUBLISHED"><spring:message code="Published"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <input type="button" id="updatePostI18nButton" class="button small radius primary"
                                   value="<spring:message code="Save"/>" data-bind="enable: savePostI18nFormIsValid"/>
                            <input type="button" value="Revert" id="revertPostI18nButton"
                                   class="button small radius alert"/>
                        </div>
                    </div>

                </form>

            </section>

            <section aria-hidden="false" class="content" id="tabImages">
                <table id="blogPostImagesList">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col"><spring:message code="Title"/></th>
                        <th scope="col"><spring:message code="Image"/></th>
                        <th scope="col"><spring:message code="Actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>

                <div data-alert data-bind="visible: imagesAlerts.errorMessages().length > 0"
                     class="alert-box radius alert">
                    <ul data-bind='template: { foreach: imagesAlerts.errorMessages, beforeRemove: imagesAlerts.hideMessageAnimation, afterAdd: imagesAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: imagesAlerts.warningMessages().length > 0"
                     class="alert-box radius warning">
                    <ul data-bind='template: { foreach: imagesAlerts.warningMessages, beforeRemove: imagesAlerts.hideMessageAnimation, afterAdd: imagesAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: imagesAlerts.infoMessages().length > 0"
                     class="alert-box radius success">
                    <ul data-bind='template: { foreach: imagesAlerts.infoMessages, beforeRemove: imagesAlerts.hideMessageAnimation, afterAdd: imagesAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>

                <spring:url value="/blogPosts/addBlogPostImage" var="addBlogPostImageUrl" htmlEscape="true"/>
                <form method="post" action="${addBlogPostImageUrl}" autocomplete="off" enctype="multipart/form-data"
                      class="large-5">
                    <input type="hidden" name="blogPostId" value="${blogPost.id}">
                    <fieldset>
                        <legend><spring:message code="BlogPostImagesManagement"/></legend>
                        <div class="row">
                            <label for="title"><spring:message code="Title"/></label>
                            <input type="text" id="title" name="title" maxlength="100" required
                                   data-bind="textInput: imageTitle"/>
                            <small class="invisible" data-bind="attr: { 'class': imageTitleIsRequiredStyle}">
                                <spring:message code="TitleMustBeSet"/>
                            </small>
                        </div>

                        <div class="row">
                            <label><spring:message code="File"/></label>
                            <input type="file" name="file" required/>
                                <%--tood image upload over ajax--%>
                                <%--<small class="invisible" data-bind="attr: { 'class': imageIsRequiredStyle}">--%>
                                <%--<spring:message code="ImageMustBeSet"/>--%>
                                <%--</small>--%>
                        </div>

                        <div class="row">
                            <input type="submit" value="<spring:message code="Add"/>"
                                   class="button small radius primary"
                                   data-bind="enable: addImageFormIsValid"/>
                            <input type="reset" id="resetImagesFormButton" class="button small radius alert"/>
                        </div>
                    </fieldset>
                </form>
            </section>

            <section aria-hidden="false" class="content" id="tabComments">
                <div data-alert data-bind="visible: commentsAlerts.errorMessages().length > 0"
                     class="alert-box radius alert">
                    <ul data-bind='template: { foreach: commentsAlerts.errorMessages, beforeRemove: commentsAlerts.hideMessageAnimation, afterAdd: commentsAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: commentsAlerts.warningMessages().length > 0"
                     class="alert-box radius warning">
                    <ul data-bind='template: { foreach: commentsAlerts.warningMessages, beforeRemove: commentsAlerts.hideMessageAnimation, afterAdd: commentsAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>
                <div data-alert data-bind="visible: commentsAlerts.infoMessages().length > 0"
                     class="alert-box radius success">
                    <ul data-bind='template: { foreach: commentsAlerts.infoMessages, beforeRemove: commentsAlerts.hideMessageAnimation, afterAdd: commentsAlerts.showMessageAnimation }'>
                        <li data-bind='text: msg'></li>
                    </ul>
                </div>

                <table id="blogPostCommentsList">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th scope="col"><spring:message code="Username"/></th>
                        <th scope="col"><spring:message code="Comment"/></th>
                        <th scope="col"><spring:message code="CreationDate"/></th>
                        <th scope="col"><spring:message code="Status"/></th>
                        <th scope="col"><spring:message code="Actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </section>

            <section aria-hidden="false" class="content" id="tabCategories">

                <form class="large-5">
                    <fieldset>
                        <legend><spring:message code="CategoriesEdition"/></legend>
                        <div data-bind='template: { foreach: blogKeywordsAssignment }'>
                            <div class="row">
                                <input type="checkbox"
                                       data-bind="checkedValue: id, checked: assigned, click: $root.changeBlogKeywordAssignment, attr: {id: 'assigned' + id}"/>
                                <label class="right inline"
                                       data-bind="text: keywordName, attr:{for: 'assigned'+id}"></label>
                            </div>
                        </div>
                    </fieldset>
                </form>

            </section>

            <section aria-hidden="false" class="content" id="tabCodeFragments">

                <div class="row">
                    <table id="blogPostCodesList">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col"><spring:message code="CodeId"/></th>
                            <th scope="col"><spring:message code="Code"/></th>
                            <th scope="col"><spring:message code="Actions"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>

                <div class="row">
                    <div data-alert data-bind="visible: blogPostCodeAlerts.errorMessages().length > 0"
                         class="alert-box radius alert">
                        <ul data-bind='template: { foreach: blogPostCodeAlerts.errorMessages, beforeRemove: blogPostCodeAlerts.hideMessageAnimation, afterAdd: blogPostCodeAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>
                    <div data-alert data-bind="visible: blogPostCodeAlerts.warningMessages().length > 0"
                         class="alert-box radius warning">
                        <ul data-bind='template: { foreach: blogPostCodeAlerts.warningMessages, beforeRemove: blogPostCodeAlerts.hideMessageAnimation, afterAdd: blogPostCodeAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>
                    <div data-alert data-bind="visible: blogPostCodeAlerts.infoMessages().length > 0"
                         class="alert-box radius success">
                        <ul data-bind='template: { foreach: blogPostCodeAlerts.infoMessages, beforeRemove: blogPostCodeAlerts.hideMessageAnimation, afterAdd: blogPostCodeAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>

                    <form autocomplete="off" id="addBlogPostCodeForm">
                        <fieldset>
                            <legend><spring:message code="BlogPostAdding"/></legend>

                            <div class="row">
                                <label for="codeId"><spring:message code="CodeId"/></label>
                                <input type="text" id="codeId" name="codeId" maxlength="100" required
                                       data-bind="textInput: codeId"/>
                                <small class="invisible" data-bind="attr: { 'class': codeIdIsRequiredStyle}">
                                    <spring:message code="CodeIdMustBeSet"/>
                                </small>
                            </div>

                            <div class="row">
                                <label for="code"><spring:message code="Code"/></label>
                                <textarea id="code" name="code" cols="30" rows="15"
                                          data-bind="textInput: code"></textarea>
                            </div>

                            <div class="row">
                                <input type="button" id="addBlogPostCodeButton" class="button small radius primary"
                                       value="<spring:message code="Add"/>" data-bind="enable: addCodeFormIsValid">
                                <input type="reset" id="resetAddCodeForm" class="button small radius alert"/>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </section>

        </div>

    </jsp:body>
</t:genericPage>
