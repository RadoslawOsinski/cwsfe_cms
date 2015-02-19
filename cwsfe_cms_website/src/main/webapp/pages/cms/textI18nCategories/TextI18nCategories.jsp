<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
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
        </div>

        <div class="row">
            <form class="large-5">
                <fieldset>
                    <legend><spring:message code="TranslationCategoryAdding"/></legend>
                    <div class="row">
                        <label for="category"><spring:message code="Category"/></label>
                        <input type="text" id="category"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addCmsTextI18nCategoryButton" class="button small radius"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert"/>
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>