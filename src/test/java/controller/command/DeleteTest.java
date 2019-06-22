package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.*;

public class DeleteTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Delete(dbManager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("delete|tableName|columnName|columnValue");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("delete");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessNonExistCommand() {
        //when
        boolean canProcess = command.canProcess("deletedtdkhg|tableName|columnName|columnValue");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithLessThenNeedParameters() {
        //when
        command.process("delete|tableName|columnName");
        //then
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithWrongNameTable() {
        //when
        command.process("delete|nonExistTableName|columnName|columnValue");
        //then
        Mockito.verify(view).write("Удаление не произведено!");
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        assertEquals("delete|tableName|columnName|columnValue", format);
    }

    @Test
    public void describeCommand() {
        //when
        String format = command.describeCommand();
        //then
        assertEquals("Удаление записи в таблице 'tableName', где columnName = columnValue", format);
    }
}
