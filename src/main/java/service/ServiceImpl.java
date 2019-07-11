package service;

import model.DBManager;
import model.DataSet;
import model.JDBCDBManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServiceImpl implements Service {
    private DBManager dbManager;

    public ServiceImpl() {
        dbManager = new JDBCDBManager();
    }

    @Override
    public List<String> commands() {
        List<String> list = new ArrayList<>();
        list.add("connect");
        list.add("help");
        list.add("menu");
        list.add("find");
        list.add("clear");
        list.add("delete");
        return list;
    }

    @Override
    public Connection connect(String databaseName, String userName, String password) {
        return dbManager.connect(databaseName, userName, password);
    }

    @Override
    public List<List<String>> find(String nameTable) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(dbManager.getAtribute(nameTable));
        List<DataSet> tableData = dbManager.getDataSetTable(nameTable);
        result.add(columns);
        for (DataSet dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                Object value = dataSet.get(column);
                if (value != null) {
                    row.add(value.toString());
                } else {
                    row.add("null");
                }
            }
        }
        return result;
    }

    @Override
    public void clear(String nameTable) {
        dbManager.clear(nameTable);
    }

    @Override
    public void delete(String nameTable, String columnName, String columnValue) {
        dbManager.delete(nameTable, columnName, columnValue);
    }
}
