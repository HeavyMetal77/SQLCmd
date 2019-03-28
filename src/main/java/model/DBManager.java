package model;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DBManager {
    void connect(String database, String login, String password);

    ArrayList<String> getTables();

    void createTable(String nameTable, String... nameColumns);

    void drop(String nameTable);

    void clear(String nameTable);

    void find(String nameTable);


    void insert(String nameTable, DataSet[] data) throws SQLException;

    boolean isConnected();
}
