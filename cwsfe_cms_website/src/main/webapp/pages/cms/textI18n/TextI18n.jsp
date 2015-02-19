<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <h3><spring:message code="Translations"/></h3>
            <table id="cmsTextI18nList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Language"/></th>
                    <th scope="col"><spring:message code="Category"/></th>
                    <th scope="col"><spring:message code="Key"/></th>
                    <th scope="col"><spring:message code="Text"/></th>
                    <th scope="col"><spring:message code="Actions"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="row">
            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="TranslationAdding"/></legend>
                    <div class="row">
                        <label for="searchLanguage"><spring:message code="Language"/></label>
                        <input type="hidden" id="searchLanguageId"/>
                        <input type="text" id="searchLanguage"/>
                    </div>
                    <div class="row">
                        <label for="searchCategory"><spring:message code="Category"/></label>
                        <input type="hidden" id="searchCategoryId"/>
                        <input type="text" id="searchCategory"/>
                    </div>
                    <div class="row">
                        <label for="key"><spring:message code="Key"/></label>
                        <input type="text" id="key"/>
                    </div>
                    <div class="row">
                        <label for="text"><spring:message code="Text"/></label>
                        <input type="text" id="text"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addCmsTextI18nButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>