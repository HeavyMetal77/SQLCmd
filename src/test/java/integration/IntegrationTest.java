package integration;

import controller.Main;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private String connectLogin = "connect|sqlcmd|sqlcmd|sqlcmd";
    private String listTables = "База данных содержит таблицы: category, contact_type, contact_value, contact";
    private String listTablesAfterTest = "База данных содержит таблицы: category, contact_type, contact_value, contact, testtable";

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testNullInsertAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("\n");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //null
                "Команды '' не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                "Существующие команды:\r\n" +
                "\tconnect|database|user|password\r\n" +
                "\t\tСоединение с базой данных\r\n" +
                "\r\n" +
                "\texit\r\n" +
                "\t\tВыход из программы\r\n" +
                "\r\n" +
                "\tcreateDB|databaseName\r\n" +
                "\t\tСоздание новой базы данных (Имя базы должно начинаться с буквы).\r\n" +
                "\r\n" +
                "\tdatabases\r\n" +
                "\t\tПолучение списка баз данных.\r\n" +
                "\r\n" +
                "\ttables\r\n" +
                "\t\tВывод списка всех таблиц\r\n" +
                "\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tВывод содержимого таблицы 'tableName'\r\n" +
                "\r\n" +
                "\tcreateTable|tableName|column1|column2|...|columnN\r\n" +
                "\t\tСоздать таблицу 'tableName' с колонками 'column1'...'columnN', при этом автоматически создается колонка id с автоинкрементом\r\n" +
                "\r\n" +
                "\tinsert|tableName|column1|value1|column2|value2|...\r\n" +
                "\t\tВставить данные в таблицу 'tableName': 'column1|value1|column2|value2'...\r\n" +
                "\r\n" +
                "\tupdate|tableName|column1|value1|column2|value2\r\n" +
                "\t\tКоманда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1\r\n" +
                "\r\n" +
                "\tdrop|tableName\r\n" +
                "\t\tУдалить таблицу 'tableName'\r\n" +
                "\r\n" +
                "\tdropDB|databaseName\r\n" +
                "\t\tУдаление базы данных 'databaseName'. Перед удалением закрыть все соединения!\r\n" +
                "\r\n" +
                "\tclear|tableName\r\n" +
                "\t\tОчистка содержимого таблицы 'tableName'\r\n" +
                "\r\n" +
                "\tdelete|tableName|columnName|columnValue\r\n" +
                "\t\tУдаление записи в таблице 'tableName', где columnName = columnValue\r\n" +
                "\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
        //https://stackoverflow.com/questions/36324452/assertequalsstring-string-comparisonfailure-when-contents-are-identical
    }

    @Test
    public void testExit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect|sqlcmd
                "Неверное количество параметров: ожидается: 4, введено: 2\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testConnectWithWrongParameters() {
        //given
        in.add("connect|sqlcmd|wrongLogin|wrongPassword");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect|sqlcmd
                "Ошибка! Причина: Ошибка подключения к базе данных!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateTableAfterConnectWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("createTable|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createTable|
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateTableErrorAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("createTable|DISTINCT|test|value");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Ошибка! Причина: Ошибка создания таблицы DISTINCT, по причине: " +
                "ERROR: syntax error at or near \"DISTINCT\"\n" +
                "  Position: 28\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        //given
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //tables
                "Вы не можете пользоваться командой 'tables', " +
                "пока не подключитесь к базе данных " +
                "командой connect|database|login|password\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //unsupported
                "Вы не можете пользоваться командой 'unsupported', " +
                "пока не подключитесь к базе данных " +
                "командой connect|database|login|password\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //unsupported
                "Команды 'unsupported' не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testFindWithErrorAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("find|nonexist");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find|nonexist
                "Ошибка! Причина: Таблицы nonexist не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertAfterConnectWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("insert|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert|
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDropCreateInsertFindTableAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("drop|testtable");
        in.add("createTable|testtable|name|surname");
        in.add("insert|testtable|name|Will|surname|Smith");
        in.add("find|testtable");
        in.add("drop|testtable");
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+------+---------+\r\n" +
                "+ id + name + surname +\r\n" +
                "+----+------+---------+\r\n" +
                "+ 1  + Will + Smith   +\r\n" +
                "+----+------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //tables
                listTables + "\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateTableFindTableWithoutDataAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("drop|testtable");
        in.add("createTable|testtable|name|surname");
        in.add("find|testtable");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "+----+------+---------+\r\n" +
                "+ id + name + surname +\r\n" +
                "+----+------+---------+\r\n" +
                "+----+------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testFindWithNotEnoughParametersAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("find|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find|
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testClearAfterConnectWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("clear|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //clear|
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testClearAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("drop|testtable");
        in.add("createTable|testtable|name|surname");
        in.add("insert|testtable|name|Will|surname|Smith");
        in.add("find|testtable");
        in.add("clear|testtable");
        in.add("find|testtable");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+------+---------+\r\n" +
                "+ id + name + surname +\r\n" +
                "+----+------+---------+\r\n" +
                "+ 1  + Will + Smith   +\r\n" +
                "+----+------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //clear
                "Таблица testtable была успешно очищена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+------+---------+\r\n" +
                "+ id + name + surname +\r\n" +
                "+----+------+---------+\r\n" +
                "+----+------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateAndDropTableAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("createTable|testtable|name|surname");
        in.add("drop|testtable");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createTable
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDropTableAfterConnectWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("drop|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop|
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("drop|testtable");
        in.add("createTable|testtable|name|surname");
        in.add("tables");
        in.add("drop|testtable");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //tables
                listTablesAfterTest + "\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertSuccessAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("drop|testtable");
        in.add("createTable|testtable|name|surname");
        in.add("insert|testtable|name|Herbert|surname|Schildt");
        in.add("find|testtable");
        in.add("drop|testtable");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+---------+---------+\r\n" +
                "+ id + name    + surname +\r\n" +
                "+----+---------+---------+\r\n" +
                "+ 1  + Herbert + Schildt +\r\n" +
                "+----+---------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertErrorAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("insert|testnonexist|nametest2|test177|field1|test818");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Ошибка! Причина: Таблицы 'testnonexist' не существует! Проверьте правильность названия таблицы!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUpdateAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("createTable|testtable|name|surname");
        in.add("insert|testtable|name|Herbert|surname|Schildt");
        in.add("find|testtable");
        in.add("update|testtable|id|1|name|Cay|surname|Horstmann");
        in.add("drop|testtable");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+---------+---------+\r\n" +
                "+ id + name    + surname +\r\n" +
                "+----+---------+---------+\r\n" +
                "+ 1  + Herbert + Schildt +\r\n" +
                "+----+---------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update
                "Данные успешно обновлены!\r\n" +
                "+----+------+-----------+\r\n" +
                "+ id + name + surname   +\r\n" +
                "+----+------+-----------+\r\n" +
                "+ 1  + Cay  + Horstmann +\r\n" +
                "+----+------+-----------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //delete
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUpdateErrorAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("update|nonexist|test1|777|test3|888");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update
                "Ошибка! Причина: Ошибка обновления данных в таблице nonexist, по причине: " +
                "ERROR: relation \"nonexist\" does not exist\n" +
                "  Position: 8\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUpdateWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("update|test|test1");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDeleteWithNotEnoughParameters() {
        //given
        in.add(connectLogin);
        in.add("delete|testtable|id");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //delete
                "Количество параметров не соответствует шаблону!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDeleteErrorAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("createTable|testtable|name|surname");
        in.add("insert|testtable|name|Herbert|surname|Schildt");
        in.add("find|testtable");
        in.add("delete|testtable|id|1");
        in.add("drop|testtable");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //create
                "Таблица testtable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find
                "+----+---------+---------+\r\n" +
                "+ id + name    + surname +\r\n" +
                "+----+---------+---------+\r\n" +
                "+ 1  + Herbert + Schildt +\r\n" +
                "+----+---------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //delete
                "Запись в таблице testtable со значением id = 1  была успешно удалена!\r\n" +
                "+----+------+---------+\r\n" +
                "+ id + name + surname +\r\n" +
                "+----+------+---------+\r\n" +
                "+----+------+---------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop
                "Таблица testtable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDeleteAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("delete|nonexist|test1|777");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //delete
                "Ошибка! Причина: Таблицы nonexist не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testGetSizeTable() {
        //given
        in.add(connectLogin);
        in.add("drop|testGetSizeTable");
        in.add("createTable|testGetSizeTable|name|surname");
        in.add("insert|testGetSizeTable|name|Arun|surname|Gupta");
        in.add("insert|testGetSizeTable|name|Cay|surname|Horstmann");
        in.add("find|testGetSizeTable");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //delete
                "Таблица testGetSizeTable была успешно удалена!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Таблица testGetSizeTable была успешно создана!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                "+----+------+-----------+\r\n" +
                "+ id + name + surname   +\r\n" +
                "+----+------+-----------+\r\n" +
                "+ 1  + Arun + Gupta     +\r\n" +
                "+ 2  + Cay  + Horstmann +\r\n" +
                "+----+------+-----------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateDatabaseAfterConnect() {
        //given
        in.add(connectLogin);
        in.add("createDB|databasename");
        in.add("databases");
        in.add("createDB|databasename");
        in.add("dropDB|databasenameWrong");
        in.add("dropDB|databasename");
        in.add("exit");


        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: \n" +
                "'connect|database|user|password' \n" +
                "или 'help' для получения помощи\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createDB
                "База данных 'databasename' успешно создана.\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //databases
                "Существующие базы данных: postgres, sqlcmd, databasetest, databasename\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createDB Error
                "Ошибка! Причина: null\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //dropDB|databasenameWrong
                "Ошибка! Причина: Базы данных с таким названием не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //dropDB
                "База данных 'databasename' успешно удалена.\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }


    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}