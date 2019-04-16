package model.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class PropertiesLoader {
    public static final String CONFIG_SQLCMD_PROPERTIES = "src/main/resources/config/sqlcmd.properties";
    private Properties properties;

    public PropertiesLoader() throws Exception {
        properties = new Properties();
        File fileProperties = new File(CONFIG_SQLCMD_PROPERTIES);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileProperties);
            properties.load(fileInputStream);
        } catch (Exception e) {
            throw new FileNotFoundException("Ошибка при загрузке конфигурации: "
                    + fileProperties.getAbsolutePath());
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public Configuration getConfiguration() {
        return new Configuration(
                getDatabaseName(),
                getServerName(),
                getPort(),
                getUserName(),
                getDriver(),
                getPassword()
        );
    }

    private String getServerName() {
        return properties.getProperty("database.server.name");
    }

    private String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    private String getPort() {
        return properties.getProperty("database.port");
    }

    private String getDriver() {
        return properties.getProperty("database.jdbc.driver");
    }

    private String getUserName() {
        return properties.getProperty("database.user.name");
    }

    private String getPassword() {
        return properties.getProperty("database.user.password");
    }
}
