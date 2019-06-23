package model;

import model.configuration.ConnectionManager;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public interface DBManager {
    //получить соединение с БД
    void connect(String database, String login, String password);

    //возвращает список таблиц БД
    Set<String> getTables();

    //создать таблицу с названием nameTable
    void createTable(String requestSql);

    //удалить таблицу с названием nameTable
    void drop(String nameTable);

    //очистить таблицу с названием nameTable
    void clear(String nameTable);

    //возвращает размер таблицы
    int getSize(String tableName);

    //возвращает массив значений ширины каждого аттрибута
    List<Integer> getWidthAtribute(String nameTable);

    //вовзращает массив атрибутов таблицы
    Set<String> getAtribute(String nameTable);

    //возвращает массив Датасетов содержащий данные из указанной таблицы
    List<DataSet> getDataSetTable(String nameTable);

    //вставить данные в таблицу
    void insert(String nameTable, DataSet dataSet);

    //обновить данные в таблице с названием nameTable
    void update(String nameTable, String column1, String value1, DataSet dataSet);

    //установить connection из ConnectionManager
    void setConnection(ConnectionManager connectionManager);

    //получить connection
    Connection getConnection();

    //удаление записи в таблице
    void delete(String nameTable, String columnName, String columnValue);

    //закрыть Connection
    void closeOpenedConnection();

    //создать базу данных
    void createDatabase(String databaseName);

    //получить список всех баз данных
    Set<String> getDatabases();

    //удалить базу данных
    void dropDatabase(String databaseName);
}
