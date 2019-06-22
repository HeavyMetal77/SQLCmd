package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.verify;

public class DropTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Drop(dbManager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("drop|tableName");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("drop");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessNonExistCommand() {
        //when
        boolean canProcess = command.canProcess("droptdkhg|tableName");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        assertEquals("drop|tableName", format);
    }

    @Test
    public void describeCommand() {
        String format = command.describeCommand();
        assertEquals("Удалить таблицу 'tableName'", format);
    }

    @Test
    public void testDropTableSuccessful() {
        //when
        command.process("drop|contact");
        try {
            verify(dbManager).drop("contact");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //then
        verify(view).write("Таблица contact была успешно удалена!");
    }

    @Test
    public void testDropTableWrongParameters() {
        //when
        command.process("drop|contact|value");
        //then
        verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testDropTableNotEnoughParameters() {
        //when
        command.process("drop|");
        //then
        verify(view).write("Количество параметров не соответствует шаблону!");
    }
}
