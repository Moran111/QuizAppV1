<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script>
        function doSubmit() {
            console.log("clicked ")
            var actionURL = "/QuizApp/SubmissionDetailServlet";
            // perform your operations
            window.location = "SubmissionDetailServlet.java";
        }

        function sortTable(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("myTable2");
            switching = true;
            // Set the sorting direction to ascending:
            dir = "asc";
            /* Make a loop that will continue until
            no switching has been done: */
            while (switching) {
                // Start by saying: no switching is done:
                switching = false;
                rows = table.rows;
                /* Loop through all table rows (except the
                first, which contains table headers): */
                for (i = 1; i < (rows.length - 1); i++) {
                    // Start by saying there should be no switching:
                    shouldSwitch = false;
                    /* Get the two elements you want to compare,
                    one from current row and one from the next: */
                    x = rows[i].getElementsByTagName("TD")[n];
                    y = rows[i + 1].getElementsByTagName("TD")[n];
                    /* Check if the two rows should switch place,
                    based on the direction, asc or desc: */
                    if (dir == "asc") {
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            // If so, mark as a switch and break the loop:
                            shouldSwitch = true;
                            break;
                        }
                    } else if (dir == "desc") {
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                            // If so, mark as a switch and break the loop:
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                if (shouldSwitch) {
                    /* If a switch has been marked, make the switch
                    and mark that a switch has been done: */
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    // Each time a switch is done, increase this count by 1:
                    switchcount++;
                } else {
                    /* If no switching has been done AND the direction is "asc",
                    set the direction to "desc" and run the while loop again. */
                    if (switchcount == 0 && dir == "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
        }

        function myFunction() {
            // Declare variables
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("myInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("myTable2");
            tr = table.getElementsByTagName("tr");

            // Loop through all table rows, and hide those who don't match the search query
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[1];
                console.log(td);
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        function myFunction1() {
            // Declare variables
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("myInput1");
            filter = input.value.toUpperCase();
            table = document.getElementById("myTable2");
            tr = table.getElementsByTagName("tr");

            // Loop through all table rows, and hide those who don't match the search query
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[4];
                console.log(td);
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

    </script>
</head>
<body>

<%@ include file="navigationG.jsp" %>

<c:set var="quizInfo" scope="session" value="${quizObject}"></c:set>
<c:set var="userInfo" scope="session" value="${userInfo}"></c:set>
<c:set var="userResultSet" scope="session" value="${userResultSet}"> </c:set>
<c:set var="allQuizInfo" scope="session" value="${allQuizInfo}"></c:set>
<c:set var="start" scope="session" value="${start}"></c:set>
<c:set var="end" scope="session" value="${end}"></c:set>
<c:set var="currPage" scope="session" value="${currPage}"></c:set>

<form name="myForm" action="/QuizApp/DisplayAllUserQuizServlet" method="GET">

    <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for Category..."> <br>
    <input type="text" id="myInput1" onkeyup="myFunction1()" placeholder="Search for Result..."> <br>

    <table id="myTable2">
        <thead>
        <tr>
            <th scope="col" onclick="sortTable(0)">FullName</th>
            <th scope="col" onclick="sortTable(1)">Category</th>
            <th scope="col">Date</th>
            <th scope="col">NumOfQuestion</th>
            <th scope="col">Results</th>
            <th scope="col">ShowDetails</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="var" items="${userResultSet}" begin="${start}" end="${end}" varStatus="counter">
        <tr>
            <td> ${var.getUserfullname()} </td>
            <td> ${var.getCategroy()} </td>
            <td> ${var.getDate()} </td>
            <td> ${var.getNumOfQuest()} </td>
            <td> ${var.getIsPass()} </td>
            <td>
                <button name="button" value="${start + counter.count}"> ${start + counter.count} </button>
        </tr>
        </c:forEach>
        <tbody>
    </table>

    <c:if test="${currPage > 1}">
        <button class="submit" name="submit" value="Prev"> Prev</button>
    </c:if>
    <c:if test="${currPage < totalPages}">
        <button class="submit" name="submit" value="Next"> Next</button>
    </c:if>
</form>

</body>
</html>