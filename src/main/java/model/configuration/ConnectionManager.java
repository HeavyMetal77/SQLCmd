package model.configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    private static final String JDBC_POSTGRESQL_LOCALHOST = "jdbc:postgresql://localhost:5432/";
    private static final String JDBC_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private Connection connection;
    private PropertiesLoader propertiesLoader;
    private Configuration configuration;

    //получить соединение с помощью properties
    public Connection getConnection() throws Exception {
        if (connection != null) {
            return connection;
        }
        try {
            propertiesLoader = new PropertiesLoader();
        } catch (Exception e) {
            configuration = null;
        }

        if (propertiesLoader != null) {
            configuration = propertiesLoader.getConfiguration();
        }
        if (connection == null) {
            try {
                if (configuration != null) {
                    connection = DriverManager.getConnection(configuration.getUrl(),
                            configuration.getUserName(),
                            configuration.getPassword());
                }
            } catch (Exception e) {
                throw new Exception(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return connection;
    }

    //получить соединение с помощью авторизации
    public Connection getConnectionWithAuthorization(String database, String login, String password) throws Exception {
        if (connection != null) {
            return connection;
        }

        if (configuration == null) {
            try {
                Class.forName(JDBC_POSTGRESQL_DRIVER);
            } catch (Exception e) {
                throw new Exception("Отсутствует драйвер базы данных!", e);
            }
            connection = DriverManager.getConnection(
                    JDBC_POSTGRESQL_LOCALHOST + database, login, password);
        }
        return connection;
    }

    //проверить наличие соединения
    public boolean isConnected() {
        return connection != null;
    }
}


