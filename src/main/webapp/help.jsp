<%--
  Created by IntelliJ IDEA.
  User: HeavyMetal77
  Date: 26.06.2019
  Time: 12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Help</title>
</head>
<body>

<h1>Существующие команды:</h1>

connect|database|user|password<br>
Соединение с базой данных<br>

exit<br>
Выход из программы<br>

<h5>createDB|databaseName</h5> <br>
<h5>Создание новой базы данных (Имя базы должно начинаться с буквы).</h5> <br>

<h5>databases</h5> <br>
<h5>Получение списка баз данных.</h5> <br>

<h5>tables</h5> <br>
<h5>Вывод списка всех таблиц</h5> <br>

<h5>find|tableName</h5> <br>
<h5>Вывод содержимого таблицы 'tableName'</h5> <br>

<h5>createTable|tableName|column1|column2|...|columnN</h5> <br>
<h5>Создать таблицу 'tableName' с колонками 'column1'...'columnN', при этом автоматически создается колонка id с автоинкрементом</h5> <br>

<h5>insert|tableName|column1|value1|column2|value2|... </h5><br>
<h5>Вставить данные в таблицу 'tableName': 'column1|value1|column2|value2'...</h5> <br>

<h5>update|tableName|column1|value1|column2|value2 </h5><br>
<h5>Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1</h5> <br>

<h5>drop|tableName</h5> <br>
<h5>Удалить таблицу 'tableName'</h5> <br>

<h5>dropDB|databaseName</h5> <br>
<h5>Удаление базы данных 'databaseName'. Перед удалением закрыть все соединения!</h5> <br>

<h5>clear|tableName</h5> <br>
<h5>Очистка содержимого таблицы 'tableName'</h5> <br>

<h5>delete|tableName|columnName|columnValue</h5> <br>
<h5>Удаление записи в таблице 'tableName', где columnName = columnValue </h5><br>

<a href="/menu">Menu</a> <br>
</body>
</html>
