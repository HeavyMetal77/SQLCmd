package controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.View;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class HelpTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Help(view, new ArrayList<>());
    }

    @Test
    public void testCanProcess() {
        //when
        boolean canProcess = command.canProcess("help");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWrongCommand() {
        //when
        boolean canProcess = command.canProcess("helps");
        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcess() {
        //when
        command.process("help");
        //then
        Mockito.verify(view).write("Существующие команды:");
    }
}
