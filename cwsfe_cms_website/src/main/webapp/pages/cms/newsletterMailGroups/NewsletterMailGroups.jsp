<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="Search"/></h3>

        <form>
            <div class="row">
                <label for="searchName"><spring:message code="NewsletterMailGroupName"/></label>
                <input type="text" id="searchName"/>
            </div>
            <div class="row">
                <label for="searchLanguage"><spring:message code="Language2LetterCode"/></label>
                <input type="hidden" id="searchLanguageId" value=""/>
                <input type="text" id="searchLanguage"/>
            </div>
            <div class="row">
                <input type="submit" value="Submit" onclick="searchNewsletterMailGroup();return false;"
                       class="button small radius">
                <input type="reset" value="Reset" class="button small radius alert">
            </div>
        </form>

        <h3><spring:message code="NewsletterMailGroupsManagement"/></h3>
        <table id="newsletterMailGroupsList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="NewsletterMailGroupName"/></th>
                <th scope="col"><spring:message code="Language"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="NewsletterMailGroupAdding"/></h3>

        <form id="addNewNewsletterMailGroupForm">
            <div class="row">
                <label for="language"><spring:message code="Language2LetterCode"/></label>
                <input type="hidden" id="languageId"/>
                <input type="text" id="language"/>
            </div>
            <div class="row">
                <label for="newsletterMailGroupName"><spring:message
                        code="NewsletterMailGroupName"/></label>
                <input type="text" id="newsletterMailGroupName" maxlength="100"/>

                <div class="row">
                    <input type="button" id="addNewsletterMailGroupButton" class="button small radius"
                           value="<spring:message code="Add"/>">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </div>
        </form>

    </jsp:body>
</t:genericPage>