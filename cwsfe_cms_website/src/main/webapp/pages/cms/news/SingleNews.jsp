<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>

        <ul class="tabs" data-tab role="tablist">
            <li class="tab-title active" role="presentational">
                <a href="#tabBasicInfo" role="tab" tabindex="0" aria-selected="true" aria-controls="tabBasicInfo">
                    <spring:message code="BasicInfo"/>
                </a>
            </li>
            <li class="tab-title" role="presentational">
                <a href="#tabImages" role="tab" tabindex="0" aria-selected="false" aria-controls="tabImages">
                    <spring:message code="Images"/>
                </a>
            </li>
        </ul>

        <div class="tabs-content">

            <section role="tabpanel" aria-hidden="false" class="content active" id="tabBasicInfo">
                <h2><spring:message code="CmsNewsAdding"/></h2>


                    <%--<c:forEach var="cmsLang" items="${cmsLanguages}">
                        <li>
                            <a href="#tabI18nContent_${cmsLang.getCode()}"><spring:message
                                    code="Content"/> ${cmsLang.getCode()}</a>
                        </li>
                    </c:forEach>--%>

                <div id="basicInfoFormValidation" class="alert-small">
                    <span class="close"></span>
                </div>
                <form id="newsForm">
                    <input type="hidden" id="cmsNewsId" name="cmsNews.id" value="${cmsNews.id}">

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

                        <input type="text" id="newsFolder"

                               value="${newsFolder.getFolderName()}"/>
                    </div>
                    <div class="row">
                        <label for="newsCode"><spring:message code="NewsCode"/></label>

                        <input type="text" id="newsCode" maxlength="100"
                               value="${cmsNews.newsCode}"/>
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
                        <input type="submit" value="<spring:message code="Save"/>"
                               onclick="saveNews();return false;" class="button small radius">
                        <input type="reset" value="Revert" class="button small radius alert"/>
                    </div>
                </form>
            </section>

                <%--<c:forEach var="cmsLang" items="${cmsLanguages}">--%>
                <%--<div id="tabI18nContent_${cmsLang.getCode()}">--%>
                <%--<p>&nbsp;</p>--%>

                <%--<c:choose>--%>
                <%--<c:when test="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()) == null}">--%>
                <%--<spring:url value="addNewsI18nContent" var="addNewsI18nContentUrl" htmlEscape="true"/>--%>
                <%--<form class="fixed" method="post" action="${addNewsI18nContentUrl}" autocomplete="off">--%>
                <%--<input type="hidden" name="newsId" value="${cmsNews.id}">--%>
                <%--<input type="hidden" name="languageId" value="${cmsLang.id}">--%>
                <%--<button type="submit" name="requestHandler" value="addNewsI18nContent"--%>
                <%--class="button green medium">--%>
                <%--<spring:message code="AddNewsInternationalisation"/>--%>
                <%--</button>--%>
                <%--</form>--%>
                <%--</c:when>--%>
                <%--<c:otherwise>--%>
                <%--&lt;%&ndash;<spring:message code="Image"):&ndash;%&gt;--%>
                <%--&lt;%&ndash;$text.escapeHtml("$*pl.com.cwsfe.cms.news.newsmanagement.CmsNewsImageByNewsIdAndTitle($#{cmsNews.id},&ndash;%&gt;--%>
                <%--&lt;%&ndash;\"imageCode\")")<br>&ndash;%&gt;--%>
                <%--<hr/>--%>

                <%--<spring:url value="updateNewsI18nContent" var="updateNewsI18nContentUrl"--%>
                <%--htmlEscape="true"/>--%>
                <%--<form class="fixed" method="post" action="${updateNewsI18nContentUrl}"--%>
                <%--autocomplete="off">--%>
                <%--<input type="hidden" name="id"--%>
                <%--value="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).id}">--%>
                <%--<input type="hidden" name="newsId"--%>
                <%--value="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).newsId}">--%>
                <%--<input type="hidden" name="languageId"--%>
                <%--value="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).languageId}">--%>

                <%--<div class="row">--%>
                <%--<label for="newsTitle"><spring:message code="Title"/></label>--%>

                <%--<input type="text" id="newsTitle"--%>
                <%--name="newsTitle"--%>

                <%--value="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).newsTitle}"--%>
                <%--maxlength="100"/>--%>
                <%--</div>--%>
                <%--<div class="row">--%>
                <%--<label for="newsShortcut"><spring:message code="Shortcut"/></label>--%>

                <%--<textarea id="newsShortcut" name="newsShortcut"--%>
                <%--cols="30"--%>
                <%--rows="15">${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).newsShortcut}</textarea>--%>
                <%--</div>--%>
                <%--<div class="row">--%>
                <%--<label for="newsDescription"><spring:message--%>
                <%--code="Description"/></label>--%>

                <%--<textarea id="newsDescription" name="newsDescription"--%>
                <%--cols="30"--%>
                <%--rows="15">${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).newsDescription}</textarea>--%>
                <%--</div>--%>
                <%--<div class="row">--%>
                <%--<label for="i18nContentStatus"><spring:message--%>
                <%--code="Status"/></label>--%>

                <%--<select id="i18nContentStatus" name="status"--%>
                <%-->--%>
                <%--<option value="H"--%>
                <%--<c:if--%>
                <%--test="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).status.equals('H')}">--%>
                <%--selected</c:if>>--%>
                <%--<spring:message code="Hidden"/></option>--%>
                <%--<option value="P"--%>
                <%--<c:if--%>
                <%--test="${cmsNews.cmsNewsI18nContents.get(cmsLang.getCode()).status.equals('P')}">--%>
                <%--selected</c:if>>--%>
                <%--<spring:message code="Published"/></option>--%>
                <%--</select>--%>
                <%--</div>--%>
                <%--<div class="row">--%>
                <%--<input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>--%>
                <%--<input type="reset" value="Revert" class="button small radius alert"/>--%>
                <%--</div>--%>
                <%--</form>--%>

                <%--</div>--%>

                <%--</c:otherwise>--%>
                <%--</c:choose>--%>

                <%--</div>--%>
                <%--</c:forEach>--%>

            <section role="tabpanel" aria-hidden="false" class="content" id="tabImages">
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
                <form class="fixed" method="post" action="${addCmsNewsImageUrl}" autocomplete="off"
                      id="addCmsNewsImageForm" enctype="multipart/form-data">
                    <input type="hidden" name="newsId" value="${cmsNews.id}"/>

                    <div class="row">
                        <label for="title"><spring:message code="Title"/></label>

                        <input type="text" id="title" name="title" maxlength="100"/>
                    </div>

                    <div class="row">
                        <label><spring:message code="File"/></label>
                        <input type="file" name="file" class="file-file"/>
                    </div>

                    <div class="row">
                        <input type="submit" value="<spring:message code="Add"/>" class="button small radius"/>
                        <input type="reset" class="button small radius alert"/>
                    </div>
                </form>

            </section>
        </div>

        <ul class="tabs" data-tab role="tablist">
            <li class="tab-title active" role="presentational" ><a href="#panel2-1" role="tab" tabindex="0" aria-selected="true" controls="panel2-1">Tab 1</a></li>
            <li class="tab-title" role="presentational" ><a href="#panel2-2" role="tab" tabindex="0"aria-selected="false" controls="panel2-2">Tab 2</a></li>
            <li class="tab-title" role="presentational"><a href="#panel2-3" role="tab" tabindex="0" aria-selected="false" controls="panel2-3">Tab 3</a></li>
            <li class="tab-title" role="presentational" ><a href="#panel2-4" role="tab" tabindex="0" aria-selected="false" controls="panel2-4">Tab 4</a></li>
        </ul>
        <div class="tabs-content">
            <section role="tabpanel" aria-hidden="false" class="content active" id="panel2-1">
                <h2>First panel content goes here...</h2>
            </section>
            <section role="tabpanel" aria-hidden="true" class="content" id="panel2-2">
                <h2>Second panel content goes here...</h2>
            </section>
            <section role="tabpanel" aria-hidden="true" class="content" id="panel2-3">
                <h2>Third panel content goes here...</h2>
            </section>
            <section role="tabpanel" aria-hidden="true" class="content" id="panel2-4">
                <h2>Fourth panel content goes here...</h2>
            </section>
        </div>

    </jsp:body>
</t:genericPage>
