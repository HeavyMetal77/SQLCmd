package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ConnectTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Connect(dbManager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("connect|sqlcmd|sqlcmd|sqlcmd");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("connect");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessNonExistCommand() {
        //when
        boolean canProcess = command.canProcess("connectdtdkhg|test");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithMoreThenNeedParameters() {
        //when
        command.process("connect|sqlcmd|sqlcmd|sqlcmd|sqlcmd");
        Mockito.verify(view).write("Неверное количество параметров: ожидается: 4, введено: 5");
    }

    @Test
    public void testProcessWithLessThenNeedParameters() {
        //when
        command.process("connect|sqlcmd");
        Mockito.verify(view).write("Неверное количество параметров: ожидается: 4, введено: 2");
    }

}
