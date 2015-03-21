<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericPage>
    <jsp:body>

        <div class="row">
            <h3><spring:message code="Folders"/></h3>
            <table id="foldersList">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><spring:message code="Folder"/></th>
                    <th scope="col"><spring:message code="OrderNumber"/></th>
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
                    <legend><spring:message code="FoldersAdding"/></legend>
                    <div class="row">
                        <label for="folderName"><spring:message code="Folder"/></label>
                        <input type="text" id="folderName"/>
                    </div>
                    <div class="row">
                        <label for="orderNumber"><spring:message code="OrderNumber"/></label>
                        <input type="text" id="orderNumber"/>
                    </div>
                    <div class="row">
                        <input type="button" id="addFolderButton" class="button small radius primary"
                               value="<spring:message code="Add"/>">
                        <input type="reset" value="Reset" class="button small radius alert">
                    </div>
                </fieldset>
            </form>
        </div>

    </jsp:body>
</t:genericPage>