package model;

import model.configuration.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class JDBCDBManager implements DBManager {
    public static final String JDBC_POSTGRESQL_LOCALHOST = "jdbc:postgresql://localhost:5432/";
    private static final String JDBC_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private Connection connection;
    private ConnectionManager connectionManager;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(ConnectionManager connectionManager) throws Exception {
        try {
            this.connection = connectionManager.getConnection();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //получить соединение с БД
    @Override
    public void connect(String database, String login, String password) throws Exception {
        connectionManager = new ConnectionManager();
        if (connectionManager.isConnected()) {
            connection = connectionManager.getConnection();
        } else {
            connection = connectionManager.getConnectionWithAuthorization(database, login, password);
        }
    }

    //получить названия всех таблиц БД
    @Override
    public Set<String> getTables() throws SQLException {
        String request = "SELECT * FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='BASE TABLE'";
        Set<String> list = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(request)) {
            while (resultSet.next()) {
                list.add(resultSet.getString("table_name"));
            }
        }
        return list;
    }

    //создать таблицу с названием nameTable
    @Override
    public void createTable(String requestSql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        }
    }

    //удалить таблицу
    @Override
    public void drop(String nameTable) throws SQLException {
        String requestSql = "DROP TABLE IF EXISTS " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        }
    }

    //очистить таблицу
    @Override
    public void clear(String nameTable) throws SQLException {
        String requestSql = "DELETE FROM " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        }
    }

    //получение содержимого указанной таблицы
    @Override
    public ResultSet getResultSet(String nameTable) throws SQLException {
        String requestSql = "SELECT * FROM " + nameTable;
        Statement stmt = null;
        stmt = connection.createStatement();
        ResultSet resultSet;
        try {
            resultSet = stmt.executeQuery(requestSql);
        } catch (SQLException e) {
            throw new SQLException(String.format("Таблицы %s не существует!", nameTable));
        }
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        if (columnCount != 0) {
            return resultSet;
        } else {
            throw new SQLException("В таблице не создано ни одного атрибута!");
        }
    }

    //возвращает размер таблицы
    @Override
    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            rsCount.next();
            return rsCount.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    //возвращает массив значений ширины каждого аттрибута
    @Override
    public ArrayList<Integer> getWidthAtribute(String nameTable) throws SQLException {
        ResultSet resultSet = getResultSet(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        //создаю массив, который содержит размеры (ширину) всех атрибутов таблицы
        //атрибуты нумеруются в БД начиная с 1, поэтом i=1
        ArrayList<Integer> arrWidthAttribute = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            int lengthColumn = resultSet.getMetaData().getColumnDisplaySize(i);
            //если ширина колонки больше 50 установить ее в 50
            if (lengthColumn > 50) {
                lengthColumn = 50;
            }
            //если ширина колонки меньше чем длинна ее заголовка - увеличить ширину
            if (lengthColumn < resultSet.getMetaData().getColumnName(i).length())
                lengthColumn = resultSet.getMetaData().getColumnName(i).length();
            //ширина колонки
            arrWidthAttribute.add(lengthColumn);
        }
        return arrWidthAttribute;
    }

    //вовзращает массив атрибутов таблицы
    @Override
    public Set<String> getAtribute(String nameTable) throws SQLException {
        ResultSet resultSet = getResultSet(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        Set<String> arrAtribute = new LinkedHashSet<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            arrAtribute.add(resultSet.getMetaData().getColumnName(i));
        }
        return arrAtribute;
    }

    //возвращает лист Датасетов содержащий данные из указанной таблицы
    @Override
    public List<DataSet> getDataSetTable(String nameTable) throws SQLException {
        ResultSet resultSet = getResultSet(nameTable);
        //размер таблицы
        int size = getSize(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<DataSet> dataSets = new LinkedList<>();
        while (resultSet.next()) {
            DataSet dataSet = new DataSetImpl();
            dataSets.add(dataSet);
            for (int i = 1; i <= columnCount; i++) {
                dataSet.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
            }
        }
        return dataSets;
    }

    //вставить данные в таблицу
    @Override
    public void insert(String insertRequestSql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertRequestSql);
        } catch (SQLException e) {
            throw new SQLException("Данные не вставлены!");
        }
    }

    //обновить данные в существующей таблице
    @Override
    public void update(String nameTable, String column1, String value1, DataSet dataSet) {
        //создаем строку запроса
        String dataRequest = "";
        Set<String> columns = dataSet.getNames();
        for (String name : columns) {
            dataRequest += name + " = '" + dataSet.get(name) + "', ";
        }
        dataRequest = dataRequest.substring(0, dataRequest.length() - 2);

        String updateRequestSql = "UPDATE " + nameTable + " SET " + dataRequest + " WHERE "
                + column1 + " = '" + value1 + "'";
        //TODO вывод на консоль не соответствует техзаданию
        //TODO - 30 records
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateRequestSql);
        } catch (SQLException e) {
            throw new RuntimeException("Данные не обновлены!");
        }
    }

    //удалить запись в таблице
    @Override
    public void delete(String nameTable, String columnName, String columnValue) throws SQLException {
        String sql = "DELETE FROM " + nameTable + " WHERE " + columnName + " = '" + columnValue + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Данные не удалены!");
        }
    }
}