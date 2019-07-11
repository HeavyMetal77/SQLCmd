<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>FIND</title>
</head>
<body>
<h1>База данных содержит таблицы:</h1><br>
<table border="1">
    <c:forEach items="${listtable}" var="element">
            <td>
                ${element}
            </td>
    </c:forEach>
    <br>
    <a href="menu">Menu</a> <br>
</table>
</body>
</html>