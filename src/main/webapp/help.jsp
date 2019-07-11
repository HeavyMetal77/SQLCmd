<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Help</title>
</head>
<body>

<h3>Существующие команды:</h3>

connect|database|user|password<br>
Соединение с базой данных<br>
<br>
exit<br>
Выход из программы<br>
<br>
createDB|databaseName<br>
Создание новой базы данных (Имя базы должно начинаться с буквы).<br>
<br>
databases<br>
Получение списка баз данных.<br>
<br>
tables <br>
Вывод списка всех таблиц <br>
<br>
find|tableName <br>
Вывод содержимого таблицы 'tableName' <br>
<br>
createTable|tableName|column1|column2|...|columnN <br>
Создать таблицу 'tableName' с колонками 'column1'...'columnN', при этом автоматически создается колонка id с автоинкрементом <br>
<br>
insert|tableName|column1|value1|column2|value2|... <br>
Вставить данные в таблицу 'tableName': 'column1|value1|column2|value2'... <br>
<br>
update|tableName|column1|value1|column2|value2 <br>
Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1 <br>
<br>
drop|tableName <br>
Удалить таблицу 'tableName' <br>
<br>
dropDB|databaseName <br>
Удаление базы данных 'databaseName'. Перед удалением закрыть все соединения! <br>
<br>
clear|tableName <br>
Очистка содержимого таблицы 'tableName' <br>
<br>
delete|tableName|columnName|columnValue <br>
Удаление записи в таблице 'tableName', где columnName = columnValue <br>
<br>
<a href="menu">Menu</a> <br>
</body>
</html>
