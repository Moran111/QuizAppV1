<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>

<form action="/QuizApp/AddQuestionServlet" method="post">
    <button name="button" value="add"> Add Questions</button>
    <button name="button" value="disable"> Disable Questions</button>
    <button name="button" value="update"> Update Questions</button>

    <input type="text" placeholder="Enter Question Description" name="quest" id="quest" required>
    <input type="text" placeholder="Enter Options Description" name="option" id="option" required>

    <input class="submit" align="center" type="submit" name="submit" value="registers">
</form>


</body>
</html>