<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <form id="editMailGroupForm">
            <fieldset>
                <legend><spring:message code="MailGroupEdit"/></legend>
                <input type="hidden" id="mailGroupId" name="mailGroupId" value="${newsletterMailGroup.id}"/>

                <div class="row">
                    <label for="newsletterMailGroupName"><spring:message
                            code="NewsletterMailGroupName"/></label>
                    <input type="text" id="newsletterMailGroupName" maxlength="100"
                           value="${newsletterMailGroup.name}"/>
                </div>
                <div class="row">
                    <label for="language"><spring:message code="Language2LetterCode"/></label>
                    <input type="hidden" id="languageId" value="${newsletterMailGroup.languageId}"/>
                    <input type="text" id="language" value="${newsletterMailGroupLanguageCode}"/>
                </div>
                <div class="row">
                    <input type="button" id="addNewsletterMailGroupButton" class="button small radius"
                           value="<spring:message code="Add"/>">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

        <form>
            <fieldset>
                <legend><spring:message code="Search"/></legend>
                <div class="row">
                    <label for="searchMail"><spring:message code="Mail"/></label>
                    <input type="text" id="searchMail"/>
                </div>
                <div class="row">
                    <input type="submit" value="Submit" onclick="searchMailInNewsletterMailGroup();return false;"
                           class="button small radius">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

        <h3><spring:message code="NewsletterMailAddressesManagement"/></h3>
        <table id="newsletterMailAddressesList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Email"/></th>
                <th scope="col"><spring:message code="Status"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <form id="addNewMailAddressForm">
            <fieldset>
                <legend><spring:message code="MailAddressAdding"/></legend>
                <div class="row">
                    <label for="newsletterMailAddress"><spring:message code="Email"/></label>
                    <input type="email" id="newsletterMailAddress" maxlength="350"/>
                </div>
                <div class="row">
                    <input type="button" id="addNewsletterMailAddressButton" class="button small radius"
                           value="<spring:message code="Add"/>">
                    <input type="reset" value="Reset">
                </div>
            </fieldset>
        </form>

    </jsp:body>
</t:genericPage>