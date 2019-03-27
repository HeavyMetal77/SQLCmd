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
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" +
                            database, login, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException("\nОшибка вводимых данных!", e);
        }
    }

    //получить названия всех таблиц БД и вывести их на консоль
    @Override
    public ArrayList<String> getTables() {
        String request = "SELECT * FROM information_schema.tables " +
                "WHERE table_schema='public' AND table_type='BASE TABLE'";
        Statement stmt = null;
        ResultSet resultSet = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(request);
            while (resultSet.next()) {
                list.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //выводим список таблиц
//        String listTables = "[";
//        for (String string : list) {
//            listTables += string + ", ";
//        }
//        listTables = listTables.substring(0, listTables.length() - 2) + "]";
//        System.out.println(listTables);
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

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(requestSql);
            stmt.close();
            System.out.println("TABLE " + nameTable + " was successfully created!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удалить таблицу
    @Override
    public void drop(String nameTable) {
        String requestSql = "DROP TABLE IF EXISTS " + nameTable;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(requestSql);
            stmt.close();
            System.out.println("TABLE " + nameTable + " was successfully deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //очистить таблицу
    @Override
    public void clear(String nameTable) {
        String requestSql = "DELETE FROM " + nameTable;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(requestSql);
            stmt.close();
            System.out.println("TABLE " + nameTable + " was successfully clear!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //получение содержимого указанной таблицы
    @Override
    public void find(String nameTable) {
        String requestSql = "SELECT * FROM " + nameTable;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(requestSql);

            if (resultSet.getMetaData().getColumnCount() != 0) {
                //рисуем верхнюю границу таблицы(+--+--+)
                printLineTable(resultSet);

                //рисуем заглавие таблицы
                printTitleTable(resultSet);

                //рисуем нижнюю границу заглавия таблицы (+--+--+)
                printLineTable(resultSet);

                //выводим содержимое кортежей таблицы
                dataCortage(resultSet);

                //рисуем нижнюю границу всей таблицы (+--+--+)
                printLineTable(resultSet);
            } else {
                System.out.println("В таблице не создано ни одного атрибута!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //выводим содержимое кортежей таблицы
    private void dataCortage(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.print("+");
            int indexColumn = 0;
            //resultSet.getMetaData().getColumnCount() - возвращает количество столбцов
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getString(++indexColumn));
                //ширина колонки
                int lengthColumn = resultSet.getMetaData().getColumnDisplaySize(indexColumn);

                //если ширина колонки больше 50 установить ее в 50
                if (lengthColumn > 50) {
                    lengthColumn = 50;
                }
                int countSpace;//кол-во пробелов
                //если значение в таблице не null
                if (resultSet.getString(indexColumn) != null) {
                    //кол-во пробелов
                    countSpace = lengthColumn - resultSet.getString(indexColumn).length();
                } else {//4 - длинна слова 'null'
                    countSpace = lengthColumn - 4;
                }
                //если в ячейке булевое значение (занимает 1 позицию)- кол-во пробелов изменяем
                if (resultSet.getMetaData().getColumnTypeName(indexColumn).equals("bool")) {
                    countSpace = resultSet.getMetaData().getColumnName(indexColumn).length() - lengthColumn;
                }
                //если кол-во пробелов больше 0
                if (countSpace > 0) {
                    System.out.print(String.format("%0" + countSpace + "d", 0).replace("0", " "));
                }
                System.out.print("+");
            }
            System.out.println();
        }
    }

    //рисуем заглавие таблицы
    private void printTitleTable(ResultSet resultSet) throws SQLException {
        System.out.print("+");
        //итерируемся по списку названий таблиц (i)
        for (int i = 0, j = 1; i < resultSet.getMetaData().getColumnCount(); i++, j++) {
            //ширина колонки
            int lengthColumn = resultSet.getMetaData().getColumnDisplaySize(j);
            //если ширина колонки больше 50 установить ее в 50
            if (lengthColumn > 50) {
                lengthColumn = 50;
            }
            System.out.print(resultSet.getMetaData().getColumnName(j));
            //остаток пробелов
            int countSpace = lengthColumn - resultSet.getMetaData().getColumnName(j).length();
            //если кол-во пробелов больше 0
            if (countSpace > 0) {
                System.out.print(String.format("%0" + countSpace + "d", 0).replace("0", " "));
            }
            System.out.print("+");
        }
        System.out.println();
    }

    //рисуем верхнюю/нижнюю границу таблицы (+--+--+)
    private void printLineTable(ResultSet resultSet) throws SQLException {
       System.out.print("+");
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            //ширина колонки
            int lengthColumn = resultSet.getMetaData().getColumnDisplaySize(i);

            //если ширина колонки больше 50 установить ее в 50
            if (lengthColumn > 50) {
                lengthColumn = 50;
            }

            //если ширина колонки меньше чем длинна ее заголовка - увеличить ширину
            if (lengthColumn < resultSet.getMetaData().getColumnName(i).length())
                lengthColumn = resultSet.getMetaData().getColumnName(i).length();

            System.out.print(String.format("%0" + lengthColumn + "d", 0).replace("0", "-"));
            System.out.print("+");
        }
        System.out.println();
    }

    @Override
    public void insert(String nameTable, DataSet [] data) {
        Statement stmt = null;
        String dataRequestColumn = "";
        String dataRequestValue = "";
        int lengthArrData = data.length;

        for (int i = 0; i < lengthArrData; i++) {
            dataRequestColumn += data[i].getName() + ", ";
        }
        dataRequestColumn = dataRequestColumn.substring(0, dataRequestColumn.length() - 2);

        for (int i = 0; i < lengthArrData; i++) {
            dataRequestValue += "'" + data[i].getValue().toString() + "'" + ", ";
        }
        dataRequestValue = dataRequestValue.substring(0, dataRequestValue.length() - 2);

        //INSERT INTO nameTable (column1, column2, ...) VALUES(value1, value2, ...);
        String insertRequestSql = "INSERT INTO " +  nameTable + " (" + dataRequestColumn + ")"
                + " VALUES (" + dataRequestValue +")";
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(insertRequestSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}
/*
update
delete
 */