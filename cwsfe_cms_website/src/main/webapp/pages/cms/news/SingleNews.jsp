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
        </ul>

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

                <form id="newsForm">
                    <input type="hidden" id="cmsNewsId" name="cmsNews.id" value="${cmsNews.id}"/>

                    <div class="row">
                        <label for="author"><spring:message code="Author"/></label>
                        <input type="hidden" id="authorId" value="${cmsNews.authorId}"/>
                        <input type="text" id="author"
                               value="${cmsAuthor.lastName} ${cmsAuthor.firstName}"
                               disabled/>
                    </div>
                    <div class="row">
                        <label for="newsType"><spring:message code="NewsType"/></label>
                        <input type="hidden" id="newsTypeId" value="${cmsNews.newsTypeId}"/>
                        <input type="text" id="newsType" value="${newsType.type}" data-bind="textInput: newsType"/>
                        <small class="invisible" data-bind="attr: { 'class': newsTypeIsRequiredStyle}">
                            <spring:message code="NewsTypeMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsFolder"><spring:message code="NewsFolder"/></label>
                        <input type="hidden" id="newsFolderId" value="${cmsNews.newsFolderId}"/>
                        <input type="text" id="newsFolder" value="${newsFolder.folderName}"
                               data-bind="textInput: newsFolder"/>
                        <small class="invisible" data-bind="attr: { 'class': newsFolderIsRequiredStyle}">
                            <spring:message code="FolderMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="newsCode"><spring:message code="NewsCode"/></label>
                        <input type="text" id="newsCode" maxlength="100" value="${cmsNews.newsCode}"
                               data-bind="textInput: newsCode"/>
                        <small class="invisible" data-bind="attr: { 'class': newsCodeIsRequiredStyle}">
                            <spring:message code="NewsCodeMustBeSet"/>
                        </small>
                    </div>
                    <div class="row">
                        <label for="status"><spring:message code="Status"/></label>
                        <select id="status">
                            <option value="HIDDEN" <c:if test="${cmsNews.status.equals('HIDDEN')}"> selected</c:if>>
                                <spring:message code="Hidden"/>
                            </option>
                            <option value="PUBLISHED" <c:if
                                test="${cmsNews.status.equals('PUBLISHED')}"> selected</c:if>>
                                <spring:message code="Published"/>
                            </option>
                        </select>
                    </div>
                    <div class="row">
                        <label for="creationDate"><spring:message code="CreationDate"/></label>
                        <input type="text" id="creationDate" maxlength="100" value="${cmsNews.creationDate}" disabled/>
                    </div>
                    <div class="row">
                        <input type="button" id="saveNewsButton" class="button small radius primary"
                               value="<spring:message code="Save"/>" data-bind="enable: saveBasicInfoFormIsValid">
                        <input type="reset" id="resetSaveBasicInfoButton" value="Revert"
                               class="button small radius alert"/>
                    </div>
                </form>
            </section>

            <section aria-hidden="true" class="content" id="tabI18nContent">
                <h2><spring:message code="Text"/></h2>

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
                        <input type="hidden" name="languageId" data-bind="value: languageId"/>
                        <input type="text" id="i18nLanguage" required/>
                        <small class="invisible" data-bind="attr: { 'class': i18nLanguageIsRequiredStyle}">
                            <spring:message code="LanguageMustBeSet"/>
                        </small>
                    </div>

                    <div data-bind="visible: isNewsI18nVisible">
                        <div class="row">
                            <label for="newsTitle"><spring:message code="Title"/></label>
                            <input type="text" id="newsTitle" name="newsTitle"
                                   data-bind="value: newsTitle"
                                   maxlength="100"/>
                            <small class="invisible" data-bind="attr: { 'class': newsTitleIsRequiredStyle}">
                                <spring:message code="TitleMustBeSet"/>
                            </small>
                        </div>
                        <div class="row">
                            <label for="newsShortcut"><spring:message code="Shortcut"/></label>
                            <textarea id="newsShortcut" name="newsShortcut"
                                      cols="30"
                                      rows="15" data-bind="value: newsShortcut"></textarea>
                        </div>
                        <div class="row">
                            <label for="newsDescription"><spring:message code="Description"/></label>
                            <textarea id="newsDescription" name="newsDescription"
                                      cols="30"
                                      rows="15" data-bind="value: newsDescription"></textarea>
                        </div>
                        <div class="row">
                            <label for="i18nContentStatus"><spring:message code="Status"/></label>
                            <select id="i18nContentStatus" name="status" data-bind="value: i18nStatus">
                                <option value="HIDDEN"><spring:message code="Hidden"/></option>
                                <option value="PUBLISHED"><spring:message code="Published"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <input type="button" value="<spring:message code="Save"/>" id="updateNewsI18nButton"
                                   class="button small radius primary" data-bind="enable: saveNewsI18nFormIsValid"/>
                            <input type="reset" value="Revert" id="revertNewsI18nButton"
                                   class="button small radius alert"/>
                        </div>

                    </div>
                </form>

            </section>

            <section aria-hidden="false" class="content" id="tabImages">
                <h2><spring:message code="CmsNewsImagesManagement"/></h2>

                <div class="row">
                    <table id="cmsNewsImagesList">
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
                </div>

                <h3><spring:message code="CmsNewsImagesManagement"/></h3>

                <div class="row">
                    <div data-alert data-bind="visible: imageAlerts.errorMessages().length > 0"
                         class="alert-box radius alert">
                        <ul data-bind='template: { foreach: imageAlerts.errorMessages, beforeRemove: imageAlerts.hideMessageAnimation, afterAdd: imageAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>
                    <div data-alert data-bind="visible: imageAlerts.warningMessages().length > 0"
                         class="alert-box radius warning">
                        <ul data-bind='template: { foreach: imageAlerts.warningMessages, beforeRemove: imageAlerts.hideMessageAnimation, afterAdd: imageAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>
                    <div data-alert data-bind="visible: imageAlerts.infoMessages().length > 0"
                         class="alert-box radius success">
                        <ul data-bind='template: { foreach: imageAlerts.infoMessages, beforeRemove: imageAlerts.hideMessageAnimation, afterAdd: imageAlerts.showMessageAnimation }'>
                            <li data-bind='text: msg'></li>
                        </ul>
                    </div>

                    <spring:url value="addCmsNewsImage" var="addCmsNewsImageUrl" htmlEscape="true"/>
                    <form method="post" action="${addCmsNewsImageUrl}" autocomplete="off" enctype="multipart/form-data">
                        <input type="hidden" name="newsId" value="${cmsNews.id}"/>

                        <div class="row">
                            <label for="title"><spring:message code="Title"/></label>
                            <input type="text" id="title" name="title" maxlength="100" required
                                   data-bind="textInput: imageTitle"/>
                        </div>

                        <div class="row">
                            <label><spring:message code="File"/></label>
                            <input type="file" name="file"/>
                                <%--tood image upload over ajax--%>
                        </div>

                        <div class="row">
                            <input type="submit" value="<spring:message code="Add"/>"
                                   class="button small radius primary" data-bind="enable: addImageFormIsValid"/>
                            <input type="reset" id="resetImagesFormButton" class="button small radius alert"/>
                        </div>
                    </form>
                </div>
            </section>
        </div>

    </jsp:body>
</t:genericPage>
