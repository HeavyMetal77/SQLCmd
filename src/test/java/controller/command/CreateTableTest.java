package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CreateTableTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new CreateTable(dbManager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("createTable|tableName|column1|column2");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("createTable");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessNonExistCommand() {
        //when
        boolean canProcess = command.canProcess("createTabledtdkhg|tableName|column1|column2");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithLessThenNeedParameters() {
        //when
        command.process("createTabledtdkhg|tableName");
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithWrongNameTable() {
        //when
        command.process("createTable|1tableName|column1|column2");
        Mockito.verify(view).write("Таблица не может называться 1tableName!");
        Mockito.verify(view).write("Имя таблицы должно начинаться только с буквы, длинной не меньше 3 символов!");
    }

    @Test
    public void testProcessWithWrongNameTableNotEnoughLetters() {
        //when
        command.process("createTable|ta|column1|column2");
        Mockito.verify(view).write("Таблица не может называться ta!");
        Mockito.verify(view).write("Имя таблицы должно начинаться только с буквы, длинной не меньше 3 символов!");
    }
}
