package integration;

import controller.Main;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    //работает перед всеми тестами 1 раз
    @BeforeClass
    public static void setup(){

        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit(){
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет пользователь!\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|user|password\n" +
                "Существующие команды:\n" +
                "\tconnect|database|user|password - \n" +
                "\t\tсоединение с базой данных\n" +
                "\ttables - \n" +
                "\t\tвывод списка всех таблиц\n" +
                "\tclear|tableName - \n" +
                "\t\tочистка содержимого таблицы tableName\n" +
                "\tfind|tableName - \n" +
                "\t\tвывод содержимого таблицы tableName\n" +
                "\tdrop|tableName - \n" +
                "\t\tудалить таблицу tableName\n" +
                "\tcreateTable|tableName|column1|column2|...|columnN - \n" +
                "\t\tсоздать таблицу tableName с колонками column1...columnN \n" +
                "\texit - \n" +
                "\t\tвыход из программы\n" +
                "Введи команду или 'help' для помощи:\n" +
                "Программа завершила работу", getData().toString().trim().replace("\r",""));
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
