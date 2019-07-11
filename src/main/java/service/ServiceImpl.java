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
        return list;
    }

    @Override
    public Connection connect(String databaseName, String userName, String password) {
        return dbManager.connect(databaseName, userName, password);
    }

    @Override
    public List<List<String>> find(String databaseName) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(dbManager.getAtribute(databaseName));
        List<DataSet> tableData = dbManager.getDataSetTable(databaseName);
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
    public void clear(String databaseName) {
        dbManager.clear(databaseName);
    }
}
