package xyz.ineanto.nicko.config;

public class SQLDataSourceConfiguration extends DataSourceConfiguration {
    private final boolean mariadb;

    public SQLDataSourceConfiguration() { this(false, "", 0, "", "", true); }

    public SQLDataSourceConfiguration(boolean enabled, String address, Integer port, String username, String password, boolean mariadb) {
        super(enabled, address, port, username, password);
        this.mariadb = mariadb;
    }

    public boolean isMariadb() {
        return mariadb;
    }
}
