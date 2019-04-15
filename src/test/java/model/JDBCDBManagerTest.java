package model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class JDBCDBManagerTest {
    private JDBCDBManager manager;

    @Before
    public void setup() {
        manager = new JDBCDBManager();
        try {
            manager.connect("sqlcmd", "sqlcmd", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllTableNames() throws SQLException {
        ArrayList<String> tablesNames = manager.getTables();
        assertEquals("[category, test1, contact_type, contact_value, contact, test2, test]",
                tablesNames.toString());
    }


}
