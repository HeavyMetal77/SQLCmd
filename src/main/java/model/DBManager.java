package model;

import model.configuration.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface DBManager {
    //получить соединение с БД
    void connect(String database, String login, String password) throws Exception;

    //возвращает список таблиц БД
    Set<String> getTables() throws SQLException;

    //создать таблицу с названием nameTable
    void createTable(String requestSql) throws SQLException;

    //удалить таблицу с названием nameTable
    void drop(String nameTable) throws SQLException;

    //очистить таблицу с названием nameTable
    void clear(String nameTable) throws SQLException;

    //получить ResultSet у таблицы с названием nameTable
    ResultSet getResultSet(String nameTable) throws SQLException;

    //вставить данные в таблицу с названием nameTable
    void insert(String nameTable, DataSet data) throws SQLException;

    //возвращает размер таблицы
    int getSize(String tableName) throws SQLException;

    //возвращает массив значений ширины каждого аттрибута
    int[] getWidthAtribute(String nameTable) throws SQLException;

    //вовзращает массив атрибутов таблицы
    String[] getAtribute(String nameTable) throws SQLException;

    //возвращает массив Датасетов содержащий данные из указанной таблицы
    DataSet[] getDataSetTable(String nameTable) throws SQLException;

    //обновить данные в таблице с названием nameTable
    void update(String nameTable, DataSet dataSet) throws SQLException;

    //установить connection из ConnectionManager
    void setConnection(ConnectionManager connectionManager) throws Exception;

    //получить connection
    Connection getConnection();
}
