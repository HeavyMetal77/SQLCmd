package model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class DataSetImplTest {
    DataSetImpl dataSet;
    List<DataSet> dataSets;

    @Before
    public void setup() {
        dataSet = mock(DataSetImpl.class);
        dataSets = Mockito.mock(ArrayList.class);
    }

    @Test
    public void testDataSetImpl() {
        //when
        dataSet.put("name", "Roman");
        dataSet.put("surname", "Ivanov");

        dataSets.add(dataSet);
        //then
        when(dataSet.get("name")).thenReturn("Roman");
        verify(dataSet).put("name", "Roman");
    }
}
