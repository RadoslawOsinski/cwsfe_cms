<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="osName" type="java.lang.String"--%>
<%--@elvariable id="osVersion" type="java.lang.String"--%>
<%--@elvariable id="architecture" type="java.lang.String"--%>
<%--@elvariable id="availableCPUs" type="java.lang.Integer"--%>
<%--@elvariable id="usedMemoryInMb" type="java.lang.Double"--%>
<%--@elvariable id="availableMemoryInMB" type="java.lang.Double"--%>
<t:genericPage>
    <jsp:body>

        <table>
            <thead>
            <tr>
                <th width="200">Parameter</th>
                <th width="150">Value</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Operating System</td>
                <td>${osName} ${osVersion}</td>
            </tr>
            <tr>
                <td>Architecture</td>
                <td>${architecture}</td>
            </tr>
            <tr>
                <td>Available CPUs</td>
                <td>${availableCPUs}</td>
            </tr>
            <tr>
                <td>Used Memory</td>
                <td><span id="usedMemoryInMb">${usedMemoryInMb}</span> Mb</td>
            </tr>
            <tr>
                <td>Available memory</td>
                <td><span id="availableMemoryInMB">${availableMemoryInMB}</span> Mb</td>
            </tr>
            </tbody>
        </table>

    </jsp:body>
</t:genericPage>
