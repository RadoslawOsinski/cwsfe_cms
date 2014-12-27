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

        <h2><spring:message code="BlogPostEdit"/></h2>

        <div class="tabs-content">

            <section aria-hidden="false" class="content active" id="tabBasicInfo">

                <div id="basicInfoFormValidation" class="alert-small">
                    <span class="close"></span>
                </div>
                <form id="newsForm">
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
                        <input type="text" id="postTextCode" value="${blogPost.postTextCode}"/>
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
                        <input type="text" id="postCreationDate" maxlength="16" value="${blogPost.postCreationDate}"
                               disabled/>
                    </div>
                    <div class="row">
                        <input type="button" id="saveBlogPostButton" class="button small radius"
                               value="<spring:message code="Save"/>">
                        <input type="reset" value="Revert" class="button small radius alert"/>
                    </div>
                </form>
            </section>

            <section aria-hidden="true" class="content" id="tabI18nContent">
                <h2><spring:message code="Text"/></h2>

                <spring:url value="updateBlogPostI18nContent" var="updateBlogPostI18nContentUrl" htmlEscape="true"/>
                <form method="post" action="${updateBlogPostI18nContentUrl}" autocomplete="off">

                    <input type="hidden" name="postId" value="${blogPost.id}">

                    <div class="row">
                        <label for="i18nLanguage"><spring:message code="Language"/></label>
                        <input type="hidden" name="languageId" data-bind="value: languageId"/>
                        <input type="text" id="i18nLanguage"/>
                    </div>
                    <div data-bind="visible: isPostI18nVisible, with: i18nData">
                        <div class="row">
                            <label for="postTitle"><spring:message code="Title"/></label>
                            <input type="text" id="postTitle"
                                   name="postTitle"
                                   data-bind="value: postTitle"
                                   maxlength="100"/>
                        </div>
                        <div class="row">
                            <label for="postShortcut"><spring:message code="Shortcut"/></label>
                            <textarea id="postShortcut" name="postShortcut"
                                      cols="30"
                                      rows="15" data-bind="value: postShortcut"></textarea>
                        </div>
                        <div class="row">
                            <label for="postDescription"><spring:message code="Description"/></label>
                            <textarea id="postDescription" name="postDescription"
                                      cols="30"
                                      rows="15" data-bind="value: postDescription"></textarea>
                        </div>
                        <div class="row">
                            <label for="blogPostI18nContentStatus"><spring:message code="Status"/></label>
                            <select id="blogPostI18nContentStatus" name="status" data-bind="value: status">
                                <option value="H"><spring:message code="Hidden"/></option>
                                <option value="P"><spring:message code="Published"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>
                            <input type="reset" value="Revert" id="revertPostI18nButton"
                                   class="button small radius alert"/>
                        </div>
                    </div>

                </form>

            </section>

                <%--&lt;%&ndash;$ifNotNull($@blogPostI18nContent, {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<table class="table table-striped table-bordered dataTable">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<thead>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th>#</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Username")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Email")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Comment")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Status")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Actions")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</thead>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tbody>&ndash;%&gt;--%>
                <%--&lt;%&ndash;$=(@blogPostComments, (List) $*pl.com.cwsfe.cms.dao.BlogPostCommentsDAO("listForPostI18nContent",&ndash;%&gt;--%>
                <%--&lt;%&ndash;$#{blogPostI18nContent.id}))&ndash;%&gt;--%>
                <%--&lt;%&ndash;$if($util.isEmpty((List) $@blogPostComments), {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td colspan="4"><spring:message code="NoRecords")</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;}, {&ndash;%&gt;--%>
                <%--&lt;%&ndash;$=(@cnt, 1)&ndash;%&gt;--%>
                <%--&lt;%&ndash;$=(@blogPostComment, (pl.com.cwsfe.cms.model.BlogPostComment) null)&ndash;%&gt;--%>
                <%--&lt;%&ndash;$for(@blogPostComment, $@blogPostComments, {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>$@cnt</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>$@blogPostComment.getUserName()</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>$@blogPostComment.getEmail()</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>$@blogPostComment.getComment()</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;$if($==($@blogPostComment.getStatus(), "N"), {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<spring:message code="NewComment")&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>
                <%--&lt;%&ndash;$if($==($@blogPostComment.getStatus(), "P"), {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<spring:message code="PublishedComment")&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>
                <%--&lt;%&ndash;$if($==($@blogPostComment.getStatus(), "B"), {&ndash;%&gt;--%>
                <%--&lt;%&ndash;<spring:message code="BlockedComment")&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>
                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<form method="post" action="$page.url($currentPageCode())">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<input type="hidden" name="blogPost.id" value="$#{blogPost.id}">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<input type="hidden" name="blogPostI18nContent.postId" value="$#{blogPost.id}">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<input type="hidden" name="blogPostI18nContent.languageId" value="$@pLang[0]">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<input type="hidden" name="blogPostComment.id" value="$@blogPostComment.getId()">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<button type="submit" name="requestHandler" value="blogPostCommentPublish" class="btn btn-success">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<spring:message code="PublishComment")&ndash;%&gt;--%>
                <%--&lt;%&ndash;</button>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<button type="submit" name="requestHandler" value="blogPostCommentBlock" class="btn btn-warning">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<spring:message code="BlockComment")&ndash;%&gt;--%>
                <%--&lt;%&ndash;</button>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</form>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;$++(@cnt)&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tbody>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tfoot>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th>#</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Username")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Email")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Comment")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Status")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<th><spring:message code="Actions")</th>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</tfoot>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</table>&ndash;%&gt;--%>
                <%--&lt;%&ndash;})&ndash;%&gt;--%>

                <%--</c:otherwise>--%>
                <%--</c:choose>--%>

                <%--</div>--%>
                <%--</c:forEach>--%>

            <section aria-hidden="false" class="content" id="tabImages">
                <h2><spring:message code="BlogPostImagesManagement"/></h2>
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
                <spring:url value="/blogPosts/addBlogPostImage" var="addBlogPostImageUrl" htmlEscape="true"/>
                <form method="post" action="${addBlogPostImageUrl}" autocomplete="off" enctype="multipart/form-data">
                    <input type="hidden" name="blogPostId" value="${blogPost.id}"/>

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

            <section aria-hidden="false" class="content" id="tabComments">
                <h2><spring:message code="Comments"/></h2>
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

                <spring:url value="/postCategoriesUpdate" var="postCategoriesUpdateUrl" htmlEscape="true"/>

                <form method="post" action="${postCategoriesUpdateUrl}" autocomplete="off">
                    <input type="hidden" name="id" value="${blogPost.id}">

                        <%--@elvariable id="blogKeywords" type="java.util.List<BlogKeyword>"--%>
                        <%--@elvariable id="blogPostSelectedKeywords" type="java.util.List<Long>"--%>
                    <c:forEach var="category" items="${blogKeywords}">

                        <div class="row">
                            <label for="postCategories">${category.keywordName}</label>
                            <input type="checkbox" id="postCategories" name="postCategories" value="${category.id}"<c:if
                                    test="${blogPostSelectedKeywords.contains(category.id)}">
                                checked</c:if>/>
                        </div>

                    </c:forEach>
                    <div class="row">
                        <input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>
                        <input type="reset" class="button small radius alert"/>
                    </div>
                </form>

            </section>

            <section aria-hidden="false" class="content" id="tabCodeFragments">

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

                <h3><spring:message code="BlogPostAdding"/></h3>

                <form autocomplete="off" id="addBlogPostCodeForm">
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
            </section>

        </div>

    </jsp:body>
</t:genericPage>