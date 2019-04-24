package controller.command;

import model.DBManager;
import model.DataSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;
import java.util.*;

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
        LinkedList<DataSet> dataSets = new LinkedList<>();
        dataSets.add(field1);
        dataSets.add(field2);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(50);
        list.add(50);

        Set<String> attributes = new LinkedHashSet<String>(Arrays.asList("id", "nametest2", "field1"));
        try {
            Mockito.when(dbManager.getWidthAtribute(nameTable)).thenReturn(list);
            Mockito.when(dbManager.getSize(nameTable)).thenReturn(2);
            Mockito.when(dbManager.getDataSetTable(nameTable)).thenReturn(dataSets);
            Mockito.when(dbManager.getAtribute(nameTable)).thenReturn(attributes);
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

        LinkedList<DataSet> dataSets = new LinkedList<>();
        Set<String> attributes = new LinkedHashSet<String>(Arrays.asList("id", "nametest2", "field1"));

        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(20);
        list.add(20);

        try {
            Mockito.when(dbManager.getWidthAtribute(nameTable)).thenReturn(list);
            Mockito.when(dbManager.getSize(nameTable)).thenReturn(0);
            Mockito.when(dbManager.getDataSetTable(nameTable)).thenReturn(dataSets);
            Mockito.when(dbManager.getAtribute(nameTable)).thenReturn(attributes);
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
