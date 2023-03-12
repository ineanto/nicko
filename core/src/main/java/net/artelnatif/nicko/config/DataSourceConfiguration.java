package net.artelnatif.nicko.config;

public class DataSourceConfiguration {
    private final String address;
    private final String port;
    private final String username;
    private final String password;

    public DataSourceConfiguration(String address, String port, String username, String password) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
