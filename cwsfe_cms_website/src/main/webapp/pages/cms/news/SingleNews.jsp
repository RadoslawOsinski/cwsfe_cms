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
                <h2><spring:message code="CmsNewsAdding"/></h2>

                <div id="basicInfoFormValidation" class="alert-small">
                    <span class="close"></span>
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
                        <input type="text" id="newsType"
                               value="${newsType.type}"/>
                    </div>
                    <div class="row">
                        <label for="newsFolder"><spring:message code="NewsFolder"/></label>
                        <input type="hidden" id="newsFolderId" value="${cmsNews.newsFolderId}"/>
                            <%--@elvariable id="newsFolder" type="eu.com.cwsfe.cms.model.CmsFolder"--%>
                        <input type="text" id="newsFolder" value="${newsFolder.folderName}"/>
                    </div>
                    <div class="row">
                        <label for="newsCode"><spring:message code="NewsCode"/></label>
                        <input type="text" id="newsCode" maxlength="100" value="${cmsNews.newsCode}"/>
                    </div>
                    <div class="row">
                        <label for="status"><spring:message code="Status"/></label>
                        <select id="status">
                            <option value="H"
                                    <c:if test="${cmsNews.status.equals('H')}"> selected</c:if>>
                                <spring:message code="Hidden"/></option>
                            <option value="P"
                                    <c:if test="${cmsNews.status.equals('P')}"> selected</c:if>>
                                <spring:message code="Published"/></option>
                        </select>
                    </div>
                    <div class="row">
                        <label for="creationDate"><spring:message code="CreationDate"/></label>
                        <input type="text" id="creationDate"
                               maxlength="100"
                               value="${cmsNews.creationDate}" disabled/>
                    </div>
                    <div class="row">
                        <input type="button" id="saveNewsButton" class="button small radius"
                               value="<spring:message code="Save"/>">
                        <input type="reset" value="Revert" class="button small radius alert"/>
                    </div>
                </form>
            </section>

            <section aria-hidden="true" class="content" id="tabI18nContent">
                <h2><spring:message code="Text"/></h2>

                <spring:url value="updateNewsI18nContent" var="updateNewsI18nContentUrl" htmlEscape="true"/>
                <form method="post" action="${updateNewsI18nContentUrl}" autocomplete="off">

                    <input type="hidden" name="newsId" value="${cmsNews.id}">

                    <div class="row">
                        <label for="i18nLanguage"><spring:message code="Language"/></label>
                        <input type="hidden" name="languageId" data-bind="value: languageId"/>
                        <input type="text" id="i18nLanguage"/>
                    </div>

                    <div data-bind="visible: isNewsI18nVisible, with: i18nData">
                        <div class="row">
                            <label for="newsTitle"><spring:message code="Title"/></label>
                            <input type="text" id="newsTitle" name="newsTitle"
                                   data-bind="value: newsTitle"
                                   maxlength="100"/>
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
                            <select id="i18nContentStatus" name="status" data-bind="value: status">
                                <option value="H"><spring:message code="Hidden"/></option>
                                <option value="P"><spring:message code="Published"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>
                            <input type="reset" value="Revert" id="revertNewsI18nButton"
                                   class="button small radius alert"/>
                        </div>

                    </div>
                </form>

            </section>

            <section aria-hidden="false" class="content" id="tabImages">
                <h2><spring:message code="CmsNewsImagesManagement"/></h2>
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

                <h3><spring:message code="CmsNewsImagesManagement"/></h3>
                <spring:url value="addCmsNewsImage" var="addCmsNewsImageUrl" htmlEscape="true"/>
                <form method="post" action="${addCmsNewsImageUrl}" autocomplete="off" enctype="multipart/form-data">
                    <input type="hidden" name="newsId" value="${cmsNews.id}"/>

                    <div class="row">
                        <label for="title"><spring:message code="Title"/></label>
                        <input type="text" id="title" name="title" maxlength="100"/>
                    </div>

                    <div class="row">
                        <label><spring:message code="File"/></label>
                        <input type="file" name="file"/>
                    </div>

                    <div class="row">
                        <input type="submit" value="<spring:message code="Add"/>" class="button small radius"/>
                        <input type="reset" class="button small radius alert"/>
                    </div>
                </form>
            </section>
        </div>

    </jsp:body>
</t:genericPage>
