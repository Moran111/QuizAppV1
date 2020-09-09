<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="js/formValidation.js"></script>
<script language="javascript">
    function customSubmit(someValue) {
        var r = confirm("Received your feedback");
        document.questionForm.submit();
    }
</script>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>

<body>
<%@ include file="navigationG.jsp" %>

<form action="/QuizApp/FeedbackServlet" method="post">

    <h1>Feedback Form</h1>

    <hr>

    <label for="feedback"><b>Enter Feedback</b></label>
    <input type="text" placeholder="Enter feedback" name="feedback" id="feedback">

    <br>

    <div id="rate1">

        <input type="radio" name="rating" value="1" class="star">
        <input type="radio" name="rating" value="2" class="star">
        <input type="radio" name="rating" value="3" class="star">
        <input type="radio" name="rating" value="4" class="star">
        <input type="radio" name="rating" value="5" class="star">
    </div>

    <script>
        $("input[name='rating']").rating('select', rating);
    </script>

    <input type="submit" name="submit" value="feedback" onclick="customSubmit()">

    <hr>


</form>

</body>
</html>