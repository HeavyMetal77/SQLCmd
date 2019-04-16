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

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
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
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //help
                "Существующие команды:\r\n" +
                "\tconnect|database|user|password - \r\n" +
                "\t\tсоединение с базой данных\r\n" +
                "\ttables - \r\n" +
                "\t\tвывод списка всех таблиц\r\n" +
                "\tclear|tableName - \r\n" +
                "\t\tочистка содержимого таблицы tableName\r\n" +
                "\tfind|tableName - \r\n" +
                "\t\tвывод содержимого таблицы tableName\r\n" +
                "\tdrop|tableName - \r\n" +
                "\t\tудалить таблицу tableName\r\n" +
                "\tcreateTable|tableName|column1|column2|...|columnN - \r\n" +
                "\t\tсоздать таблицу tableName с колонками column1...columnN \r\n" +
                "\tinsert|tableName|column1|value1|column2|value2|... - \r\n" +
                "\t\tвставить данные в таблицу tableName: column1|value1 ....\r\n" +
                "\texit - \r\n" +
                "\t\tвыход из программы\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу", getData().toString().trim().replace("", ""));
        //https://stackoverflow.com/questions/36324452/assertequalsstring-string-comparisonfailure-when-contents-are-identical
        //или после каждого перевода строки \r\n заменить на \r\n как в testExit()
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
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testFindTestWithoutConnect() {
        //given
        in.add("find|test");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //find|test
                "Вы не можете пользоваться командой 'find|test', " +
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
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
    public void testFindTableWithoutAttributeAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("find|test1");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find|test1 - (TableWithoutAttribute)
                "В таблице не создано ни одного атрибута!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testFindTableAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("find|contact_value");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find|contact_value
                "+-----------+-----------+---------------+--------------------------------------------------+----------------------+----------------------+------+\r\n" +
                "+id         +id_contact +id_contact_type+value                                             +created               +updated               +active+\r\n" +
                "+-----------+-----------+---------------+--------------------------------------------------+----------------------+----------------------+------+\r\n" +
                "+-----------+-----------+---------------+--------------------------------------------------+----------------------+----------------------+------+\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }


    @Test
    public void testFindWithErrorAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("find|nonexist");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //find|nonexist
                "Таблицы nonexist не существует!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testFindWithNotEnoughParametersAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("find|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
    public void testClearAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("clear|test1");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //clear|test1
                "TABLE test1 was successfully clear!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testClearAfterConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("clear|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
    public void testConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect|sqlcmd
                "Неверное количество параметров: ожидается: 4, введено: 2\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testCreateTableAfterConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("createTable|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
    public void testCreateAndDropTableAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("createTable|test3|test3|");
        in.add("drop|test3");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createTable|test3|test3|
                "TABLE test3 was successfully created!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop|test3
                "TABLE test3 was successfully deleted!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testDropTableAfterConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("createTable|test3|test3|");
        in.add("drop|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //createTable|test3|test3|
                "TABLE test3 was successfully created!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop|
                "Количество параметров не соответствует шаблону!\r\n" +
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
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //tables
                "Вы не можете пользоваться командой 'tables', " +
                "пока не подключитесь к базе данных " +
                "командой connect|database|login|password\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("drop|test3");
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //drop|test3
                "TABLE test3 was successfully deleted!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //tables
                "[category, test1, contact_type, contact_value, contact, test2, test]\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertAfterConnectWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("insert|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert|
                "Ошибка! Причина: Недостаточно параметров!\r\n" +
                "Повтори попытку!\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertErrorAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("insert|test2|id|9|nametest2|test177|field1|test818");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert|test2|id|9|nametest2|test177|field1|test818
                "Ошибка! Причина: Данные не вставлены!\r\n" +
                "Повтори попытку!\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testInsertSuccessAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("insert|test|test1|111|test2|222");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //insert|test|test1|111|test2|222
                "Данные успешно вставлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testNullInsertAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("\n");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
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
    public void testUpdateAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("update|test|test1|555|test2|666");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update|test|test1|555|test2|666
                "Данные успешно обновлены!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUpdateErrorAfterConnect() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("update|test|test1|777|test3|888");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update|test|test1|777|test3|888
                "Ошибка! Причина: Данные не обновлены!\r\n" +
                "Повтори попытку!\r\n" +
                //exit
                "Программа завершила работу\r\n", getData());
    }

    @Test
    public void testUpdateWithNotEnoughParameters() {
        //given
        in.add("connect|sqlcmd|sqlcmd|sqlcmd");
        in.add("update|test|test1");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\r\n" +
                "Файл конфигурации не загружен!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password\r\n" +
                //connect
                "Подключение к базе выполнено успешно!\r\n" +
                "Введи команду или 'help' для помощи:\r\n" +
                //update|test|test1
                "Ошибка! Причина: Недостаточно параметров!\r\n" +
                "Повтори попытку!\r\n" +
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
