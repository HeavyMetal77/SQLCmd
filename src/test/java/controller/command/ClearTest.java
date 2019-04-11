package controller.command;

import model.DBManager;
import model.DataSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;

import static junit.framework.TestCase.*;

public class ClearTest {
    private View view;
    private DBManager dbManager;
    private Command command;


    @Before
    public void setup(){
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Clear(dbManager, view);
    }

    @Test
    public void testClearTable() {

        //when
        command.process("clear|test");

        //then
        try {
            Mockito.verify(dbManager).clear("test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Mockito.verify(view).write("TABLE test was successfully clear!");
    }

    @Test
    public void testCantProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("clear|test");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("clear");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessNonexistCommand() {
        //when
        boolean canProcess = command.canProcess("cleardtdkhg|test");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithMoreThen2Parameters() {
        //when
        command.process("clear|test|morethen2");
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithLessThen2Parameters() {
        //when
        command.process("clear");
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }
}
