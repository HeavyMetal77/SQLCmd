package controller.command;

import junit.framework.TestCase;
import model.DBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TablesTest {
    private View view;
    private Command command;
    private DBManager dbManager;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        dbManager = Mockito.mock(DBManager.class);
        command = new Tables(dbManager, view);
    }

    @Test
    public void testCanProcess() {
        //when
        boolean canProcess = command.canProcess("tables");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() {
        //given
        try {
            Set<String> listTables = new LinkedHashSet<>();
            listTables.add("test1");
            listTables.add("test2");
            when(dbManager.getTables()).thenReturn(listTables);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //when
        command.process("tables");
        //then
        shouldPrint("[База данных содержит таблицы: test1, test2]");
    }

    @Test
    public void testPrintEmptyTableData() {
        try {
            when(dbManager.getTables()).thenReturn(new LinkedHashSet<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        command.process(("tables"));
        shouldPrint("[В базе данных таблицы отсутствуют]");
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        TestCase.assertEquals("tables", format);
    }

    @Test
    public void describeCommand() {
        //when
        String format = command.describeCommand();
        //then
        TestCase.assertEquals("Вывод списка всех таблиц", format);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
