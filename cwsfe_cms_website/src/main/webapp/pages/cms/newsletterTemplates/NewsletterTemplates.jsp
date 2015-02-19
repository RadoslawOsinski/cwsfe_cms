<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <form>
            <fieldset>
                <legend><spring:message code="Search"/></legend>
                <div class="row">
                    <label for="searchName"><spring:message code="Name"/></label>
                    <input type="text" id="searchName"/>
                </div>
                <div class="row">
                    <label for="searchLanguage"><spring:message code="Language2LetterCode"/></label>
                    <input type="hidden" id="searchLanguageId" value=""/>

                    <input type="text" id="searchLanguage"/>
                </div>
                <div class="row">
                    <input type="submit" value="Submit" onclick="searchNewsletterTemplate();return false;"
                           class="button small radius">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

        <h3><spring:message code="NewsletterTemplatesManagement"/></h3>
        <table id="newsletterTemplatesList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Language"/></th>
                <th scope="col"><spring:message code="Name"/></th>
                <th scope="col"><spring:message code="Subject"/></th>
                <th scope="col"><spring:message code="Status"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <form id="addNewNewsletterTemplateForm">
            <fieldset>
                <legend><spring:message code="NewsletterTemplateAdding"/></legend>
                <div class="row">
                    <label for="language"><spring:message code="Language2LetterCode"/></label>
                    <input type="hidden" id="languageId"/>
                    <input type="text" id="language"/>
                </div>
                <div class="row">
                    <label for="newsletterTemplateName"><spring:message code="Name"/></label>
                    <input type="text" id="newsletterTemplateName" maxlength="100"/>
                </div>
                <div class="row">
                    <input type="button" id="addNewsletterTemplateButton" class="button small radius"
                           value="<spring:message code="Add"/>">
                    <input type="reset" value="Reset" class="button small radius alert">
                </div>
            </fieldset>
        </form>

    </jsp:body>
</t:genericPage>