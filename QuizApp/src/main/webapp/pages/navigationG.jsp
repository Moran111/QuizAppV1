<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="userInfo" scope="session" value="${userInfo}"></c:set>

<nav role="navigation" class="navbar navbar-default">
    <div class="">

    </div>
    <div class="navbar-collapse">
        <ul class="nav navbar-nav">
            <li class="active"><a href="/QuizApp/QuizTypeServlet"> Home </a></li>
            <li class="active"><a href="/QuizApp/pages/login.html"> Login </a></li>
            <li class="active"><a href="/QuizApp/LogoutServlet"> Logout </a></li>
            <li class="active"><a href="/QuizApp/pages/feedback.jsp"> Feedback </a></li>
            <li class="active"><a href="/QuizApp/pages/contact.jsp"> Contact Us </a></li>
            <c:if test="${userInfo.userName == 'admin'}">
                <li class="active"><a href="/QuizApp/pages/admin.jsp"> Admin Page </a></li>
            </c:if>

        </ul>
    </div>
</nav>