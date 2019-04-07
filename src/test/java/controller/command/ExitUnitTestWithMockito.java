package controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.*;

public class ExitUnitTestWithMockito {
    private View view = Mockito.mock(View.class);

    @Test
    public void testCanProcessExit() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }


    @Test
    public void testCantProcessExitFailCommand() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("qwerty");

        //then
        assertFalse(canProcess);
    }


    @Test
    public void testCanProcessExit_throwsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {

        }

        //then
        Mockito.verify(view).write("Программа завершила работу");
        //throws ExitException
    }
}
