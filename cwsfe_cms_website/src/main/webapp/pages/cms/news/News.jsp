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
            <form id="addNewNewsForm" class="large-5">
                <fieldset>
                    <legend><spring:message code="CmsNewsAdding"/></legend>
                    <div class="row">
                        <label for="author"><spring:message code="Author"/></label>
                        <input type="hidden" id="authorId"/>
                        <input type="text" id="author"/>
                    </div>
                    <div class="row">
                        <label for="newsType"><spring:message code="NewsType"/></label>
                        <input type="hidden" id="newsTypeId"/>
                        <input type="text" id="newsType" value=""/>
                    </div>
                    <div class="row">
                        <label for="newsFolder"><spring:message code="NewsFolder"/></label>
                        <input type="hidden" id="newsFolderId"/>
                        <input type="text" id="newsFolder"/>
                    </div>
                    <div class="row">
                        <label for="newsCode"><spring:message code="NewsCode"/></label>
                        <input type="text" id="newsCode" maxlength="100"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
