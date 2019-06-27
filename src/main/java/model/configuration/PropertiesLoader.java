package model.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static final String CONFIG_SQLCMD_PROPERTIES = "src/main/resources/config/sqlcmd.properties";
    private Properties prop;

    public PropertiesLoader() throws Exception {
        prop = new Properties();
        File fileProperties = new File(CONFIG_SQLCMD_PROPERTIES);
        try (InputStream input = new FileInputStream(fileProperties)) {
            prop.load(input);
        } catch (Exception e) {
            throw new FileNotFoundException("Ошибка при загрузке конфигурации: "
                    + fileProperties.getAbsolutePath());
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
        return prop.getProperty("database.server.name");
    }

    private String getDatabaseName() {
        return prop.getProperty("database.name");
    }

    private String getPort() {
        return prop.getProperty("database.port");
    }

    private String getDriver() {
        return prop.getProperty("database.jdbc.driver");
    }

    private String getUserName() {
        return prop.getProperty("database.user.name");
    }

    private String getPassword() {
        return prop.getProperty("database.user.password");
    }
}
