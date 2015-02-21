<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <form>
                <fieldset>
                    <legend><spring:message code="Search"/></legend>
                    <div class="row">
                        <label for="searchName"><spring:message code="Name"/></label>
                        <input type="text" id="searchName"/>
                    </div>
                    <div class="row">
                        <label for="searchRecipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="hidden" id="searchRecipientGroupId" value=""/>
                        <input type="text" id="searchRecipientGroup"/>
                    </div>
                    <div class="row">
                        <input type="submit" value="Submit" id="searchNewsletterMailButton"
                               class="button small radius">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

        <div class="row">
            <h3><spring:message code="NewsletterMailsManagement"/></h3>
            <table id="newsletterMailsList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="RecipientGroup"/></th>
                    <th scope="col"><spring:message code="Name"/></th>
                    <th scope="col"><spring:message code="Subject"/></th>
                    <th scope="col"><spring:message code="Status"/></th>
                    <th scope="col"><spring:message code="Actions"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="row">
            <form id="addNewNewsletterMailForm">
                <fieldset>
                    <legend><spring:message code="NewsletterMailAdding"/></legend>
                    <div class="row">
                        <label for="recipientGroup"><spring:message code="RecipientGroup"/></label>
                        <input type="hidden" id="recipientGroupId"/>

                        <input type="text" id="recipientGroup"/>
                    </div>
                    <div class="row">
                        <label for="newsletterMailName"><spring:message code="Name"/></label>
                        <input type="text" id="newsletterMailName" maxlength="100"/>
                    </div>
                    <div class="row">
                        <label for="newsletterMailSubject"><spring:message code="Subject"/></label>
                        <input type="text" id="newsletterMailSubject" maxlength="100"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addNewsletterMailButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>
