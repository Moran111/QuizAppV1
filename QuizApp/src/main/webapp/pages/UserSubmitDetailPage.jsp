<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<c:set var="allQuizInfo" scope="session" value="${allQuizInfo}"></c:set>

<%@ include file="navigationG.jsp" %>

<c:forEach items="${questAllChoice}" var="entry">
    ${entry.key} <br>
    <c:forEach items="${entry.value}" var="choice">
        ${choice} <br>
    </c:forEach>
    You choose: ${questUserChoice.get(entry.key)} <br>
    The correct Answer is : ${questCorrectChoice.get(entry.key)} <br>
    <br>
</c:forEach>

The Total Score is ${totalScore} <br>
<c:if test="${totalScore >= 6}">
    You passed the quiz! <a href="/QuizApp/QuizTypeServlet">Take another quiz</a>
</c:if>
<c:if test="${totalScore < 6}">
    You failed the quiz! <a href="/QuizApp/QuizTypeServlet">Take another quiz</a>
</c:if>


</body>
</html>