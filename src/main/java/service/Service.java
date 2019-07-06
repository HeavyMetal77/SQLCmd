package service;

import java.util.List;

public interface Service {
    List<String> commands();

    void connect(String databaseName, String userName, String password);
}
