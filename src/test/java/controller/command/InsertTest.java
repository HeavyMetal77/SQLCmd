package controller.command;

import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class InsertTest {
    private View view;
    private Command command;
    private DBManager dbManager;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        dbManager = Mockito.mock(DBManager.class);
        command = new Insert(dbManager, view);
    }

    @Test
    public void testCanProcess() {
        //when
        boolean canProcess = command.canProcess("insert|");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() {
        //when
        command.process("insert|nameTable|column1|column2");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[Данные успешно вставлены!]", captor.getAllValues().toString());
    }

    @Test
    public void testWithWrongParameters() throws Exception {
        //when
        command.process("insert|user|name");
        //then
        verify(view).write("Количество параметров не соответствует шаблону!");
    }
}
