package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DBManager {
    void connect(String database, String login, String password);

    ArrayList<String> getTables();

    void createTable(String nameTable, String... nameColumns);

    void drop(String nameTable);

    void clear(String nameTable);

    ResultSet find(String nameTable);

    void insert(String nameTable, DataSet data) throws SQLException;

    //возвращает размер таблицы
    int getSize(String tableName);

    //возвращает массив значений ширины каждого аттрибута
    int[] getWidthAtribute(String nameTable) throws SQLException;

    //вовзращает массив атрибутов таблицы
    String[] getAtribute(String nameTable) throws SQLException;

    //возвращает массив Датасетов содержащий данные из указанной таблицы
    DataSet[] getDataSetTable(String nameTable) throws SQLException;

    boolean isConnected();

    void update(String nameTable, DataSet dataSet) throws SQLException;

    }
