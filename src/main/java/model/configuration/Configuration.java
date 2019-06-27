package model.configuration;

public final class Configuration {
    private String databaseName;
    private String serverName;
    private String portNumber;
    private String userName;
    private String driver;
    private String password;

    public Configuration(String databaseName, String serverName,
                         String portNumber, String userName, String driver, String password) {
        this.databaseName = databaseName;
        this.serverName = serverName;
        this.portNumber = portNumber;
        this.userName = userName;
        this.driver = driver;
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getDriver() {
        return driver;
    }

    public String getPassword() {
        return password;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return String.format("%s%s:%s/%s", driver, serverName, portNumber, databaseName);
    }
}
