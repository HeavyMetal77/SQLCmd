package controller.command;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class ExitUnitTest {
    private TestView view;
    Command command;

    @Before
    public void setup() {
        view = new TestView();
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExit() {
        //given

        //when
        boolean canProcess = command.canProcess("exit");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessExitFailCommand() {
        //given

        //when
        boolean canProcess = command.canProcess("qwerty");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessExit_throwsExitException() {
        //given

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            //then
            //throws ExitException
            assertEquals("Программа завершила работу\n", view.getContent());
        }
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        assertEquals("exit", format);
    }

    @Test
    public void describeCommand() {
        //when
        String format = command.describeCommand();
        //then
        assertEquals("Выход из программы", format);
    }
}
