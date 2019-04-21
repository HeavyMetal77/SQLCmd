package controller.command;

import model.DBManager;
import model.DataSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

public class FindTest {
    private View view;
    private DBManager dbManager;
    private Command command;


    @Before
    public void setup() {
        dbManager = Mockito.mock(DBManager.class);
        view = Mockito.mock(View.class);
        command = new Find(dbManager, view);
    }

    @Test
    public void testPrintTableData() {
        //given
        String nameTable = "test";

        DataSet field1 = new DataSet();
        field1.put("id", 1);
        field1.put("nametest2", "supertest");
        field1.put("field1", "null");

        DataSet field2 = new DataSet();
        field2.put("id", 2);
        field2.put("nametest2", "supertest2");
        field2.put("field1", "null");

        Set<String> atributes = new LinkedHashSet<String>(Arrays.asList("id", "nametest2", "field1"));
        try {
            Mockito.when(dbManager.getWidthAtribute(nameTable)).thenReturn(new int[]{10, 50, 50});
            Mockito.when(dbManager.getSize(nameTable)).thenReturn(2);
            Mockito.when(dbManager.getDataSetTable(nameTable)).thenReturn(new DataSet[]{field1, field2});
            Mockito.when(dbManager.getAtribute(nameTable)).thenReturn(atributes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //when
        command.process("find|test");

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals("[+----------+--------------------------------------------------+--------------------------------------------------+, " +
                        "+id        +nametest2                                         +field1                                            +, " +
                        "+----------+--------------------------------------------------+--------------------------------------------------+, " +
                        "+1         +supertest                                         +null                                              +, " +
                        "+2         +supertest2                                        +null                                              +, " +
                        "+----------+--------------------------------------------------+--------------------------------------------------+]",
                captor.getAllValues().toString());
    }

    @Test
    public void testCantProcessWithParameters() {
        //when
        boolean canProcess = command.canProcess("find|contact");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWithoutParameters() {
        //when
        boolean canProcess = command.canProcess("find");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessWithMoreThen2Parameters() {
        //when
        command.process("find|test|morethen2");
        //then
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testProcessWithLessThen2Parameters() {
        //when
        command.process("find");
        //then
        Mockito.verify(view).write("Количество параметров не соответствует шаблону!");
    }

    @Test
    public void testCantProcessNonexistCommand() {
        //when
        boolean canProcess = command.canProcess("findkhg|contact");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintTableDataFromEmptyTable() {
        //given
        String nameTable = "test";

        DataSet[] dataSets = new DataSet[0];
        Set<String> atributes = new LinkedHashSet<String>(Arrays.asList("id", "nametest2", "field1"));
        try {
            Mockito.when(dbManager.getWidthAtribute(nameTable)).thenReturn(new int[]{5, 20, 20});
            Mockito.when(dbManager.getSize(nameTable)).thenReturn(0);
            Mockito.when(dbManager.getDataSetTable(nameTable)).thenReturn(dataSets);
            Mockito.when(dbManager.getAtribute(nameTable)).thenReturn(atributes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //when
        command.process("find|test");

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, Mockito.atLeastOnce()).write(captor.capture());
        assertEquals(
                "[+-----+--------------------+--------------------+, " +
                        "+id   +nametest2           +field1              +, " +
                        "+-----+--------------------+--------------------+, " +
                        "+-----+--------------------+--------------------+]",
                captor.getAllValues().toString());
    }
}
