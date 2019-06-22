package controller.command;

import model.JDBCDBManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import view.View;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabasesTest {
    private JDBCDBManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(JDBCDBManager.class);
        view = mock(View.class);
        command = new Databases(manager, view);
    }

    @Test
    public void testGetDatabaseNames() {
        //when
        when(manager.getDatabases()).thenReturn(new HashSet<>(Arrays.asList("DataBaseTest1", "DataBaseTest2")));
        command.process("databases");
        //then
        shouldPrint("[Существующие базы данных: DataBaseTest1, DataBaseTest2]");
    }

    @Test
    public void canProcess() {
        //when
        boolean canProcess = command.canProcess("databases");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void canProcessError() {
        //when
        boolean canProcessFalse = command.canProcess("DBWrong");
        //then
        assertFalse(canProcessFalse);
    }

    @Test
    public void canProcessEmptyCommand() {
        //when
        boolean canProcessFalse = command.canProcess("");
        //then
        assertFalse(canProcessFalse);
    }

    @Test
    public void testPrintZeroDatabases() {
        //when
        when(manager.getDatabases()).thenReturn(new HashSet<>());
        command.process(("databases"));
        //then
        shouldPrint("[Базы данных отсутствуют.]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
