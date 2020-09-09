<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<script>
    function change() {
        consolg.log("the onclick function is called");
        var elem = document.getElementById("button");
        if (elem.value == "SUSPEND") {
            elem.value = "ACTIVE";
            elem.innerHTML = "ACTIVE";
        } else {
            elem.value = "SUSPEND";
            elem.innerHTML = "SUSPEND";
        }
    }

</script>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Insert title here</title>
</head>
<body>

<%@ include file="navigationG.jsp" %>

<form action="/QuizApp/DisplayProfileServlet" method="Post">

    <c:set var="currentPage" scope="session" value="${currentPage}"></c:set>
    <c:set var="start" scope="session" value="${start}"></c:set>
    <c:set var="end" scope="session" value="${end}"></c:set>

    page: ${currentPage} <br>
    <c:forEach var="info" items="${profiles}" begin="${start}" end="${end}" varStatus="counter">
        <input type="hidden" name="profileIdx"
               value="${info.id}"> First Name:  ${info.firstName} Last Name:  ${info.lastName}<br>

        <c:if test="${info.status == 'SUSPEND'}">
            <button type="submit" name="button" id="button" value="${info.status} ${info.id}" onclick="change()">
                active
            </button>
        </c:if>

        <c:if test="${info.status == 'ACTIVE'}">
            <button type="submit" name="button" id="button" value="${info.status} ${info.id}" onclick="change()">
                suspend
            </button>
        </c:if>
        <br>

        <a href="/QuizApp/pages/admin.jsp"> </a>
    </c:forEach>

    <c:if test="${currentPage != 1}">
        <input type="submit" name="page" value="prev">
    </c:if>

    <c:if test="${currentPage != numOfPage}">
        <input type="submit" name="page" value="next">
    </c:if>


</form>

</body>
</html>