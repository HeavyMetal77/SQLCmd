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

    public void setConnection(ConnectionManager connectionManager) {
        try {
            this.connection = connectionManager.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //получить соединение с БД
    @Override
    public void connect(String database, String login, String password) {
        connectionManager = new ConnectionManager();
        try {
            if (connectionManager.isConnected()) {
                connection = connectionManager.getConnection();
            } else {
                connection = connectionManager.getConnectionWithAuthorization(database, login, password);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void closeOpenedConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
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
    public Set<String> getTables() {
        String request = "SELECT * FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='BASE TABLE'";
        Set<String> list = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(request)) {
            while (resultSet.next()) {
                list.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    //создать таблицу с названием nameTable
    @Override
    public void createTable(String requestSql, String nameTable) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Ошибка создания таблицы %s, по причине: %s", nameTable, e.getMessage()));
        }
    }

    //удалить таблицу
    @Override
    public void drop(String nameTable) {
        String requestSql = "DROP TABLE IF EXISTS " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Ошибка удаления таблицы %s, по причине: %s",
                    nameTable, e.getMessage()));
        }
    }

    //очистить таблицу
    @Override
    public void clear(String nameTable) {
        String requestSql = "DELETE FROM " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e.getMessage());
        }
    }

    //возвращает список значений ширины каждого аттрибута
    @Override
    public List<Integer> getWidthAtribute(String nameTable) {
        List<DataSet> dataSets = getDataSetTable(nameTable);
        List<Integer> arrWidthAttribute = new ArrayList<>();
        if (!dataSets.isEmpty()) {
            Set<String> names = dataSets.get(0).getNames();
            for (String name : names) {
                arrWidthAttribute.add(name.length());
            }
            for (DataSet dataSet : dataSets) {
                List<Object> values = dataSet.getValues();
                for (int j = 0; j < arrWidthAttribute.size(); j++) {
                    int lengthAttr = arrWidthAttribute.get(j);
                    if (values.get(j) == null) {
                        if (lengthAttr < 4) {
                            lengthAttr = 4;
                        }
                    } else {
                        int lengthValue = values.get(j).toString().length();
                        if (lengthAttr < lengthValue) {
                            lengthAttr = lengthValue;
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
    public Set<String> getAtribute(String nameTable) {
        Set<String> arrAtribute;
        String requestSql = "SELECT * FROM " + nameTable;
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(requestSql);) {
            //количество атрибутов таблицы
            int columnCount = resultSet.getMetaData().getColumnCount();
            arrAtribute = new LinkedHashSet<>(columnCount);
            if (columnCount != 0) {
                for (int i = 1; i <= columnCount; i++) {
                    arrAtribute.add(resultSet.getMetaData().getColumnName(i));
                }
            } else {
                throw new RuntimeException("В таблице не создано ни одного атрибута!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Таблицы %s не существует!", nameTable));
        }
        return arrAtribute;
    }

    //возвращает лист Датасетов содержащий данные из указанной таблицы
    @Override
    public List<DataSet> getDataSetTable(String nameTable) {
        List<DataSet> dataSets = null;
        String requestSql = "SELECT * FROM " + nameTable;
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(requestSql);) {
            //количество атрибутов таблицы
            int columnCount = resultSet.getMetaData().getColumnCount();
            dataSets = new LinkedList<>();
            while (resultSet.next()) {
                DataSet dataSet = new DataSetImpl();
                dataSets.add(dataSet);
                for (int i = 1; i <= columnCount; i++) {
                    dataSet.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Таблицы %s не существует!", nameTable));
        }
        return dataSets;
    }

    //вставить данные в таблицу
    @Override
    public void insert(String nameTable, DataSet dataSet) {
        Set<String> tables = getTables();
        if (!tables.contains(nameTable.toLowerCase())) {
            throw new RuntimeException(String.format("Таблицы '%s' не существует! Проверьте правильность названия таблицы!", nameTable));
        }
        StringBuilder requestColumn = new StringBuilder("");
        StringBuilder requestValue = new StringBuilder("");
        Set<String> columns = dataSet.getNames();
        for (String name : columns) {
            requestColumn.append(name).append(", ");
            requestValue.append("'").append(dataSet.get(name)).append("'").append(", ");
        }
        String request = "INSERT INTO " + nameTable + " (" + requestColumn.substring(0, requestColumn.length() - 2) + ")"
                + " VALUES (" + requestValue.substring(0, requestValue.length() - 2) + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(request);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Данные в таблицу '%s' не вставлены, по причине: %s",
                    nameTable, e.getMessage()));
        }
    }

    //обновить данные в существующей таблице
    @Override
    public void update(String nameTable, String column, String value, DataSet dataSet) {
        //создаем строку запроса
        String request = "";
        Set<String> columns = dataSet.getNames();
        for (String name : columns) {
            request += name + " = '" + dataSet.get(name) + "', ";
        }
        request = request.substring(0, request.length() - 2);
        String updateRequest = "UPDATE " + nameTable + " SET " + request + " WHERE "
                + column + " = '" + value + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateRequest);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Ошибка обновления данных в таблице %s, " +
                    "по причине: %s", nameTable, e.getMessage()));
        }
    }

    //удалить запись в таблице
    @Override
    public void delete(String nameTable, String columnName, String columnValue) {
        String sql = "DELETE FROM " + nameTable + " WHERE " + columnName + " = '" + columnValue + "'";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Ошибка удаления записи в таблице %s, по причине: %s",
                    nameTable, e.getMessage()));
        }
    }
}