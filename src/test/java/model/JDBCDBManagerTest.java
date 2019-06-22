package model;

import model.configuration.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
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
            manager.connect("sqlcmd", "sqlcmd", "");
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
        try {
            when(manager.getSize("contact")).thenReturn(5);
            sizeTable = manager.getSize("contact");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //then
        assertEquals(sizeTable, 5);
    }

    @Test
    public void testGetSizeTable() {
        //given
        int sizeTable = 0;
        //when
        try {
            sizeTable = manager.getSize("testGetSizeTable");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //then
        assertEquals(sizeTable, 2);

        //when
        try {
            manager.drop("testGetSizeTable");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllTableNames() {
        Set<String> tablesNames = null;
        try {
            tablesNames = manager.getTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("[category, contact_type, contact_value, contact, testgetsizetable]",
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
