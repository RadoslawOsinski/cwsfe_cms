<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>
        <div class="row">
            <spring:url value="/newsletterMails/updateNewsletterMail" var="updateNewsletterMailUrl"
                        htmlEscape="true"/>
            <form id="editNewsletterForm" method="post" action="${updateNewsletterMailUrl}" autocomplete="off">
                <fieldset>
                    <legend><spring:message code="NewsletterMailEdit"/></legend>
                    <input type="hidden" name="id" id="newsletterMailId" value="${newsletterMail.id}"/>

                    <div class="row">
                        <label for="recipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="hidden" id="recipientGroupId" name="recipientGroupId"
                               value="${newsletterMail.recipientGroupId}"/>

                        <input type="text" id="recipientGroup"
                               value="${newsletterMailGroupName}"/>
                    </div>
                    <div class="row">
                        <label for="newsletterName"><spring:message code="Name"/></label>

                        <input type="text" id="newsletterName" name="name"
                               maxlength="100"
                               value="${newsletterMail.name}"/>
                    </div>
                    <div class="row">
                        <label for="newsletterSubject"><spring:message code="Subject"/></label>

                        <input type="text" id="newsletterSubject" name="subject"
                               maxlength="100"
                               value="${newsletterMail.subject}"/>
                    </div>
                    <div class="row">
                        <label for="newsletterContent"><spring:message code="Content"/></label>

                                <textarea id="newsletterContent" name="mailContent"
                                          cols="30"
                                          rows="15">${newsletterMail.mailContent}</textarea>
                    </div>
                    <div class="row">
                        <input type="submit" name="requestHandler" value="<spring:message code="Save"/>"
                               class="button small radius"/>
                        <input type="reset" value="Reset" class="button small radius alert">
                        <input type="button" id="confirmSendButton" value="<spring:message code="Send"/>"
                               class="button small radius secondary"/>
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <div id="newsletterTestSendFormValidation" class="alert-small">
                <span class="close"></span>
            </div>
            <form id="newsletterTestSendForm">
                <fieldset>
                    <legend><spring:message code="NewsletterTestSend"/></legend>
                    <div class="row">
                        <label for="testEmail"><spring:message code="Email"/></label>

                        <input type="email" id="testEmail" maxlength="350"/>
                    </div>
                    <div class="row">
                        <input type="button" id="newsletterTestSendButton" value="<spring:message code="TestSend"/>" class="button small radius primary">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div id="confirmSendNewsletterModal" class="reveal-modal" data-reveal>
            <p><spring:message code="DoYouReallyWantToSendNewsletter"/></p>
            <input type="button" id="confirmSendNewsletterButton" value="<spring:message code="Send"/>" class="button small radius primary">
            <input type="button" id="cancelSendNewsletterButton" value="<spring:message code="Cancel"/>" class="button small radius alert">
            <a class="close-reveal-modal">&#215;</a>
        </div>

    </jsp:body>
</t:genericPage>
