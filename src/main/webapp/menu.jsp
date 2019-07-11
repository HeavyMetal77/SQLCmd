<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Menu</title>
</head>
<body>
<p>Список команд:</p><br>
<c:forEach items="${list}" var="item">
    <a href=${item}>${item}</a> <br>
</c:forEach>

</body>
</html>