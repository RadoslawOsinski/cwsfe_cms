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
                <a href="#rolesTab" role="tab" tabindex="0" aria-selected="false" aria-controls="rolesTab">
                    <spring:message code="Roles"/>
                </a>
            </li>
        </ul>

        <div class="tabs-content">

            <section aria-hidden="false" class="content active" id="tabBasicInfo">
                <h2><spring:message code="SelectedUser"/></h2>

                <div id="basicInfoFormValidation" class="alert-small">
                    <span class="close"></span>
                </div>
                <form id="userForm">
                    <input type="hidden" id="cmsUserId" name="cmsUser.id" value="${cmsUser.id}">

                    <div class="row">
                        <label for="userName"><spring:message code="Username"/></label>
                        <input type="text" id="userName" maxlength="100"
                               value="${cmsUser.userName}"/>
                    </div>
                    <div class="row">
                        <label for="status"><spring:message code="Status"/></label>
                        <select id="status">
                            <option value="NEW"
                                    <c:if test="${cmsUser.status.equals('NEW')}"> selected</c:if>>
                                <spring:message code="UserStatusNew"/></option>
                            <option value="LOCKED"
                                    <c:if test="${cmsUser.status.equals('LOCKED')}"> selected</c:if>>
                                <spring:message code="UserStatusLocked"/></option>
                            <option value="DELETED"
                                    <c:if test="${cmsUser.status.equals('DELETED')}"> selected</c:if>>
                                <spring:message code="UserStatusDeleted"/></option>
                        </select>
                    </div>
                    <div class="row">
                        <input type="button" id="saveUserButton" class="button small radius"
                               value="<spring:message code="Save"/>">
                        <input type="reset" value="Revert" class="button small radius alert"/>
                    </div>
                </form>
            </section>

            <section aria-hidden="false" class="content" id="rolesTab">
                <spring:url value="/userRolesUpdate" var="userRolesUpdateUrl" htmlEscape="true"/>

                <form method="post" action="${userRolesUpdateUrl}" autocomplete="off">
                    <input type="hidden" name="id" value="${cmsUser.id}"/>

                    <%--@elvariable id="cmsRoles" type="java.util.List<CmsRole>"--%>
                    <c:forEach var="role" items="${cmsRoles}">

                        <div class="row">
                            <label class="wide">${role.roleName}</label>

                            <input type="checkbox" name="cmsUserRoles"
                                   value="${role.id}"<c:if
                                    test="${userSelectedRoles.contains(role.id)}"> checked</c:if>/>

                        </div>

                    </c:forEach>
                    <div class="row">
                        <input type="submit" value="<spring:message code="Save"/>" class="button small radius"/>
                        <input type="reset" class="button small radius alert"/>
                    </div>
                </form>
            </section>
        </div>

    </jsp:body>
</t:genericPage>
