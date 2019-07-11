<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>CreateDB</title>
</head>
<body>
<p>Создание базы данных:</p><br>
<form action="createDB" method="post">
    <table>
        <tr>
            <td>Введите название базы данных:</td>
            <td><input type="text" name="databaseName"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="createDB"/></td>
        </tr>
    </table>
</form>
<a href="help">Help</a> <br>
<a href="menu">Menu</a> <br>
</body>
</html>