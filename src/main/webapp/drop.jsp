<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Clear</title>
</head>
<body>
<p>Удалить таблицу:</p><br>
<form action="drop" method="post">
    <table>
        <tr>
            <td>Введите название таблицы:</td>
            <td><input type="text" name="nameTable"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="drop"/></td>
        </tr>
    </table>
</form>
<a href="help">Help</a> <br>
<a href="menu">Menu</a> <br>
</body>
</html>