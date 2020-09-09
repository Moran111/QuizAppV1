<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="link.html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>

    <script language="javascript">
        var tim;
        var min = '${sessionScope.min}';
        var sec = '${sessionScope.sec}';
        var f = new Date();

        function customeSubmit(someValue) {
            document.questionForm.minute.value = min;
            document.questionForm.second.value = sec;
            document.questionForm.submit();
        }

        function examTimer() {
            if (parseInt(sec) > 0) {
                document.getElementById("showtime").innerHTML = "Time Remaining :" + min + " Minute ," + sec + " Seconds";
                sec = parseInt(sec) - 1;
                tim = setTimeout("examTimer()", 1000);
            } else {

                if (parseInt(min) == 0 && parseInt(sec) == 0) {
                    document.getElementById("showtime").innerHTML = "Time Remaining :" + min + " Minute ," + sec + " Seconds";
                    alert("Time Up");
                    document.questionForm.minute.value = 0;
                    document.questionForm.second.value = 0;
                    document.questionForm.submit();

                }

                if (parseInt(sec) == 0) {
                    document.getElementById("showtime").innerHTML = "Time Remaining :" + min + " Minute ," + sec + " Seconds";
                    min = parseInt(min) - 1;
                    sec = 59;
                    tim = setTimeout("examTimer()", 1000);
                }

            }
        }

        var userAnswered = '${sessionScope.answeredQuest}';
        var numQuest = '${sessionScope.numOfQuest}';

        function SubmitAllQuestion(someValue) {
            // if not finish all questions
            if (userAnswered < numQuest) {
                var r = confirm("You don't finish all question, submit it anyway?");
                if (r) {
                    customeSubmit(someValue);
                } else {

                }
            } else {
                customeSubmit(someValue);
            }
        }

    </script>

</head>
<body onload="examTimer()">

<%@ include file="navigationG.jsp" %>

${quizId}

<div id="showtime"></div>


<form action="/QuizApp/QuizServlet" method=post>

    <c:set var="currQuest" scope="session" value="${currQuest}"></c:set>
    <c:set var="numOfQuest" scope="session" value="${numOfQuest}"></c:set>
    <c:set var="prevChoice" scope="session" value="${prevChoice}"></c:set>


    ${question.get(currQuest)} <br>

    <c:forEach var="choice" items="${question.get(currQuest).getChoices()}" varStatus="counter">
        <c:choose>
            <c:when test="${prevChoice != null && prevChoice == counter.count}">
                <input type="radio" name="answer" value="${counter.count}" checked>${choice}  <br/>
            </c:when>
            <c:otherwise>
                <input type="radio" name="answer" value="${counter.count}">${choice}  <br/>
            </c:otherwise>
        </c:choose>
        <%--     	    	<c:if test = "${prevChoice != null && prevChoice == counter.count}">
                                <input type="radio" name="answer" value="${counter.count}" checked>${choice}  <br/>
                        </c:if>
                        <c:if test = "${prevChoice == null || pervChoice != counter.count}">
                                <input type="radio" name="answer" value="${counter.count}">${choice}  <br/>
                        </c:if> --%>

    </c:forEach>

    <c:if test="${sessionScope.currQuest > 0}">
        <input type="submit" name="submit" value="Prev" id="Prev" onclick="customSubmit()">
    </c:if>

    <c:if test="${sessionScope.currQuest < sessionScope.numOfQuest}">
        <input type="submit" name="submit" value="Next" id="Next" onclick="customSubmit()">
    </c:if>

    <input type="submit" name="submit" value="SubmitAll" onclick="SubmitAllQuestion()"/>
</form>


</body>
</html>