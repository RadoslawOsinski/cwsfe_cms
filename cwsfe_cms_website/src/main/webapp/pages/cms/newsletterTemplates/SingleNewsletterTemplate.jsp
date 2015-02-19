<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:genericPage>
    <jsp:body>

        <div id="newsletterTemplateEditFormValidation" class="alert-small">
            <c:if test="${updateErrors != null}">
                <p>${updateErrors}</p>
            </c:if>
            <c:if test="${updateSuccessfull != null}">
                <p>${updateSuccessfull}</p>
            </c:if>
            <span class="close"></span>
        </div>
        <spring:url value="/newsletterTemplates/updateNewsletterTemplate" var="updateNewsletterTemplateUrl"
                    htmlEscape="true"/>
        <form id="editNewsletterTemplateForm" method="post" action="${updateNewsletterTemplateUrl}"
              autocomplete="off">
            <fieldset>
                <legend><spring:message code="NewsletterTemplateEdit"/></legend>
                <input type="hidden" name="id" id="newsletterTemplateId" value="${newsletterTemplate.id}"/>

                <div class="row">
                    <label for="language"><spring:message code="Language2LetterCode"/></label>
                    <input type="hidden" id="languageId" name="languageId"
                           value="${newsletterTemplate.languageId}"/>

                    <input type="text" id="language" name="language" value="${newsletterTemplateLanguageCode}"/>
                </div>
                <div class="row">
                    <label for="newsletterTemplateName"><spring:message code="Name"/></label>

                    <input type="text" id="newsletterTemplateName" name="name"
                           maxlength="100"
                           value="${newsletterTemplate.name}"/>
                </div>
                <div class="row">
                    <label for="newsletterTemplateSubject"><spring:message code="Subject"/></label>

                    <input type="text" id="newsletterTemplateSubject" name="subject"
                           maxlength="100"
                           value="${newsletterTemplate.subject}"/>
                </div>
                <div class="row">
                    <label for="newsletterTemplateContent"><spring:message code="Content"/></label>

                                <textarea id="newsletterTemplateContent" name="content"
                                          class="huge w-icon medium"
                                          cols="30"
                                          rows="15">${newsletterTemplate.content}</textarea>
                </div>
                <div class="row">
                    <input type="submit" name="requestHandler" value="<spring:message code="Save"/>"
                           class="button small radius"/>
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

        <div id="newsletterTemplateTestSendFormValidation" class="alert-small">
            <span class="close"></span>
        </div>
        <form id="newsletterTemplateTestSendForm">
            <fieldset>
                <legend><spring:message code="NewsletterTemplateTestSend"/></legend>
                <div class="row">
                    <label for="testEmail"><spring:message code="Email"/></label>
                    <input type="email" id="testEmail" maxlength="350"/>
                </div>
                <div class="row">
                    <input type="submit" value="<spring:message code="TestSend"/>"
                           onclick="newsletterTemplateTestSend();return false;" class="button small radius">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

    </jsp:body>
</t:genericPage>