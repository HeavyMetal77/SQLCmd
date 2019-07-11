<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>FIND</title>
</head>
<body>
<h1>Получить данные с таблицы:</h1><br>
<form action="find" method="post">
    <table>
        <tr>
            <td>Имя таблицы</td>
            <td><input type="text" name="nameTable"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="connect"/></td>
        </tr>
    </table>
</form>
<a href="help">Help</a> <br>
<a href="menu">Menu</a> <br>
</body>
</html>