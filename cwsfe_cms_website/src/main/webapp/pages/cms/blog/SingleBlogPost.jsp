<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>

        <div class="full-width">

        <div id="tabs">
            <ul>
                <li><a href="#basicInfoTab"><spring:message code="BasicInfo"/></a></li>
                <c:forEach var="cmsLanguage" items="${cmsLanguages}">
                    <li>
                        <a href="#i18nContentTab_${cmsLanguage.getCode()}"><spring:message
                                code="Content"/> ${cmsLanguage.getCode()}</a>
                    </li>
                </c:forEach>
                <li><a href="#imagesTab"><spring:message code="Images"/></a></li>
                <li><a href="#categoriesTab"><spring:message code="Categories"/></a></li>
                <li><a href="#codeFragmentsTab"><spring:message code="CodeFragments"/></a></li>
            </ul>

            <div id="basicInfoTab">
                <p>&nbsp;</p>


                <h3><spring:message code="BlogPostEdit"/></h3>

                <div id="basicInfoFormValidation" class="alert-small">
                    <span class="close"></span>
                </div>
                <form id="blogPostBasicInfoForm">
                    <input type="hidden" id="blogPostId" name="blogPost.id" value="${blogPost.id}">

                    <div class="row">
                        <label for="author"><spring:message code="Author"/></label>
                        <input type="hidden" id="postAuthorId" value="${blogPost.postAuthorId}"/>

                        <input type="text" id="author"
                               value="${cmsAuthor.lastName} ${cmsAuthor.firstName}"
                               disabled/>
                    </div>
                    <div class="row">
                        <label for="postTextCode"><spring:message code="PostTextCode"/></label>

                        <input type="text" id="postTextCode"

                               value="${blogPost.postTextCode}"/>
                    </div>
                    <div class="row">
                        <label for="status"><spring:message code="Status"/></label>

                        <select id="status">
                            <option value="H"<c:if
                                    test="${blogPost.status.equals('H')}"> selected</c:if>>
                                <spring:message code="Hidden"/></option>
                            <option value="P"<c:if
                                    test="${blogPost.status.equals('P')}"> selected</c:if>>
                                <spring:message code="Published"/></option>
                        </select>
                    </div>
                    <div class="row">
                        <label for="postCreationDate"><spring:message code="CreationDate"/></label>

                        <input type="text" id="postCreationDate"
                               maxlength="16"
                               value="<fmt:formatDate value="${blogPost.postCreationDate}"
                        pattern="yyyy-MM-dd HH:mm"/>"
                        disabled/>
                    </div>
                    <div class="row">
                        <input type="submit" value="<spring:message code="Save"/>"
                               onclick="saveBlogPost();return false;" class="button small radius">
                        <input type="reset" value="Revert" class="button small radius alert"/>
                    </div>
                </form>
            </div>

        </div>

        <c:forEach var="cmsLanguage" items="${cmsLanguages}">
            <div id="i18nContentTab_${cmsLanguage.getCode()}">
                <p>&nbsp;</p>

                <c:choose>
                    <c:when test="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()) == null}">
                        <spring:url value="/addBlogPostsI18nContent" var="addBlogPostsI18nContentUrl"
                                    htmlEscape="true"/>
                        <form class="fixed" method="post" action="${addBlogPostsI18nContentUrl}"
                              autocomplete="off">
                            <input type="hidden" name="postId" value="${blogPost.id}">
                            <input type="hidden" name="languageId" value="${cmsLanguage.id}">
                            <button type="submit" name="requestHandler" value="addBlogPostsI18nContent"
                                    class="button green medium">
                                <spring:message code="AddPostInternationalisation"/>
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <%--&lt;%&ndash;<spring:message code="Image"):&ndash;%&gt;--%>
                        <%--&lt;%&ndash;$text.escapeHtml("$*pl.com.cwsfe.cms.news.newsmanagement.CmsNewsImageByNewsIdAndTitle($#{blogPost.id},&ndash;%&gt;--%>
                        <%--&lt;%&ndash;\"imageCode\")")<br>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;<hr/>&ndash;%&gt;--%>

                        <spring:url value="updateBlogPostI18nContent" var="updateBlogPostI18nContentUrl"
                                    htmlEscape="true"/>
                        <form class="fixed" method="post" action="${updateBlogPostI18nContentUrl}"
                              autocomplete="off">
                            <input type="hidden" name="id"
                                   value="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).id}">
                            <input type="hidden" name="postId"
                                   value="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).postId}">
                            <input type="hidden" name="languageId"
                                   value="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).languageId}">

                            <div class="row">
                                <label for="postTitle"><spring:message code="Title"/></label>

                                <input type="text" id="postTitle"
                                       name="postTitle"

                                       value="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).postTitle}"
                                       maxlength="100"/>
                            </div>
                            <div class="row">
                                <label for="postShortcut"><spring:message code="Shortcut"/></label>

                                                        <textarea id="postShortcut" name="postShortcut"
                                                                  cols="30"
                                                                  rows="15">${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).postShortcut}</textarea>
                            </div>
                            <div class="row">
                                <label for="postDescription"><spring:message
                                        code="Description"/></label>

                                                        <textarea id="postDescription" name="postDescription"
                                                                  cols="30"
                                                                  rows="15">${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).postDescription}</textarea>
                            </div>
                            <div class="row">
                                <label for="blogPostI18nContentStatus"><spring:message
                                        code="Status"/></label>

                                <select id="blogPostI18nContentStatus" name="status"
                                        >
                                    <option value="H"
                                            <c:if
                                                    test="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).status.equals('H')}">
                                                selected</c:if>>
                                        <spring:message code="Hidden"/></option>
                                    <option value="P"
                                            <c:if
                                                    test="${blogPost.blogPostI18nContent.get(cmsLanguage.getCode()).status.equals('P')}">
                                                selected</c:if>>
                                        <spring:message code="Published"/></option>
                                </select>
                            </div>

                            <div class="row">
                                <input type="submit" value="<spring:message code="Save"/>"
                                       class="button small radius"/>
                                <input type="reset" value="Revert" class="button small radius alert"/>
                            </div>
                        </form>

                        <%--$ifNotNull($@blogPostI18nContent, {--%>
                        <%--<table class="table table-striped table-bordered dataTable">--%>
                        <%--<thead>--%>
                        <%--<tr>--%>
                        <%--<th>#</th>--%>
                        <%--<th><spring:message code="Username")</th>--%>
                        <%--<th><spring:message code="Email")</th>--%>
                        <%--<th><spring:message code="Comment")</th>--%>
                        <%--<th><spring:message code="Status")</th>--%>
                        <%--<th><spring:message code="Actions")</th>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--$=(@blogPostComments, (List) $*pl.com.cwsfe.cms.dao.BlogPostCommentsDAO("listForPostI18nContent",--%>
                        <%--$#{blogPostI18nContent.id}))--%>
                        <%--$if($util.isEmpty((List) $@blogPostComments), {--%>
                        <%--<tr>--%>
                        <%--<td colspan="4"><spring:message code="NoRecords")</td>--%>
                        <%--</tr>--%>
                        <%--}, {--%>
                        <%--$=(@cnt, 1)--%>
                        <%--$=(@blogPostComment, (pl.com.cwsfe.cms.model.BlogPostComment) null)--%>
                        <%--$for(@blogPostComment, $@blogPostComments, {--%>
                        <%--<tr>--%>
                        <%--<td>$@cnt</td>--%>
                        <%--<td>$@blogPostComment.getUserName()</td>--%>
                        <%--<td>$@blogPostComment.getEmail()</td>--%>
                        <%--<td>$@blogPostComment.getComment()</td>--%>
                        <%--<td>--%>
                        <%--$if($==($@blogPostComment.getStatus(), "N"), {--%>
                        <%--<spring:message code="NewComment")--%>
                        <%--})--%>
                        <%--$if($==($@blogPostComment.getStatus(), "P"), {--%>
                        <%--<spring:message code="PublishedComment")--%>
                        <%--})--%>
                        <%--$if($==($@blogPostComment.getStatus(), "B"), {--%>
                        <%--<spring:message code="BlockedComment")--%>
                        <%--})--%>
                        <%--</td>--%>
                        <%--<td>--%>
                        <%--<form method="post" action="$page.url($currentPageCode())">--%>
                        <%--<input type="hidden" name="blogPost.id" value="$#{blogPost.id}">--%>
                        <%--<input type="hidden" name="blogPostI18nContent.postId" value="$#{blogPost.id}">--%>
                        <%--<input type="hidden" name="blogPostI18nContent.languageId" value="$@pLang[0]">--%>
                        <%--<input type="hidden" name="blogPostComment.id" value="$@blogPostComment.getId()">--%>
                        <%--<button type="submit" name="requestHandler" value="blogPostCommentPublish" class="btn btn-success">--%>
                        <%--<spring:message code="PublishComment")--%>
                        <%--</button>--%>
                        <%--<button type="submit" name="requestHandler" value="blogPostCommentBlock" class="btn btn-warning">--%>
                        <%--<spring:message code="BlockComment")--%>
                        <%--</button>--%>
                        <%--</form>--%>
                        <%--</td>--%>
                        <%--</tr>--%>
                        <%--$++(@cnt)--%>
                        <%--})--%>
                        <%--})--%>
                        <%--</tbody>--%>
                        <%--<tfoot>--%>
                        <%--<tr>--%>
                        <%--<th>#</th>--%>
                        <%--<th><spring:message code="Username")</th>--%>
                        <%--<th><spring:message code="Email")</th>--%>
                        <%--<th><spring:message code="Comment")</th>--%>
                        <%--<th><spring:message code="Status")</th>--%>
                        <%--<th><spring:message code="Actions")</th>--%>
                        <%--</tr>--%>
                        <%--</tfoot>--%>
                        <%--</table>--%>
                        <%--})--%>

                    </c:otherwise>
                </c:choose>

            </div>
        </c:forEach>


        <div id="imagesTab">
            <h3><spring:message code="BlogPostImagesManagement"/></h3>
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

            <h3><spring:message code="BlogPostImagesManagement"/></h3>

            <div id="addBlogPostImageFormValidation" class="alert-small">
                <span class="close"></span>
            </div>
            <spring:url value="/blogPosts/addBlogPostImage" var="addBlogPostImageUrl"
                        htmlEscape="true"/>
            <form class="fixed" method="post" action="${addBlogPostImageUrl}" autocomplete="off"
                  id="addBlogPostImageForm" enctype="multipart/form-data">
                <input type="hidden" name="blogPostId" value="${blogPost.id}"/>

                <div class="row">
                    <label for="title"><spring:message code="Title"/></label>

                    <input type="text" id="title" name="title"

                           maxlength="100"/>
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
        </div>

        <div id="categoriesTab">

            <spring:url value="/postCategoriesUpdate" var="postCategoriesUpdateUrl"
                        htmlEscape="true"/>

            <form class="fixed" method="post" action="${postCategoriesUpdateUrl}" autocomplete="off">
                <input type="hidden" name="id" value="${blogPost.id}">

                <c:forEach var="category" items="${blogKeywords}">

                    <div class="row">
                        <label>${category.keywordName}</label>

                        <input type="checkbox" name="postCategories" value="${category.id}"<c:if
                                test="${blogPostSelectedKeywords.contains(category.id)}">
                            checked</c:if>/>
                    </div>

                </c:forEach>
                <div class="row">
                    <input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>
                    <input type="reset" class="button small radius alert"/>
                </div>
            </form>

        </div>

        <div id="codeFragmentsTab">

            <div class="box">
                <h3><spring:message code="BlogPostCodesManagement"/></h3>
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

            <div class="box">

                <h3><spring:message code="BlogPostAdding"/></h3>

                <form class="fixed" autocomplete="off" id="addBlogPostCodeForm">
                    <input type="hidden" name="blogPostId" value="${blogPost.id}"/>

                    <div class="row">
                        <label for="codeId"><spring:message code="CodeId"/></label>
                        <input type="text" id="codeId" name="codeId" maxlength="100"/>
                    </div>

                    <div class="row">
                        <label for="code"><spring:message code="Code"/></label>
                                            <textarea id="code" name="code"
                                                      cols="30"
                                                      rows="15"></textarea>
                    </div>

                    <div class="row">
                        <input type="button" id="addBlogPostCodeButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" class="button small radius alert"/>
                    </div>
                </form>
            </div>

        </div>

    </jsp:body>
</t:genericPage>