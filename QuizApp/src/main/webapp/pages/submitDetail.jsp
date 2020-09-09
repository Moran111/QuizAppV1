<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="link.html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="navigationG.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<p> ${username} result ${quizname}</p> <br>

name ${firstname} ${lastname} <br><br>

${starttime} <br>
${endtime} <br>
<c:forEach var="answer" items="${answer}">
    correct answer is : ${answer}<br>
</c:forEach>

<c:forEach var="UserAnswer" items="${UserAnswer}">
    ${UserAnswer}<br>
</c:forEach>


Total Correct Answers: ${result} <br>

<a href="http://localhost:8080/QuizApp/QuizTypeServlet">Take Another Quiz</a>

</body>
</html>