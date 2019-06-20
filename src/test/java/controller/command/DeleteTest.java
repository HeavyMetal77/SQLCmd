package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithWrongNameTable() {
        //when
        command.process("delete|nonExistTableName|columnName|columnValue");
        Mockito.verify(view).write("Удаление не произведено!");
    }
}
