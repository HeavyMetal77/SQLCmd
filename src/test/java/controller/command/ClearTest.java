package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.verify;

public class ClearTest {
    private DBManager dbManager;
    private Command command;
    private View view;

    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Clear(dbManager, view);
    }

    @Test
    public void testClearTable() {
        //when
        command.process("clear|test");
        //then
        verify(dbManager).clear("test");
        verify(view).write("Таблица test была успешно очищена!");
    }

    @Test
    public void testCanProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("clear|test");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("clear");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessNonexistCommand() {
        //when
        boolean canProcess = command.canProcess("cleardtdkhg|test");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithMoreThen2Parameters() {
        //when
        command.process("clear|test|morethen2");
        verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithLessThen2Parameters() {
        //when
        command.process("clear");
        //then
        verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        assertEquals("clear|tableName", format);
    }

    @Test
    public void describeCommand() {
        //when
        String format = command.describeCommand();
        //then
        assertEquals("Очистка содержимого таблицы 'tableName'", format);
    }
}
