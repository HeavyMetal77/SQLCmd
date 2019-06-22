package controller.command;

import model.JDBCDBManager;
import org.junit.Before;
import org.junit.Test;
import view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateDatabaseTest {
    private JDBCDBManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(JDBCDBManager.class);
        view = mock(View.class);
        command = new CreateDatabase(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        //when
        boolean canProcess = command.canProcess("createDB|databaseName");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        //when
        boolean canProcess = command.canProcess("createDBError|baseName");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("createDB|");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() {
        //when
        command.process("createDB|databaseName");
        //then
        verify(manager).createDatabase("databaseName");
        verify(view).write("База данных 'databaseName' успешно создана.");
    }

    @Test
    public void testProcessWrongParameters() {
        //when
        command.process("createDB|databaseName|wrong");
        //then
        verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testNameStartWithWrongName() throws Exception {
        //when
        command.process("createDB|12databaseName");
        //then
        verify(view).write("База данных не может называться '12databaseName'!");
    }
}
