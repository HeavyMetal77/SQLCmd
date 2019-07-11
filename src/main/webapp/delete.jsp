<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Delete</title>
</head>
<body>
<p>Удаление данных таблицы:</p><br>
<form action="delete" method="post">
    <table>
        <tr>
            <td>Введите название таблицы:</td>
            <td><input type="text" name="nameTable"/></td>
        </tr>
        <tr>
            <td>Введите название столбца таблицы:</td>
            <td><input type="text" name="columnName"/></td>
        </tr>
        <tr>
            <td>Введите значение столбца таблицы:</td>
            <td><input type="text" name="columnValue"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="delete"/></td>
        </tr>
    </table>
</form>
<a href="help">Help</a> <br>
<a href="menu">Menu</a> <br>
</body>
</html>