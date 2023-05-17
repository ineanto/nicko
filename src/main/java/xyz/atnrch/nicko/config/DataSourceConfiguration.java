package xyz.atnrch.nicko.config;

public class DataSourceConfiguration {
    public static final DataSourceConfiguration SQL_EMPTY = new DataSourceConfiguration("127.0.0.1", 3306, "root", "");
    public static final DataSourceConfiguration REDIS_EMPTY = new DataSourceConfiguration("127.0.0.1", 6379, "", "");

    private final String address;
    private final Integer port;
    private final String username;
    private final String password;

    public DataSourceConfiguration(String address, Integer port, String username, String password) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public DataSourceConfiguration() { this("", 0, "", ""); }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
