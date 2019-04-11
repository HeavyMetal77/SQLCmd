package model;

import java.sql.*;
import java.util.ArrayList;

public class JDBCDBManager implements DBManager {
    private Connection connection;

    //получить соединение с БД
    @Override
    public void connect(String database, String login, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path or dependencies Maven!");
        }
        try {
            if (connection != null) {
                connection = null;
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" +
                            database, login, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("Ошибка вводимых данных!", e);
        }
    }

    //получить названия всех таблиц БД
    @Override
    public ArrayList<String> getTables() {
        String request = "SELECT * FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='BASE TABLE'";
        ArrayList<String> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(request)) {
            while (resultSet.next()) {
                list.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //создать таблицу с названием nameTable
    @Override
    public void createTable(String nameTable, String... nameColumns) {
        String requestSql = "CREATE TABLE IF NOT EXISTS " +
                nameTable + " (ID INT PRIMARY KEY NOT NULL,";
        String textNameColumn = "";
        for (String text : nameColumns) {
            textNameColumn += " " + text + " TEXT NOT NULL,";
        }

        textNameColumn = textNameColumn.substring(0, (textNameColumn.length() - 1));
        requestSql += textNameColumn + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удалить таблицу
    @Override
    public void drop(String nameTable) {
        String requestSql = "DROP TABLE IF EXISTS " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //очистить таблицу
    @Override
    public void clear(String nameTable) {
        String requestSql = "DELETE FROM " + nameTable;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(requestSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //получение содержимого указанной таблицы
    @Override
    public ResultSet find(String nameTable) {
        String requestSql = "SELECT * FROM " + nameTable;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(requestSql);
            //количество атрибутов таблицы
            int columnCount = resultSet.getMetaData().getColumnCount();
            if (columnCount != 0) {
                return resultSet;
            } else {
                throw new RuntimeException("В таблице не создано ни одного атрибута!");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException("Таблицы не существует!");
        }
    }

    //возвращает размер таблицы
    @Override
    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        } catch (SQLException e) {
            return 0;
        }
    }

    //возвращает массив значений ширины каждого аттрибута
    @Override
    public int[] getWidthAtribute(String nameTable) throws SQLException {
        ResultSet resultSet = find(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        //создаю массив, который содержит размеры (ширину) всех атрибутов таблицы
        //атрибуты нумеруются в БД начиная с 1, поэтом i=1
        int[] arrWidthAttribute = new int[columnCount];
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
            arrWidthAttribute[i - 1] = lengthColumn;
        }
        return arrWidthAttribute;
    }

    //вовзращает массив атрибутов таблицы
    @Override
    public String[] getAtribute(String nameTable) throws SQLException {
        ResultSet resultSet = find(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();
        String[] arrAtribute = new String[columnCount];
        for (int i = 0, j = 1; i < columnCount; i++, j++) {
            arrAtribute[i] = resultSet.getMetaData().getColumnName(j);
        }
        return arrAtribute;
    }

    //возвращает массив Датасетов содержащий данные из указанной таблицы
    @Override
    public DataSet[] getDataSetTable(String nameTable) throws SQLException {
        ResultSet resultSet = find(nameTable);
        //размер таблицы
        int size = getSize(nameTable);
        //количество атрибутов таблицы
        int columnCount = resultSet.getMetaData().getColumnCount();

        DataSet[] dataSets = new DataSet[size];
        int countDataset = 0;
        while (resultSet.next()) {
            DataSet dataSet = new DataSet();
            dataSets[countDataset++] = dataSet;
            for (int i = 1; i <= columnCount; i++) {
                dataSet.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
            }
        }
        return dataSets;
    }

    //вставить данные в таблицу
    @Override
    public void insert(String insertRequestSql, DataSet dataSet) throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertRequestSql);
        } catch (SQLException e) {
            throw new SQLException("Данные не вставлены!");
        }
    }

    //проверить наличие соединения
    @Override
    public boolean isConnected() {
        return connection != null;
    }

    //обновить данные в существующей таблице
    @Override
    public void update(String nameTable, DataSet dataSet) {
        //создаем строку запроса
        String dataRequest = "";
        int lengthArrData = dataSet.getNames().length;
        for (int i = 0; i < lengthArrData; i++) {
            dataRequest += dataSet.getNames()[i] + " = " + dataSet.getValues()[i] + ", ";
        }
        dataRequest = dataRequest.substring(0, dataRequest.length() - 2);

        //UPDATE table_name SET column1 = value1, column2 = value2...., columnN = valueN
        //WHERE [condition];
        String insertRequestSql = "UPDATE " + nameTable + " SET " + dataRequest;
        //TODO отсутствуют условия обновления WHERE [condition] (обновляются все записи с такими значениями)
        //TODO вывод на консоль не соответствует техзаданию
        //TODO - 30 records
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertRequestSql);
        } catch (SQLException e) {
            throw new RuntimeException("Данные не обновлены!");
        }
    }
}
/*
update
delete
 */