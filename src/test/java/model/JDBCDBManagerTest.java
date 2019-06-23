package model;

import model.configuration.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCDBManagerTest {
    private JDBCDBManager manager;
    private Connection connection;

    @Before
    public void setup() {
        manager = new JDBCDBManager();
        try {
            manager.connect("databasetest", "sqlcmd", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSizeTableMock() {
        //given
        manager = mock(JDBCDBManager.class);
        int sizeTable = 0;
        //when
        when(manager.getSize("contact")).thenReturn(5);
        sizeTable = manager.getSize("contact");
        //then
        assertEquals(sizeTable, 5);
    }

    @Test
    public void testGetSizeTable() {
        //given
        int sizeTable = 0;
        //when
        sizeTable = manager.getSize("testGetSizeTable");
        //then
        assertEquals(sizeTable, 2);
        manager.drop("testGetSizeTable");
    }

    @Test
    public void testGetAllTableNames() {
        //when
        Set<String> tablesNames = manager.getTables();
        //then
        assertEquals("[testgetsizetable]",
                tablesNames.toString());
    }

    @Test
    public void testSetConnection() {
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        try {
            manager.setConnection(connectionManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(connection);
    }
}
