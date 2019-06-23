package model;

import model.configuration.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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

    //возвращает размер таблицы
    int getSize(String tableName) throws SQLException;

    //возвращает массив значений ширины каждого аттрибута
    List<Integer> getWidthAtribute(String nameTable) throws SQLException;

    //вовзращает массив атрибутов таблицы
    Set<String> getAtribute(String nameTable) throws SQLException;

    //возвращает массив Датасетов содержащий данные из указанной таблицы
    List<DataSet> getDataSetTable(String nameTable) throws SQLException;

    //вставить данные в таблицу
    void insert(String insertRequestSql) throws SQLException;

    //обновить данные в таблице с названием nameTable
    void update(String nameTable, String column1, String value1, DataSet dataSet) throws SQLException;

    //установить connection из ConnectionManager
    void setConnection(ConnectionManager connectionManager) throws Exception;

    //получить connection
    Connection getConnection();

    //удаление записи в таблице
    void delete(String nameTable, String columnName, String columnValue) throws SQLException;

    //закрыть Connection
    void closeOpenedConnection();

    //создать базу данных
    void createDatabase(String databaseName);

    //получить список всех баз данных
    Set<String> getDatabases();

    //удалить базу данных
    void dropDatabase(String databaseName);
}
