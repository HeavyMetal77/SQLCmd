package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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
}
