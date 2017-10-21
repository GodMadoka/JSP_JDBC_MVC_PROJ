<%--
  Created by IntelliJ IDEA.
  User: ars_saki
  Date: 10/20/17
  Time: 8:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.web.jdbc.*" %>
<%@ page import="com.web.jdbc.Student" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Student Tracker App</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
    <div id="wrapper">
        <div id="header">
            <h2>FooBar University</h2>
        </div>
    </div>

    <div id="container">
        <div id="content">
            <input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp';return false;"
            class = "add-student-button">
            <form action="StudentControllerServlet" method="get">
                <input type="hidden" name="command" value="SEARCH" />
                Search student: <input type="text" name="theSearchName" />
                <input type="submit" value="Search" class="add-student-button">
            </form>
            <table>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="tempStudent" items="${STUDENT_LIST}">
                    <c:url  var="updateLink" value="StudentControllerServlet">
                        <c:param name="command" value="LOAD" />
                        <c:param name="studentId" value="${tempStudent.id}" />
                    </c:url>
                    <c:url  var="deleteLink" value="StudentControllerServlet">
                        <c:param name="command" value="DELETE" />
                        <c:param name="studentId" value="${tempStudent.id}" />
                    </c:url>
                    <tr>
                        <td> ${tempStudent.firstName} </td>
                        <td> ${tempStudent.lastName} </td>
                        <td> ${tempStudent.email} </td>
                        <td>
                            <a href="${updateLink}">Update</a>
                            |
                            <a href="${deleteLink}"
                               onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false;">
                                Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</body>
</html>