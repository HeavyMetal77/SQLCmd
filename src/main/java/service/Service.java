package service;

import java.sql.Connection;
import java.util.List;

public interface Service {
    List<String> commands();

    Connection connect(String databaseName, String userName, String password);

    List<List<String>> find(String databaseName);

    void clear(String databaseName);
}
