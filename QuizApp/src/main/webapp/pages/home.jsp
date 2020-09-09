<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="link.html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>


<%@ include file="navigationG.jsp" %>

<form class="form1" action="/QuizApp/GenerateQuizServlet" method=post>
    <c:forEach items="${requestScope.types}" var="type">
        <input type="radio" name="checkboxlist1" value="${type}"> ${type} <br>
    </c:forEach>
    <br>
    <input align="center" type="submit" value="Submit"/>
</form>


</body>
</html>