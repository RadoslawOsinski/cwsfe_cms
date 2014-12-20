<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <h3><spring:message code="TranslationCategories"/></h3>
        <table id="cmsTextI18nCategoriesList">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col"><spring:message code="Category"/></th>
                <th scope="col"><spring:message code="Status"/></th>
                <th scope="col"><spring:message code="Actions"/></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <h3><spring:message code="TranslationCategoryAdding"/></h3>

        <form>
            <div class="row">
                <label for="category"><spring:message code="Category"/></label>
                <input type="text" id="category"/>
            </div>
            <div class="row">
                <input type="button" id="addCmsTextI18nCategoryButton" class="button small radius"
                       value="<spring:message code="Add"/>">
                <input type="reset" value="Reset" class="button small radius alert"/>
            </div>
        </form>

    </jsp:body>
</t:genericPage>