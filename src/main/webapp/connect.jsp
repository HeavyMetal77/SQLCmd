<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Connect</title>
</head>
<body>

<h1>Connect:</h1><br>

<form action="connect" method="post">
    <table>
        <tr>
            <td>Database name</td>
            <td><input type="text" name="dbname"/></td>
        </tr>
        <tr>
            <td>User name</td>
            <td><input type="text" name="username"/></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="connect"/></td>
        </tr>
    </table>
</form>
<a href="help">Help</a> <br>


</body>
</html>
