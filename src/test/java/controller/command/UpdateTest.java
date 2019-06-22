package controller.command;

import junit.framework.TestCase;
import model.DBManager;
import model.DataSet;
import model.DataSetImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.View;

import java.sql.SQLException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UpdateTest {
    private View view;
    private Command command;
    private DBManager dbManager;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        dbManager = Mockito.mock(DBManager.class);
        command = new Update(dbManager, view);
    }

    @Test
    public void testCanProcess() {
        //when
        boolean canProcess = command.canProcess("update|");
        //then
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() {
        //then
        Set<String> attributes = new LinkedHashSet<>(Arrays.asList("id", "name", "surname"));
        ArrayList<Integer> arrWidthAttribute = new ArrayList<>(Arrays.asList(5, 10, 20));
        DataSetImpl dataSet = new DataSetImpl();
        dataSet.put("name", "Roman");
        dataSet.put("surname", "Ivanov");
        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        try {
            when(dbManager.getAtribute("testtable")).thenReturn(attributes);
            when(dbManager.getWidthAtribute("testtable")).thenReturn(arrWidthAttribute);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //when
        command.process("update|testtable|name|Roman|surname|Petrov|");
        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[Данные успешно обновлены!, " +
                "+-------+------------+----------------------+, " +
                "+ id    + name       + surname              +, " +
                "+-------+------------+----------------------+, " +
                "+-------+------------+----------------------+]", captor.getAllValues().toString());
    }

    @Test
    public void formatCommand() {
        //when
        String format = command.formatCommand();
        //then
        TestCase.assertEquals("update|tableName|column1|value1|column2|value2", format);
    }

    @Test
    public void describeCommand() {
        //when
        String format = command.describeCommand();
        //then
        TestCase.assertEquals("Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1", format);
    }
}
