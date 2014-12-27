<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>
        <div class="full-width">

            <div id="tabs">
                <ul>
                    <li><a href="#tabBasicInfo"><spring:message code="BasicInfo"/></a></li>
                    <li><a href="#rolesTab"><spring:message code="Roles"/></a></li>
                </ul>

                <div id="tabBasicInfo">

                    <h3><spring:message code="SelectedUser"/></h3>

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
                                <option value="N"
                                        <c:if test="${cmsUser.status.equals('N')}"> selected</c:if>>
                                    <spring:message code="UserStatusNew"/></option>
                                <option value="L"
                                        <c:if test="${cmsUser.status.equals('L')}"> selected</c:if>>
                                    <spring:message code="UserStatusLocked"/></option>
                                <option value="D"
                                        <c:if test="${cmsUser.status.equals('D')}"> selected</c:if>>
                                    <spring:message code="UserStatusDeleted"/></option>
                            </select>
                        </div>
                        <div class="row">
                            <input type="button" id="saveUserButton" class="button small radius"
                                   value="<spring:message code="Save"/>">
                            <input type="reset" value="Revert" class="button small radius alert"/>
                        </div>
                    </form>

                </div>

                <div id="rolesTab">

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

                </div>

            </div>


        </div>

    </jsp:body>
</t:genericPage>
