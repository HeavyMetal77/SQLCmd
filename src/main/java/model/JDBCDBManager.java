package model;

import model.configuration.ConnectionManager;

import java.sql.*;
import java.util.*;

public class JDBCDBManager implements DBManager {
    private Connection connection;
    private ConnectionManager connectionManager;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(ConnectionManager connectionManager) throws Exception {
            this.connection = connectionManager.getConnection();
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

    public void closeOpenedConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    //создать базу данных
    @Override
    public void createDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + databaseName);
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public Set<String> getDatabases() {
        Set<String> list = new LinkedHashSet<>();

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT datname FROM pg_database WHERE datistemplate = false;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            list = null;
        }
        return list;
    }

    //удаление базы данных
    @Override
    public void dropDatabase(String databaseName) {
        Set<String> listDatabases = getDatabases();
        if (listDatabases.contains(databaseName)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP DATABASE " + databaseName);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage() + " База данных не удалена! Перед удалением, закройте все соединения с ней!");
            }
        } else {
            throw new RuntimeException("Базы данных с таким названием не существует!");
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
    public int getSize(String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            rsCount.next();
            return rsCount.getInt(1);
        }
    }

    //возвращает список значений ширины каждого аттрибута
    @Override
    public ArrayList<Integer> getWidthAtribute(String nameTable) throws SQLException {
        List<DataSet> dataSets = getDataSetTable(nameTable);
        ArrayList<Integer> arrWidthAttribute = new ArrayList<>();
        if (!dataSets.isEmpty()) {
            Set<String> names = dataSets.get(0).getNames();
            for (String name : names) {
                arrWidthAttribute.add(name.length());
            }
            for (int i = 0; i < dataSets.size(); i++) {
                List<Object> values = dataSets.get(i).getValues();
                for (int j = 0; j < arrWidthAttribute.size(); j++) {
                    int lengthAttr = arrWidthAttribute.get(j);
                    if (!(values.get(j) == null)) {
                        int lengthValue = values.get(j).toString().length();
                        if (lengthAttr < lengthValue) {
                            lengthAttr = lengthValue;
                        }
                    } else {
                        if (lengthAttr < 4) {
                            lengthAttr = 4;
                        }
                    }
                    arrWidthAttribute.set(j, lengthAttr);
                }
            }
        } else {
            Set<String> atribute = getAtribute(nameTable);
            for (String atr : atribute) {
                arrWidthAttribute.add(atr.length());
            }
        }
        return arrWidthAttribute;
    }

    //вовзращает множество (список) атрибутов таблицы
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
        }
    }

    //обновить данные в существующей таблице
    @Override
    public void update(String nameTable, String column1, String value1, DataSet dataSet) throws SQLException {
        //создаем строку запроса
        String dataRequest = "";
        Set<String> columns = dataSet.getNames();
        for (String name : columns) {
            dataRequest += name + " = '" + dataSet.get(name) + "', ";
        }
        dataRequest = dataRequest.substring(0, dataRequest.length() - 2);

        String updateRequestSql = "UPDATE " + nameTable + " SET " + dataRequest + " WHERE "
                + column1 + " = '" + value1 + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateRequestSql);
        }
    }

    //удалить запись в таблице
    @Override
    public void delete(String nameTable, String columnName, String columnValue) throws SQLException {
        String sql = "DELETE FROM " + nameTable + " WHERE " + columnName + " = '" + columnValue + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}