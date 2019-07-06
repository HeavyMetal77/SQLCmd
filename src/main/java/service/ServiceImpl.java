package service;

import model.DBManager;
import model.JDBCDBManager;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {
    private DBManager dbManager;

    public ServiceImpl() {
        dbManager = new JDBCDBManager();
    }


    @Override
    public List<String> commands() {
        return Arrays.asList("menu", "help", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
            dbManager.connect(databaseName, userName, password);
    }
}
